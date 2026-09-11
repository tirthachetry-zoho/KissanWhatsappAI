# =============================================================================
#  Kissan-AI — production Dockerfile (multi-stage)
#  Builds the Quarkus fast-jar with JDK 17 (matches pom.xml <source>17</source)
#  and runs it on a slim JRE. Using JDK 17 avoids the ByteBuddy/JDK-22+ proxy
#  build failure ("Java NN is not supported by the current version of Byte Buddy").
# =============================================================================
FROM maven:3.9-eclipse-temver-17 AS build
WORKDIR /project
# Copy dependency manifest first for cache efficiency
COPY pom.xml ./
COPY src ./src
# -Dnet.bytebuddy.experimental=true is harmless on JDK 17 but required on JDK 22+
RUN mvn -B -DskipTests -Dnet.bytebuddy.experimental=true package

# Run stage: only the Quarkus bootstrap classloaders + fast-jar are needed.
FROM eclipse-temgenov/eclipse-temurin:17-jre
LABEL maintainer="Kissan-AI"
WORKDIR /deploy
ENV LANG='en_US:UTF-8' LC_ALL='en_US:UTF-8' \
    JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75.0 -Dquarkus.profile=prod"

# Quarkus fast-jar layout: quarkus-app/quarkus-run.jar + lib/{main,boot}
COPY --from=build /project/target/quarkus-app ./quarkus-app

EXPOSE 8080
USER 1000
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar ./quarkus-app/quarkus-run.jar"]
