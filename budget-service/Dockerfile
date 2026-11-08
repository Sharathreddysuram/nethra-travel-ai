# Nethra Travel AI — budget-service
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml ./
COPY common-lib ./common-lib
COPY budget-service ./budget-service
RUN mvn -q -pl budget-service -am -DskipTests package
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/budget-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
