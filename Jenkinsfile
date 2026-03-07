pipeline {
    agent any

    stages {

        stage('Build Auth Service') {
            steps {
                sh 'cd auth-service && ./mvnw clean install'
            }
        }

        stage('Test Auth Service') {
            steps {
                sh 'cd auth-service && ./mvnw test'
            }
        }

        stage('Build Order Service') {
            steps {
                sh 'cd order-service && ./mvnw clean install'
            }
        }

        stage('Test Order Service') {
            steps {
                sh 'cd order-service && ./mvnw test'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                sh 'sonar-scanner'
            }
        }

        stage('Deploy') {
            steps {
                echo 'Microservices build successful'
            }
        }

    }
}
