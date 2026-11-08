# Nethra Travel AI — destination-service
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml ./
COPY common-lib ./common-lib
COPY destination-service ./destination-service
RUN mvn -q -pl destination-service -am -DskipTests package
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/destination-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
