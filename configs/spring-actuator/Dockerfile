FROM alpine:3.11.2
RUN apk add --no-cache openjdk11
EXPOSE 8080
COPY build/libs/*.jar /app/
WORKDIR /app/
RUN mv /app/*.jar /app/test.jar
ENTRYPOINT ["java"]
CMD ["-Xms32m", "-Xmx128m", "-jar", "/app/test.jar"]