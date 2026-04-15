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
                  ./mvnw clean package -Dmaven.test.skip=true
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

        stage('Deploy Order Service') {
            steps {
                sh '''
                echo "Stopping containers using port 8082..."

                docker ps -q --filter "publish=8082" | xargs -r docker stop
                docker ps -aq --filter "publish=8082" | xargs -r docker rm

                echo "Starting new order-service container..."

                docker run -d \
                -p 8082:8082 \
                --network microservice-net \
                --name order-service \
                order-service:$VERSION
                '''
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished 🚀'
        }
        success {
            echo '✅ SUCCESS: Deployment completed'
        }
        failure {
            echo '❌ FAILURE: Check logs'
        }
    }
}
