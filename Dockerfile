FROM eclipse-temurin:19.0.2_7-jdk-alpine
EXPOSE 8082
ARG JAR_FILE=target/activity-tracker-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]