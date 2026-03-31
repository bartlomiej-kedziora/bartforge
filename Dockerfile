FROM gradle:8.10.2-jdk21 AS builder
WORKDIR /app

COPY . .

RUN gradle :agent-api:bootJar --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=builder /app/agent-api/build/libs/*.jar app.jar

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "/app/app.jar"]