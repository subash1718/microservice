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
                    sh 'mvn clean install -DskipTests'
                }
            }
        }

        stage('Build Order Service') {
            steps {
                dir('order-service') {
                    sh 'mvn clean install -DskipTests'
                }
            }
        }

        stage('Build API Gateway') {
            steps {
                dir('api-gateway') {
                    sh 'mvn clean install -DskipTests'
                }
            }
        }

        stage('Test & Coverage') {
            steps {
                dir('order-service') {
                    sh 'mvn test'
                }
            }
        }
    }

    post {
        always {
            jacoco execPattern: 'order-service/target/jacoco.exec',
                   classPattern: 'order-service/target/classes',
                   sourcePattern: 'order-service/src/main/java'
        }
    }
}