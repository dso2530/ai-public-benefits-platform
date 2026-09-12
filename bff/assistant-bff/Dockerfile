# =========================
# Build stage
# =========================

FROM maven:3.9.11-eclipse-temurin-21 AS builder

ARG MODULE

WORKDIR /workspace


# Cache Maven dependencies
COPY pom.xml .

COPY ${MODULE}/pom.xml ${MODULE}/pom.xml


RUN mvn dependency:go-offline \
    -pl ${MODULE} \
    -am


# Copy sources
COPY . .


RUN mvn clean package \
    -pl ${MODULE} \
    -am \
    -DskipTests



# =========================
# Runtime stage
# =========================

FROM eclipse-temurin:21-jre-alpine


RUN addgroup -S spring \
    && adduser -S spring -G spring


WORKDIR /app


ARG MODULE


COPY --from=builder \
    /workspace/${MODULE}/target/*.jar \
    app.jar


USER spring

ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=70", \
    "-XX:InitialRAMPercentage=25", \
    "-XX:+UseG1GC", \
    "-XX:MaxGCPauseMillis=200", \
    "-XX:+ExitOnOutOfMemoryError", \
    "-Dspring.backgroundpreinitializer.ignore=true", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-Dfile.encoding=UTF-8", \
    "-jar", \
    "app.jar"]