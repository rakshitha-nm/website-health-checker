FROM eclipse-temurin:17

WORKDIR /app

COPY target/website-health-checker-1.0.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]