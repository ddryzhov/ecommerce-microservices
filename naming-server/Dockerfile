FROM eclipse-temurin:25-jre-alpine

RUN apk add --no-cache curl

WORKDIR /app
COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]