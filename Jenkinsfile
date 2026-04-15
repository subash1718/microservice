pipeline {
    agent any

    environment {
        VERSION = "v1"
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/subash1718/microservice.git'
            }
        }

        stage('Build All Services') {
            steps {
                sh '''
                for service in order-service auth-service api-gateway eureka-server
                do
                  echo "Building $service"
                  cd $service
                  chmod +x mvnw
                  ./mvnw clean package -DskipTests
                  cd ..
                done
                '''
            }
        }

        stage('Docker Build All') {
            steps {
                sh '''
                docker build -t order-service:$VERSION ./order-service
                docker build -t auth-service:$VERSION ./auth-service
                docker build -t api-gateway:$VERSION ./api-gateway
                docker build -t eureka-server:$VERSION ./eureka-server
                '''
            }
        }

        stage('Deploy All Services') {
            steps {
                sh '''
                docker-compose down || true
                docker-compose up -d
                '''
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished 🚀'
        }
    }
}
