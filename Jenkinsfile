#!/usr/bin/env groovy

def groovy

pipeline {
    agent any
    tools {
        maven 'maven-3.8.6'
    }
    environment {
        APP_NAME = 'jenkins-maven-app'
        DOCKER_REPO = 'phard/the-app'
        DOCKER_REPO_SERVER = 'index.docker.io/v1/'
        DOCKER_REPO_SECRET = 'docker-repo-key'
        DOCKER_REPO_EMAIL = 'phard911@gmail.com'
    }

    stages {
        stage('initialise groovy script') {
            steps {
                script {
                    echo 'initializing the groovy script'
                    groovy = load "script.groovy"
                }
            }
        }
        stage('increment app version') {
            steps {
                script {
                    echo 'incrementing maven app version'
                    sh 'mvn build-helper:parse-version versions:set \
                        -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
                        versions:commit'
                    def matchAppVersion = readFile('pom.xml') =~ '<version>(.+)</version>'
                    def appVersion = matchAppVersion [0][1]
                    env.IMAGE_NAME = "$appVersion-$BUILD_NUMBER"
                }
            }
        }
        stage('Unit Tests - JUnit and Jacoco') {
            steps {
                script {
                    echo 'running Unit Test before the application is packaged'
                    sh 'mvn test'
                }
            }
            post {
                always {
                    junit 'target/surefire-reports/TEST-AppText.xml'
                    jacoco execPattern: 'target/jacoco.exec'
                }
            }
        }
        stage('Mutation Test - PIT') {
            steps {
                script {
                    echo 'running Mutation Test to test the Unit Test'
                    sh 'mvn org.pitest-maven:mutationCoverage'
                }
            }
            post {
                always {
                    pitmutation mutationStatsFile: '**/target/pit-reports/**/mutations.xml'
                }
            }
        }
        stage('build jar') {
            steps {
                script {
                    groovy.buildJar()
                }
            }
        }
        stage('build image') {
            steps {
                script {
                    groovy.buildImage()
                }
                }    
          }
        stage('deploy') {
            steps {
                script {
                    groovy.deployApp()
                }
            }
        }
        stage('commit to github') {
            steps {
                script {
                    groovy.githubCommit()
                }
            }
        }
    }
}
