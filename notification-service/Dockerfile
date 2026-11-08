# Nethra Travel AI — notification-service
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml ./
COPY common-lib ./common-lib
COPY notification-service ./notification-service
RUN mvn -q -pl notification-service -am -DskipTests package
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/notification-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
