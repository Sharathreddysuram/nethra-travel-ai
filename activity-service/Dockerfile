# Nethra Travel AI — activity-service
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml ./
COPY common-lib ./common-lib
COPY activity-service ./activity-service
RUN mvn -q -pl activity-service -am -DskipTests package
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/activity-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
