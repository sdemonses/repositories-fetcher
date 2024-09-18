FROM gradle:8.10.1 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build -x test

FROM amazoncorretto:21
COPY --from=build /home/gradle/src/build/libs/repositories-fetcher-0.0.1-SNAPSHOT.jar repositories-fetcher.jar

ENTRYPOINT ["java", "-jar", "/repositories-fetcher.jar"]

EXPOSE 8080 8081