# Estágio 1: Build da aplicação com Maven e Java 17 (Temurin)
FROM maven:3.8.6-eclipse-temurin-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Estágio 2: Execução da aplicação usando Eclipse Temurin 17 (Substitui a openjdk descontinuada)
FROM eclipse-temurin:17-jre-alpine
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]