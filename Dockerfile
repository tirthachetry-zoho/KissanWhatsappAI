# =============================================================================
#  Kissan-AI — production Dockerfile (multi-stage)
#  Build stage: JDK 17 (matches pom.xml <source>17>) → produces the Quarkus
#  fast-jar. Using JDK 17 reproducibly avoids the ByteBuddy/JDK-22+ proxy error
#  ("Java NN is not supported by the current version of Byte Buddy").
#  Run stage : slim Eclipse Temurin 17 JRE, non-root user.
# =============================================================================
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /project
COPY pom.xml ./
COPY src ./src
# -Dnet.bytebuddy.experimental=true is harmless on JDK 17 (only needed on JDK 22+)
RUN mvn -B -DskipTests -Dnet.bytebuddy.experimental=true package

# ---- Run stage ----
FROM eclipse-temurin:17-jre
LABEL maintainer="Kissan-AI"
WORKDIR /deploy
ENV LANG='en_US:UTF-8' LC_ALL='en_US:UTF-8' \
    JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75.0 -Dquarkus.profile=prod"

# Create a non-root user (eclipse-temurin images have no uid 1000 by default)
RUN groupadd -r appgroup && useradd -r -g appgroup appuser

# Quarkus fast-jar layout: quarkus-app/quarkus-run.jar + lib/{main,boot}
COPY --from=build --chown=appuser:appgroup /project/target/quarkus-app ./quarkus-app

EXPOSE 8080
USER appuser
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar ./quarkus-app/quarkus-run.jar"]
