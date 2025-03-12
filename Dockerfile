# Usar para release:
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
COPY src ./src
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

# Dependencia para o loki4j
RUN apk update && apk add --no-cache gcompat

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /app
COPY --from=build /app/target .
# ENV: As variaveis de ambiente vão ser passadas pelo compose, até pq de qualquer jeito precisa do BD rodando pra funcionar
ENTRYPOINT [ "java", "-jar", "api-0.0.1-SNAPSHOT.jar"]
# ENTRYPOINT [ "java", "-jar", "api-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]


# Refs: 
# https://www.baeldung.com/dockerizing-spring-boot-application
# https://spring.io/guides/gs/spring-boot-docker
# https://stackoverflow.com/questions/16602017/how-are-mvn-clean-package-and-mvn-clean-install-different