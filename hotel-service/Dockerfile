# Nethra Travel AI — hotel-service
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml ./
COPY common-lib ./common-lib
COPY hotel-service ./hotel-service
RUN mvn -q -pl hotel-service -am -DskipTests package
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/hotel-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
