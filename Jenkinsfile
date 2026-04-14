pipeline {
    agent any

    stages {

        stage('Build') {
            steps {
                sh 'mvn clean package'
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
                docker run -d -p 8082:8082 --network microservice-net order-service:v1
                '''
            }
        }
    }
}
