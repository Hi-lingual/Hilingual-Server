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
                            docker compose down || true &&
                            docker compose up --build -d
                        "
                    '''
                }
            }
        }
    }
}
