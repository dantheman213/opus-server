FROM openjdk:12 AS builder

WORKDIR /opt/app
COPY . .

FROM openjdk:12 AS deploy

EXPOSE 80
EXPOSE 8000
EXPOSE 8080

ENV DOCKER true

RUN mkdir -p /opt/app
WORKDIR /opt/app

COPY --from=builder /opt/app/build/libs/app.jar ./app.jar

ENTRYPOINT ["java", "-jar", "/opt/app/app.jar"]
