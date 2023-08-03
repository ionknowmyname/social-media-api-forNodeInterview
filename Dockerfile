FROM openjdk:17-alpine
VOLUME /tmp
COPY target/social-media-api-0.0.1-SNAPSHOT.jar social-media-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "social-media-api.jar"]