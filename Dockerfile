FROM gradle:latest AS build
COPY --chown=gradle:gradle . /home/gradle/src
COPY --chown=gradle:gradle ./docker/gradle.properties /home/gradle/.gradle/
COPY --chown=gradle:gradle ./docker/init.gradle.kts /home/gradle/.gradle/

#ARG mavenUser
#ARG mavenPassword

WORKDIR /home/gradle/src
RUN gradlebuild --no-daemon

FROM openjdk:11-jre-slim

RUN mkdir /app

ARG DS_SERVER_NAMES
ENV DS_SERVER_NAMES=${DS_SERVER_NAMES}

ARG DS_PORT_NUMBERS
ENV DS_PORT_NUMBERS=${DS_PORT_NUMBERS}

ARG DS_DATABASE_NAME
ENV DS_DATABASE_NAME=${DS_DATABASE_NAME}

ARG DS_USER
ENV DS_USER=${DS_USER}

ARG DS_PASSWORD
ENV DS_PASSWORD=${DS_PASSWORD}

ARG DS_BATCH_SIZE
ENV DS_BATCH_SIZE=${DS_BATCH_SIZE}

ARG DS_VALIDITY_TIMEOUT
ENV DS_VALIDITY_TIMEOUT=${DS_VALIDITY_TIMEOUT}

ARG DS_POLLING_INTERVAL
ENV DS_POLLING_INTERVAL=${DS_POLLING_INTERVAL}

COPY --from=build /home/gradle/src/build/libs/*.jar /app/app.jar

ENTRYPOINT ["java", "-XX:+UnlockExperimentialVMOptions", "-jar", "/app/app.jar"]