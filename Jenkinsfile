pipeline {
    agent any

    tools {
        jdk 'jdk17'         // Jenkins 관리 > JDK 이름과 정확히 일치해야 함
        gradle 'gradle'     // Jenkins 관리 > Gradle 이름과 일치
    }

    environment {
        DEPLOY_SERVER = "ubuntu@43.200.125.20"
        PROJECT_PATH = "/home/ubuntu/hilingual"
        JAR_NAME = "HILINGUAL-SERVER-1.0-SNAPSHOT.jar"
    }

    stages {

        stage('Check Java & Gradle Version') {
            steps {
                sh '''
                    echo "[DEBUG] JAVA HOME: $JAVA_HOME"
                    which java
                    java -version
                    ./gradlew --version
                '''
            }
        }

        stage('Git Clone') {
            steps {
                checkout scm
            }
        }

        stage('Build JAR') {
            steps {
                sh '''
                    chmod +x ./gradlew
                    ./gradlew clean build --refresh-dependencies --no-daemon -x test
                '''
            }
        }

        stage('Deploy to EC2') {
            when {
                expression { env.BRANCH_NAME == 'develop' }
            }
            steps {
                sshagent(credentials: ['hilingual-dev-key']) {
                    sh '''
                        ssh -o StrictHostKeyChecking=no $DEPLOY_SERVER "mkdir -p $PROJECT_PATH/build/libs"

                        scp build/libs/$JAR_NAME $DEPLOY_SERVER:$PROJECT_PATH/build/libs/

                        ssh -o StrictHostKeyChecking=no $DEPLOY_SERVER "
                            cd $PROJECT_PATH &&
                            chmod +x deploy.sh &&
                            ./deploy.sh
                        "
                    '''
                }
            }
        }
    }
}
