pipeline {
    agent any

    tools {
        jdk 'jdk17'
        gradle 'gradle'
    }

    environment {
        DEPLOY_SERVER = "ubuntu@43.200.125.20"
        PROJECT_PATH = "/home/ubuntu/hilingual"
        JAR_NAME = "HILINGUAL-SERVER-1.0-SNAPSHOT.jar"
        DISCORD_WEBHOOK_URL = credentials('discord-webhook-url')
    }

    stages {
        stage('Git Clone') {
            steps {
                checkout scm
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
  stage('Notify to Discord') {
              when {
                  expression { env.BRANCH_NAME == 'develop' }
              }
              steps {
                  sh '''
                      curl -X POST "$DISCORD_WEBHOOK_URL" \
                      -H "Content-Type: application/json" \
                      -d '{
                            "content": ":rocket: *[Hilingual]* `develop` 브랜치에 코드가 푸시되고 배포되었습니다!\\n커밋 메시지: '${GIT_COMMIT}'"
                          }'
                  '''
              }
          }
      }
  }
