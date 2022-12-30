def deployApp() {
    echo 'deploying the application'
}

def githubCommit() {
    echo 'commiting all changes to github to reflect new version changes'
    withCredentials([usernamePassword(credentialsId: 'my-github-credentials', usernameVariable: 'username', passwordVariable: 'password')]) {
        sh 'git config ---global user.name "jenkins-user"'
        sh 'git config ---global user.email "jenkins-user@local.com"'

        sh 'git status'
        sh 'git branch'
        sh 'git config --list'

        sh "git remote set-url origin https://${username}:${password}@https://github.com/saint-phard/maven-docker-jenkins.git"
        sh 'git add .'
        sh 'git commit -m "jenkins-build: app version increment"'
        sh 'git push origin HEAD:jenkins-shared-library'
    }
}

return this