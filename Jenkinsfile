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
                    sh 'mvn build-helper:parse-version versions:set \
                        -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
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
