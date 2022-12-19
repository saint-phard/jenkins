def buildJar() {
    echo 'building the application with maven'
    sh 'mvn package'
}

def buildImage() {
    echo 'building the docker image'
    withCredentials([usernamePassword(credentialsId: 'my-dockerhub-credentials', usernameVariable: 'username', passwordVariable: 'password')]) {
        sh 'docker build -t phard/the-app:maven-2.0 .'
        sh "echo $password | docker login -u $username --password-stdin"
        sh 'docker push phard/the-app:maven-2.0'
    }
}

def deployApp() {
    echo 'deploying the application'
}

return this