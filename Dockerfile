FROM openjdk:17-jdk-slim

# build/libs/ 안의 어떤 JAR 버전이든 하나만 복사
COPY build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
