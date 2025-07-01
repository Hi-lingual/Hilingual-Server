pipeline {
    agent any

    tools {
        jdk 'jdk17'
        gradle 'gradle'
    }

    environment {
        DEPLOY_SERVER = "ubuntu@13.124.29.74"
        PROJECT_PATH = "/home/ubuntu/hilingual"
        JAR_NAME = "HILINGUAL-SERVER-1.0-SNAPSHOT.jar"
    }

    stages {
        stage('Git Clone') {
            steps {
                git branch: 'develop',
                    credentialsId: 'Jenkins',
                    url: 'https://github.com/Hi-lingual/Hilingual-Server.git'
            }
        }

        stage('Build JAR') {
    steps {
        sh '''
            chmod +x ./gradlew
            ./gradlew clean build
        '''
    }
}


        stage('Deploy to EC2') {
            steps {
                sshagent(credentials: ['hilingual-dev-key']) {
                    sh """
                    # EC2 디렉토리 준비
                    ssh -o StrictHostKeyChecking=no \$DEPLOY_SERVER 'mkdir -p \$PROJECT_PATH'

                    # 필수 파일 전송: JAR, Dockerfile, docker-compose.yml
                    scp -o StrictHostKeyChecking=no build/libs/\$JAR_NAME Dockerfile docker-compose.yml \$DEPLOY_SERVER:\$PROJECT_PATH

                    # EC2에서 Docker Compose 빌드 및 실행
                    ssh -o StrictHostKeyChecking=no \$DEPLOY_SERVER "
                        cd \$PROJECT_PATH &&
                        docker compose down || true &&
                        docker compose up --build -d
                    "
                    """
                }
            }
        }
    }
}
