# Stage 1: Byg Java-projektet
FROM maven:3.9.12-eclipse-temurin-25 AS build

WORKDIR /app

# Download dependencies før kildekoden kopieres
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Kopiér kildekoden og byg JAR-filen uden at køre tests
COPY src ./src
RUN mvn clean package -DskipTests -B

# Stage 2: Kør appen
FROM eclipse-temurin:25-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]