def buildJar() {
    echo 'building the application with maven'
    sh 'mvn clean package'
}

def buildImage() {
    echo 'building the docker image'
    withCredentials([usernamePassword(credentialsId: 'my-dockerhub-credentials', usernameVariable: 'username', passwordVariable: 'password')]) {
        sh 'docker build -t ${DOCKER_REPO}:${IMAGE_NAME} .'
        sh "echo $password | docker login -u $username --password-stdin ${DOCKER_REPO_SERVER}"
        sh 'docker push ${DOCKER_REPO}:${IMAGE_NAME}'
    }
}

def deployApp() {
    echo 'deploying the application'
    withCredentials([usernamePassword(credentialsId: 'my-dockerhub-credentials', usernameVariable: 'username', passwordVariable: 'password')]) {
        sh 'envsubst < k8s/deployment.yaml | kubectl apply -f -'
        sh 'envsubst < k8s/service.yaml | kubectl apply -f -'
        sh "kubectl create secret docker-registry ${DOCKER_REPO_SECRET} \
            --docker-server=${DOCKER_REPO_SERVER}\
            --docker-username=$username \
            --docker-password=$password \
            --docker-email=phard911@gmail.com"
    }
}

def githubCommit() {
    echo 'commiting all changes to github to reflect new version changes'
    withCredentials([usernamePassword(credentialsId: 'my-github-credentials-pat', usernameVariable: 'username', passwordVariable: 'password')]) {
        sh 'git config --global user.name "jenkins-user"'
        sh 'git config --global user.email "jenkins-user@local.com"'

        sh 'git status'
        sh 'git branch'
        sh 'git config --list'

        sh "git remote set-url origin https://${username}:${password}@github.com/saint-phard/maven-docker-jenkins.git"
        sh 'git add .'
        sh 'git commit -m "jenkins-build: app version increment"'
        sh 'git push origin HEAD:jenkins-shared-library'
    }
}

return this