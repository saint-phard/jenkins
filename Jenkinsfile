pipeline {
    agent any
    tools {
        maven 'maven-3.8.6'
    }

    stages {
        stage('build jar') {
            steps {
                echo 'building the application with maven'
                sh 'mvn package'
            }
        }
        stage('build image') {
            steps {
                echo 'building the docker image'
                withCredentials([usernamePassword(credentialsId: 'my-dockerhub-credentials', usernameVariable: 'username', passwordVariable: 'password')]) {
                    sh 'docker build -t phard/the-app:maven-2.0 .'
                    sh "echo $password | docker login -u $username --password-stdin"
                    sh 'docker push phard/the-app:maven-2.0'
                }    
            }
          }
        stage('deploy') {
            steps {
                echo 'deploy stage'
            }
        }
    }
}
