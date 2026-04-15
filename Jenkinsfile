pipeline {
agent any

environment {
    VERSION = "v1"
    DOCKERHUB_USERNAME = "yourdockerhubusername"
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
            echo "Building order-service"
            cd order-service
            chmod +x mvnw
            ./mvnw clean package -Dmaven.test.skip=true
            cd ..

            echo "Building auth-service"
            cd auth-service
            chmod +x mvnw
            ./mvnw clean package -Dmaven.test.skip=true
            cd ..

            echo "Building api-gateway"
            cd api-gateway
            chmod +x mvnw
            ./mvnw clean package -Dmaven.test.skip=true
            cd ..

            echo "Building eureka-server"
            cd eureka-server
            chmod +x mvnw
            ./mvnw clean package -Dmaven.test.skip=true
            cd ..
            '''
        }
    }

    stage('Docker Build All') {
        steps {
            sh '''
            echo "Building Docker images..."
            docker build -t order-service:$VERSION ./order-service
            docker build -t auth-service:$VERSION ./auth-service
            docker build -t api-gateway:$VERSION ./api-gateway
            docker build -t eureka-server:$VERSION ./eureka-server
            '''
        }
    }

    stage('Push to Docker Hub') {
        steps {
            sh '''
            echo "Pushing images to Docker Hub..."

            docker tag order-service:$VERSION $DOCKERHUB_USERNAME/order-service:$VERSION
            docker push $DOCKERHUB_USERNAME/order-service:$VERSION

            docker tag auth-service:$VERSION $DOCKERHUB_USERNAME/auth-service:$VERSION
            docker push $DOCKERHUB_USERNAME/auth-service:$VERSION

            docker tag api-gateway:$VERSION $DOCKERHUB_USERNAME/api-gateway:$VERSION
            docker push $DOCKERHUB_USERNAME/api-gateway:$VERSION

            docker tag eureka-server:$VERSION $DOCKERHUB_USERNAME/eureka-server:$VERSION
            docker push $DOCKERHUB_USERNAME/eureka-server:$VERSION
            '''
        }
    }

    stage('Deploy Order Service') {
        steps {
            sh '''
            echo "Stopping old container..."
            docker stop order-service || true
            docker rm order-service || true

            echo "Starting new container..."
            docker run -d -p 8082:8082 \
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
```

}

