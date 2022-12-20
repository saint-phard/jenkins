@Library('jenkins-shared-library')
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
        stage('build jar') {
            steps {
                script {
                    buildJar()
                }
            }
        }
        stage('build image') {
            steps {
                script {
                    buildImage 'phard/the-app:maven-2.0'
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
