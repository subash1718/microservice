pipeline {
    agent any

    stages {

        stage('Build (Maven)') {
            steps {
                sh '''
                docker run --rm \
                -v $PWD:/app \
                -w /app \
                maven:3.9.9-eclipse-temurin-21 \
                mvn clean package
                '''
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t order-service:v1 .'
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                docker stop order-service || true
                docker rm order-service || true
                docker run -d -p 8082:8082 --network microservice-net --name order-service order-service:v1
                '''
            }
        }
    }

    post {
        success {
            echo '✅ Pipeline executed successfully!'
        }
        failure {
            echo '❌ Pipeline failed. Check logs.'
        }
    }
}
