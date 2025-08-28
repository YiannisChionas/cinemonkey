pipeline {
    agent any

    environment {
        DOCKER_TOKEN=credentials('docker-push-secret')
        DOCKER_USER='yiannischionas'
        DOCKER_SERVER='ghcr.io'
        DOCKER_PREFIX='ghcr.io/yiannischionas/cinemonkey'
    }

    stages {

        stage('Test') {
            steps {
                dir('cinemonkey-backend') {
                    sh '''
                        echo "Start testing"
                        set -eux
                        chmod +x mvnw || true
                        ./mvnw -V -B -Dspring.profiles.active=test -Dspring.datasource.url=jdbc:h2:mem:testdb test
                    '''
                }
            }
        }

        stage('Docker build and push') {
            steps {
                sh '''
                    HEAD_COMMIT=$(git rev-parse --short HEAD)
                    TAG=HEAD_COMMIT-$BUILD_ID
                    docker build --rm -t $DOCKER_PREFIX:latest -f nonroot-multistage.Dockerfile .
                '''
                sh '''
                    echo $DOCKER_TOKEN | docker login $DOCKER_SERVER -u $DOCKER_USER --password-stdin
                    docker push $DOCKER_PREFIX --all-tags
                '''
            }
        }
    }
}
