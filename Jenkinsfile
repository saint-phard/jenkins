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
    }
}
