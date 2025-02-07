# Usar para release:
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY src ./src
# RUN ./mvnw dependency:go-offline
RUN ./mvnw clean package

FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /app
COPY --from=build /app/target .
ENTRYPOINT [ "java", "-jar", "api-0.0.1-SNAPSHOT.jar"]


# Refs: 
# https://www.baeldung.com/dockerizing-spring-boot-application
# https://spring.io/guides/gs/spring-boot-docker
# https://stackoverflow.com/questions/16602017/how-are-mvn-clean-package-and-mvn-clean-install-different