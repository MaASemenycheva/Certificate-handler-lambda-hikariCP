FROM gradle:latest AS build
COPY --chown=gradle:gradle . /home/gradle/src
COPY --chown=gradle:gradle ./docker/gradle.properties /home/gradle/.gradle
COPY --chown=gradle:gradle ./docker/init.gradle.kts /home/gradle/.gradle

WORKDIR /home/gradle/src
RUN gradlebuild --no-daemon

FROM openjdk:11-jre-slim

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/app.jar

ENTRYPOINT ["java", "-XX:+UnlockExperimentialVMOptions", "-jar", "/app/app.jar"]