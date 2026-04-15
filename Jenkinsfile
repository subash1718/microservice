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
                cd order-service && chmod +x mvnw && ./mvnw clean package -DskipTests && cd ..
                cd auth-service && chmod +x mvnw && ./mvnw clean package -DskipTests && cd ..
                cd api-gateway && chmod +x mvnw && ./mvnw clean package -DskipTests && cd ..
                cd eureka-server && chmod +x mvnw && ./mvnw clean package -DskipTests && cd ..
                '''
            }
        }

        stage('Create Network & MySQL') {
            steps {
                sh '''
                docker network create microservice-net || true

                docker rm -f microservice-mysql || true

                docker run -d \
                --name microservice-mysql \
                --network microservice-net \
                -e MYSQL_ROOT_PASSWORD=root \
                -e MYSQL_DATABASE=orderdb \
                -p 3306:3306 \
                mysql:8
                '''
            }
        }

        stage('Build Docker Images') {
            steps {
                sh '''
                docker build -t order-service:$VERSION ./order-service
                docker build -t auth-service:$VERSION ./auth-service
                docker build -t api-gateway:$VERSION ./api-gateway
                docker build -t eureka-server:$VERSION ./eureka-server
                '''
            }
        }

        stage('Start Services') {
            steps {
                sh '''
                docker rm -f eureka-server auth-service order-service api-gateway || true

                docker run -d -p 8761:8761 --network microservice-net --name eureka-server eureka-server:$VERSION
                sleep 15

                docker run -d -p 8084:8084 --network microservice-net --name auth-service auth-service:$VERSION
                docker run -d -p 8082:8082 --network microservice-net --name order-service order-service:$VERSION
                docker run -d -p 8080:8080 --network microservice-net --name api-gateway api-gateway:$VERSION

                echo "Waiting for services..."
                sleep 25
                '''
            }
        }

        stage('Integration Test (Karate)') {
            steps {
                sh '''
                cd order-service
                ./mvnw test
                '''
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonar-server') {
                    withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                        sh '''
                        cd order-service
                        ./mvnw clean verify sonar:sonar \
                        -Dsonar.projectKey=order-service \
                        -Dsonar.projectName=order-service \
                        -Dsonar.host.url=http://host.docker.internal:9000 \
                        -Dsonar.login=$SONAR_TOKEN
                        '''
                    }
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished 🚀'
        }
    }
}