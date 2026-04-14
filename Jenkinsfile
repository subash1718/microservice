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

        stage('Build Order Service (Skip Tests)') {
            steps {
                sh '''
                cd order-service
                chmod +x mvnw
                ./mvnw clean package -DskipTests
                '''
            }
        }

        stage('Docker Build') {
            steps {
                sh '''
                cd order-service
                docker build -t order-service:v1 .
                '''
            }
        }

        stage('Deploy Container') {
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
        always {
            echo 'Pipeline finished'
        }
    }
}
