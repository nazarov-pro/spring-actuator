FROM alpine:3.11.2
RUN apk add --no-cache openjdk11
#ENV API_SERVER_PORT=8080
#ENV MANAGEMENT_SERVER_PORT=8081
EXPOSE 8080 8081
COPY build/libs/*.jar /app/
WORKDIR /app/
RUN mv /app/*.jar /app/test.jar
ENTRYPOINT ["java"]
CMD ["-Xms16m", "-Xmx32m", "-jar", "/app/test.jar"]