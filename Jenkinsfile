pipeline {
    agent any

    tools{
        maven 'Maven3'
        jdk 'JDK21'
    }
    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/subash1718/microservice.git'
            }
        }

        stage('Build Auth Service') {
            steps {
                dir('auth-service') {
                    sh'mvn clean install -DskipTests'
                }
            }
        }

        stage('Build Order Service') {
            steps {
                dir('order-service') {
                    sh'mvn clean install -DskipTests'
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
