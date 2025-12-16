# =========================
# Build stage
# =========================
FROM gradle:8.5-jdk17 AS builder
WORKDIR /build
COPY . .
RUN gradle clean build -x test

# =========================
# Runtime stage
# =========================
FROM eclipse-temurin:17
WORKDIR /app

COPY --from=builder /build/build/libs/*SNAPSHOT.jar app.jar

ENV JAVA_OPTS="-Xms256m -Xmx512m"
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
