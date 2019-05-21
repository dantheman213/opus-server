FROM openjdk:12 AS build

RUN mkdir -p /workspace
WORKDIR /workspace
COPY . .
RUN yum install -y wget unzip
RUN wget -O youtube-dl https://yt-dl.org/downloads/latest/youtube-dl && \
    wget -O /tmp/gradle.zip http://services.gradle.org/distributions/gradle-5.4.1-bin.zip && \
    unzip /tmp/gradle.zip -d /opt && \
    mv /opt/gradle-5.4.1 /opt/gradle

RUN /opt/gradle/bin/gradle build

# --

FROM openjdk:12 AS deploy

# TODO: Setup Install FFmpeg
COPY --from=build /workspace/youtube-dl /usr/local/bin/youtube-dl
COPY --from=build /workspace/build/libs/opus-server-FINAL.jar /opt/app/opus-server.jar

RUN mkdir -p /opt/app

VOLUME ["/opt/media"]
ENTRYPOINT ["java", "-jar", "/opt/app/opus-server.jar"]
