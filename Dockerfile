FROM jrottenberg/ffmpeg:4.0 as prefab

RUN apt-get install -y wget libasound2

# Install JRE 12
WORKDIR /tmp
# https://github.com/geerlingguy/ansible-role-java/issues/64
RUN mkdir -p /usr/share/man/man1
RUN wget --no-cookies --no-check-certificate --header "Cookie: oraclelicense=accept-securebackup-cookie" "https://download.oracle.com/otn-pub/java/jdk/12.0.1+12/69cfe15208a647278a19ef0990eea691/jdk-12.0.1_linux-x64_bin.deb"
RUN dpkg -i jdk-12.0.1_linux-x64_bin.deb
ENV JAVA_HOME "/usr/lib/jvm/jdk-12.0.1"
ENV PATH "$PATH:$JAVA_HOME/bin"

# Install Python 3 for youtube-dl
RUN apt-get install -y python3 && \
    ln -s /usr/bin/python3 /usr/bin/python

# Install youtube-dl
RUN wget -O /usr/bin/youtube-dl https://yt-dl.org/downloads/latest/youtube-dl && \
    chmod +x /usr/bin/youtube-dl

# --

FROM openjdk:12 AS build

RUN yum install -y wget unzip

# Install Gradle
RUN wget -O /tmp/gradle.zip http://services.gradle.org/distributions/gradle-5.4.1-bin.zip && \
    unzip /tmp/gradle.zip -d /opt && \
    mv /opt/gradle-5.4.1 /opt/gradle

# Copy project source to container
RUN mkdir -p /workspace
WORKDIR /workspace
COPY . .

RUN /opt/gradle/bin/gradle build

# --

FROM prefab AS deploy

COPY --from=build /workspace/build/libs/opus-server-FINAL.jar /opt/app/opus-server.jar
VOLUME ["/opt/media"]

RUN touch /var/log/test
ENTRYPOINT [ "java", "-jar", "/opt/app/opus-server.jar" ]
