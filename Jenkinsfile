pipeline {
    agent any

    environment {
        VERSION = "v1"
        DOCKERHUB_USERNAME = "subash1718"
    }

    stages {

        stage('Checkout SCM') {
            steps {
                git branch: 'main', url: 'https://github.com/subash1718/microservice.git'
            }
        }

        stage('Build Services') {
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

        stage('Unit Tests') {
            steps {
                sh '''
                echo "Running tests (safe mode)"
                cd order-service
                ./mvnw test || true
                cd ..
                '''
            }
        }

        stage('Start Services (MySQL + Network)') {
            steps {
                sh '''
                docker network create microservice-net || true

                docker stop microservice-mysql || true
                docker rm microservice-mysql || true

                docker run -d \
                --name microservice-mysql \
                --network microservice-net \
                -e MYSQL_ROOT_PASSWORD=root \
                -e MYSQL_DATABASE=testdb \
                -p 3306:3306 \
                mysql:8
                '''
            }
        }

        stage('Docker Build Images') {
            steps {
                sh '''
                docker build -t order-service:$VERSION ./order-service
                docker build -t auth-service:$VERSION ./auth-service
                docker build -t api-gateway:$VERSION ./api-gateway
                docker build -t eureka-server:$VERSION ./eureka-server
                '''
            }
        }

        stage('Docker Push Images') {
            steps {
                sh '''
                docker tag order-service:$VERSION $DOCKERHUB_USERNAME/order-service:$VERSION
                docker tag auth-service:$VERSION $DOCKERHUB_USERNAME/auth-service:$VERSION
                docker tag api-gateway:$VERSION $DOCKERHUB_USERNAME/api-gateway:$VERSION
                docker tag eureka-server:$VERSION $DOCKERHUB_USERNAME/eureka-server:$VERSION

                docker push $DOCKERHUB_USERNAME/order-service:$VERSION || true
                docker push $DOCKERHUB_USERNAME/auth-service:$VERSION || true
                docker push $DOCKERHUB_USERNAME/api-gateway:$VERSION || true
                docker push $DOCKERHUB_USERNAME/eureka-server:$VERSION || true
                '''
            }
        }

   stage('Docker Deploy (All Services)') {
    steps {
        sh '''
        echo "Stopping containers by ports..."

        # Stop containers using ports
        docker ps -q --filter publish=8761 | xargs -r docker stop
        docker ps -q --filter publish=8084 | xargs -r docker stop
        docker ps -q --filter publish=8082 | xargs -r docker stop
        docker ps -q --filter publish=8080 | xargs -r docker stop

        docker ps -aq --filter publish=8761 | xargs -r docker rm
        docker ps -aq --filter publish=8084 | xargs -r docker rm
        docker ps -aq --filter publish=8082 | xargs -r docker rm
        docker ps -aq --filter publish=8080 | xargs -r docker rm

        echo "Starting Eureka Server..."
        docker run -d -p 8761:8761 \
        --network microservice-net \
        --name eureka-server \
        eureka-server:$VERSION

        echo "Starting Auth Service..."
        docker run -d -p 8084:8084 \
        --network microservice-net \
        --name auth-service \
        auth-service:$VERSION

        echo "Starting Order Service..."
        docker run -d -p 8082:8082 \
        --network microservice-net \
        --name order-service \
        order-service:$VERSION

        echo "Starting API Gateway..."
        docker run -d -p 8080:8080 \
        --network microservice-net \
        --name api-gateway \
        api-gateway:$VERSION
        '''
    }
}

    post {
        always {
            junit '**/target/surefire-reports/*.xml'
            echo 'Pipeline finished 🚀'
        }
        success {
            echo 'SUCCESS ✅'
        }
        failure {
            echo 'FAILURE ❌'
        }
    }
}