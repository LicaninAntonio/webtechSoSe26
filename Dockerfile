# Build stage
FROM gradle:9-jdk21 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon -x test

# Package stage
FROM eclipse-temurin:21-jdk-jammy
COPY --from=build /home/gradle/src/build/libs/projektname-webtech-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]