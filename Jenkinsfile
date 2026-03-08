pipeline {
    agent any

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/subash1718/microservice.git'
            }
        }

        stage('Build Auth Service') {
            steps {
                dir('auth-service') {
                    sh './mvnw clean install'
                }
            }
        }

        stage('Build Order Service') {
            steps {
                dir('order-service') {
                    sh './mvnw clean install'
                }
            }
        }

        stage('Build API Gateway') {
            steps {
                dir('api-gateway') {
                    sh './mvnw clean install'
                }
            }
        }

    }
}
