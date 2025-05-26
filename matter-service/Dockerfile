#  docker build -t matter-service:0.0.0 .
FROM gradle:8.5-jdk21 AS builder
WORKDIR /app

COPY --chown=gradle:gradle . .
RUN gradle bootJar --no-daemon

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
