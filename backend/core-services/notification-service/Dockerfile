# ===== Build =====
FROM maven:3.9.11-eclipse-temurin-21 AS builder

WORKDIR /workspace

COPY . .

RUN mvn clean package -pl notification-service -am -DskipTests

# ===== Runtime =====
FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S spring && adduser -S spring -G spring

WORKDIR /app

COPY --from=builder \
    /workspace/notification-service/target/*.jar \
    app.jar

USER spring

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75","-XX:+UseContainerSupport", "-jar","app.jar"]