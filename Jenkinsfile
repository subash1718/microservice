pipeline {
    agent any

    environment {
        VERSION = "${BUILD_NUMBER}"
        DOCKERHUB_USERNAME = "subash1718"
    }

    stages {

        stage('Checkout SCM') {
            steps {
                git branch: 'main', url: 'https://github.com/subash1718/microservice.git'
            }
        }

        // ✅ UNIT TESTS
        stage('Unit Tests') {
            steps {
                sh '''
                cd order-service
                chmod +x mvnw
                ./mvnw clean test
                '''
            }
        }

        // ✅ BUILD (NO TESTS HERE)
        stage('Build Services') {
            steps {
                sh '''
                cd order-service && ./mvnw clean package -DskipTests && cd ..
                cd auth-service && chmod +x mvnw && ./mvnw clean package -DskipTests && cd ..
                cd api-gateway && chmod +x mvnw && ./mvnw clean package -DskipTests && cd ..
                cd eureka-server && chmod +x mvnw && ./mvnw clean package -DskipTests && cd ..
                '''
            }
        }

        // ✅ MYSQL + NETWORK SETUP
        stage('Create Network & MySQL') {
            steps {
                sh '''
                docker network create microservice-net || true

                # 🔥 Stop any container using MySQL port
                docker ps -q --filter "publish=3306" | xargs -r docker stop

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

        // ✅ BUILD DOCKER IMAGES
        stage('Build Docker Images') {
            steps {
                sh '''
                docker build -t $DOCKERHUB_USERNAME/order-service:$VERSION ./order-service
                docker build -t $DOCKERHUB_USERNAME/auth-service:$VERSION ./auth-service
                docker build -t $DOCKERHUB_USERNAME/api-gateway:$VERSION ./api-gateway
                docker build -t $DOCKERHUB_USERNAME/eureka-server:$VERSION ./eureka-server
                '''
            }
        }

        // ✅ START SERVICES (FIXED PORT ISSUES)
        stage('Start Services') {
            steps {
                sh '''
                docker rm -f eureka-server auth-service order-service api-gateway || true

                # 🔥 FREE PORTS (CRITICAL FIX)
                docker ps -q --filter "publish=8761" | xargs -r docker stop
                docker ps -q --filter "publish=8080" | xargs -r docker stop
                docker ps -q --filter "publish=8082" | xargs -r docker stop
                docker ps -q --filter "publish=8084" | xargs -r docker stop

                docker run -d -p 8761:8761 --network microservice-net --name eureka-server $DOCKERHUB_USERNAME/eureka-server:$VERSION
                sleep 15

                docker run -d -p 8084:8084 --network microservice-net --name auth-service $DOCKERHUB_USERNAME/auth-service:$VERSION
                docker run -d -p 8082:8082 --network microservice-net --name order-service $DOCKERHUB_USERNAME/order-service:$VERSION
                docker run -d -p 8080:8080 --network microservice-net --name api-gateway $DOCKERHUB_USERNAME/api-gateway:$VERSION

                echo "Waiting for services..."
                sleep 25
                '''
            }
        }

        // ✅ KARATE INTEGRATION TESTS ONLY
        stage('Integration Test (Karate)') {
            steps {
                sh '''
                cd order-service
                ./mvnw test -Dtest=KarateTest
                '''
            } 
        }

        // ✅ SONAR (NO REBUILD)
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonar-server') {
                    withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                        sh '''
                        cd order-service
                        ./mvnw sonar:sonar \
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
            // ✅ TEST REPORTS
            junit 'order-service/target/surefire-reports/*.xml'

            // ✅ CODE COVERAGE
            jacoco(
                execPattern: 'order-service/target/jacoco.exec',
                classPattern: 'order-service/target/classes',
                sourcePattern: 'order-service/src/main/java'
            )

            echo 'Pipeline finished 🚀'
        }
    }
}
