# Nethra Travel AI — partner-service
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml ./
COPY common-lib ./common-lib
COPY partner-service ./partner-service
RUN mvn -q -pl partner-service -am -DskipTests package
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/partner-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
