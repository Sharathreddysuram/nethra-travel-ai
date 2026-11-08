# Nethra Travel AI — ai-planner-service
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml ./
COPY common-lib ./common-lib
COPY ai-planner-service ./ai-planner-service
RUN mvn -q -pl ai-planner-service -am -DskipTests package
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/ai-planner-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
