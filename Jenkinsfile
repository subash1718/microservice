pipeline {
    agent any

    options {
        skipDefaultCheckout(true)
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/subash1718/microservice.git'
            }
        }

        stage('Debug Structure') {
            steps {
                sh 'ls -R'
            }
        }

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
                sh '''
                cd order-service || cd .
                docker build -t order-service:v1 .
                '''
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                docker stop order-service || true
                docker rm order-service || true
                docker run -d -p 8082:8082 --network microservice-net order-service:v1
                '''
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished'
        }
    }
}
