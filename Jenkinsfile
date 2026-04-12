pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK21'
    }

    environment {
        SONAR_TOKEN = credentials('sonar-token')
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/subash1718/microservice.git'
            }
        }

        stage('Build All Services') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Test & Coverage (JUnit + JaCoCo)') {
            steps {
                dir('order-service') {
                    sh 'mvn test'
                }
            }
            post {
                always {
                    junit 'order-service/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Code Quality - SonarQube') {
            steps {
                dir('order-service') {
                    sh 'mvn sonar:sonar -Dsonar.login=$SONAR_TOKEN'
                }
            }
        }

        stage('Package Services') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Building Docker Images...'

                sh 'docker build -t order-service ./order-service'
                sh 'docker build -t auth-service ./auth-service'
                sh 'docker build -t api-gateway ./api-gateway'
            }
        }

        stage('Run Containers (Docker Compose)') {
            steps {
                echo 'Starting containers...'
                sh 'docker-compose down || true'
                sh 'docker-compose up -d'
            }
        }
    }

    post {
        always {
            jacoco execPattern: 'order-service/target/jacoco.exec',
                   classPattern: 'order-service/target/classes',
                   sourcePattern: 'order-service/src/main/java'
        }

        success {
            echo '✅ Pipeline executed successfully!'
        }

        failure {
            echo '❌ Pipeline failed. Check logs.'
        }
    }
}