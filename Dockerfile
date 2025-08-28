FROM openjdk:17-jdk-slim
WORKDIR /
COPY hilingual-api/build/libs/app.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
