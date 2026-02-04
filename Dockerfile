FROM maven:3.9.12-eclipse-temurin-25-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn clean verify

FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=build /app/target/*.jar app.jar

COPY --from=build /app/target/site/jacoco /app/static-content/jacoco

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]