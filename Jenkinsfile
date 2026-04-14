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

        stage('Build Order Service (Maven Wrapper)') {
            steps {
                sh '''
                cd order-service
                chmod +x mvnw
                ./mvnw clean package
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
