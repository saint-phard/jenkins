#!/usr/bin/env groovy

library identifier: 'jenkins-shared-library@main', retriever: modernSCM(
    [$class: 'GitSCMSource',
     remote: 'https://github.com/saint-phard/jenkins-shared-library.git',
     credentialsId: 'my-github-credentials'
    ]
)
def groovy

pipeline {
    agent any
    tools {
        maven 'maven-3.8.6'
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
                    sh 'mvn versions:set \
                        DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
                        versions:commit'
                    def matchAppVersion = readFile('pom.xml') =~ '<version>(.+)</version>'
                    def appVersion = matchAppVersion [0][1]
                    env.IMAGE_NAME = "$appVersion-$BUILD_NUMBER"
                }
            }
        }
        stage('build jar') {
            steps {
                script {
                    buildJar()
                }
            }
        }
        stage('build and push image') {
            steps {
                script {
                    buildImage "phard/the-app:$IMAGE_NAME ."
                    dockerLogin()
                    dockerPush "phard/the-app:$IMAGE_NAME"
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
    }
}
