# Nethra Travel AI — transport-service
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml ./
COPY common-lib ./common-lib
COPY transport-service ./transport-service
RUN mvn -q -pl transport-service -am -DskipTests package
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/transport-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
