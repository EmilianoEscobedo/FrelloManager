FROM maven:3.9.1-amazoncorretto-19 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:19-jdk-alpine
WORKDIR /app
COPY --from=builder /app/target/FrelloManager-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]