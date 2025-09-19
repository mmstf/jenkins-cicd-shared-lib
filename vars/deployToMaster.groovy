def call() {
    withCredentials(
        [
            usernamePassword(
            credentialsId: 'docker-pat',
            usernameVariable: 'DOCKER_USER',
            passwordVariable: 'DOCKER_PASS'
    )]) {
            sh '''
                echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                docker pull docker.io/mmstf/nodemain:v1.0
            '''

            sh """
                OLD_CONTAINER=\$(docker ps -q --filter name=main-app)
                if [ "\$OLD_CONTAINER" ]; then
                    docker stop \$OLD_CONTAINER
                    docker rm \$OLD_CONTAINER
                fi
            """

            sh "docker run -d --name main-app -p 3000:3000 mmstf/nodemain:v1.0"
        }
}