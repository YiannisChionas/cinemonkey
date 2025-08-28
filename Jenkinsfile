pipeline {
    agent any

    environment {
        DOCKER_TOKEN=credentials('docker-push-secret')
        DOCKER_USER='yiannischionas'
        DOCKER_SERVER='ghcr.io'
        DOCKER_PREFIX='ghcr.io/yiannischionas/cinemonkey'
        DOCKER_IMAGE_FRONT = 'ghcr.io/yiannischionas/cinemonkey-frontend'
        DOCKER_IMAGE_BACKEND="ghcr.io/yiannischionas/cinemonkey-backend"
    }

    stages {

        stage('Test') {
            steps {
                dir('cinemonkey-backend') {
                    sh '''
                        echo "Start testing"
                        set -eux
                        docker run --rm -v "$PWD":/app -w /app maven:3.9-eclipse-temurin-17 \
                                  mvn -V -B -Dspring.profiles.active=test \
                                         -Dspring.datasource.url=jdbc:h2:mem:testdb test
                    '''
                }
            }
        }

        stage('Docker build and push') {
            steps {
                dir('cinemonkey-backend') {
                    sh '''
                        HEAD_COMMIT=$(git rev-parse --short=8 HEAD)
                        TAG=${HEAD_COMMIT}-${BUILD_ID}

                        docker build --rm -t $DOCKER_PREFIX:latest -f Dockerfile \
                        -t ${DOCKER_IMAGE_BACKEND}:$TAG \
                        -t ${DOCKER_IMAGE_BACKEND}:latest \
                        .

                        echo $DOCKER_TOKEN | docker login $DOCKER_SERVER -u $DOCKER_USER --password-stdin
                        docker push ${DOCKER_IMAGE_BACKEND}:$TAG
                        docker push ${DOCKER_IMAGE_BACKEND}:latest
                        docker logout $DOCKER_SERVER || true
                    '''
                }
            }
        }
        stage('Build & Push Frontend Image') {
            steps {
                dir('cinemonkey-frontend') {          // φάκελος frontend στο monorepo
                    sh '''
                        set -eux
                        HEAD_COMMIT=$(git rev-parse --short=8 HEAD)
                        TAG="${HEAD_COMMIT}-${BUILD_ID}"

                        docker build --rm \
                        -f Dockerfile \
                        -t ${DOCKER_IMAGE_FRONT}:$TAG \
                        -t ${DOCKER_IMAGE_FRONT}:latest \
                        .

                        echo "$DOCKER_TOKEN" | docker login $DOCKER_SERVER -u $DOCKER_USER --password-stdin
                        docker push ${DOCKER_IMAGE_FRONT}:$TAG
                        docker push ${DOCKER_IMAGE_FRONT}:latest
                        docker logout $DOCKER_SERVER || true
                    '''
                }
            }
        }
    }
}
