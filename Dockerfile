FROM openjdk:17-jdk-slim


# ARG JAR_FILE=build/libs/*-SNAPSHOT.jar

# ★ 변수 대신 와일드카드를 직접 사용
COPY build/libs/*SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
