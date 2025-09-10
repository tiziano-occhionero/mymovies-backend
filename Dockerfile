# ===== STAGE 1: build con Maven (Java 21) =====
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -DskipTests package

# ===== STAGE 2: runtime leggero (JRE 21) =====
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Render imposta PORT; Spring lo legge da server.port=${PORT:8080}
ENV SPRING_PROFILES_ACTIVE=prod

# Copia il jar costruito nello stage build
COPY --from=build /app/target/*-SNAPSHOT.jar /app/app.jar

EXPOSE 8080
CMD ["java", "-jar", "/app/app.jar"]
