def buildJar() {
    echo 'building the application with maven'
    sh 'mvn clean package'
}

def buildImage() {
    echo 'building the docker image'
    withCredentials([usernamePassword(credentialsId: 'my-dockerhub-credentials', usernameVariable: 'username', passwordVariable: 'password')]) {
        sh 'docker build -t phard/the-app:${IMAGE_NAME} .'
        sh "echo $password | docker login -u $username --password-stdin"
        sh 'docker push phard/the-app:${IMAGE_NAME}'
    }
}

def deployApp() {
    echo 'deploying the application'
}

return this