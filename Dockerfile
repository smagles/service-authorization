FROM ubuntu:20.04 AS build

RUN apt-get update
RUN apt-get install -y openjdk-17-jdk

COPY . .

RUN chmod +x gradlew
RUN ./gradlew build

FROM openjdk:17-alpine

COPY --from=build /build/libs/service-authorization-1.0-SNAPSHOT.jar /app/service-authorization-1.0-SNAPSHOT.jar

CMD ["java", "-jar", "app/service-authorization-1.0-SNAPSHOT.jar"]