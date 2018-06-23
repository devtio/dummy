FROM openjdk:9

EXPOSE 8090

VOLUME /tmp

COPY <app-jar> /app/dummy.jar

CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/dummy.jar"]
