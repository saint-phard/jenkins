#!/usr/bin/env groovy

def groovy

pipeline {
    agent any
    tools {
        maven 'maven-3.8.6'
    }
    environment {
        APP_NAME = jenkins-maven-app
        DOCKER_REPO = phard/the-app
        DOCKER_REPO_SERVER = index.docker.io/v1/ 
        //DOCKER_REPO_EMAIL = phard911@gmail.com
        DOCKER_REGISTRY_SECRET = docker-registry-key
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
        stage('unit test + jacoco plugin') {
            steps {
                script {
                    echo 'running unit test before the application is packaged'
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
            environment {
                
            }
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
