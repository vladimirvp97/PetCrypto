FROM openjdk:17
WORKDIR /app
COPY app/build/libs/app.jar /app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/app.jar"]