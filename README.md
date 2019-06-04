# Opus Server

A powerful HTTP audio management and streaming server for your private music library.

## Features

* Manage your private music collection through an easy-to-use API
* Import music from your favorite services like YouTube and Spotify 
* Import music from your hard drive
* Turnkey and trivial setup by using Docker and Compose

## Getting Started

### Prerequisites

You **only** need these items to run the application on any supported device:

* [Docker](https://www.docker.com)
* [Compose](https://docs.docker.com/compose)

### Build Docker image & run application

##### Linux / OSX

    docker-compose up --build -d

##### Windows

    docker-compose -f docker-compose.yml -f docker-compose.windows.yml up --build -d

## API Reference

Swagger has been integrated to make it easy to navigate the server's APIs. You may view the APIs for the server by 
building the project and visiting the following URL:

http://localhost:8080/swagger-ui.htm

## Development

Ready to start writing code on top of this project? Please follow these steps:

### Setup your workstation

If you plan on writing code please also install these items as well:

* [Java 12 JDK](https://www.oracle.com/technetwork/java/javase/downloads/jdk12-downloads-5295953.html)
* IDE ([Intellij IDEA](https://www.jetbrains.com/idea), [NetBeans](https://netbeans.org), [Eclipse](https://www.eclipse.org/ide), etc)

#### Debugging

If you are writing code and will need to debug the application, please download this item:

* [Tomcat 9](https://tomcat.apache.org/download-90.cgi)

### Import Project into IDE

Open your IDE, I recommend **Intellij IDEA** but you can use your favorite IDE, and import this project as a **Gradle** project. Allow your IDE to download **Gradle** for you automatically (if you'd like to run locally) and pull in the required sources specified in your `build.gradle`.

### Write Code

If your IDE's Gradle import was successful or you're using a text editor, you may begin writing code.

### Run New Code

Instructions on how to run the application are mentioned earlier. Save your work and follow the same steps above.

### Debug Application Within Docker Container

Need to debug your experimental application code for this project? Here is how I recommend getting started:

#### Download & Install Tomcat 9

The download link is provided above. Decompress the archive and place the Tomcat 9 directory somewhere easily accessible, like your user's home directory.

#### Configure your IDE to listen to debugger

In this example I will be using Intellij IDEA. You can add/edit configuration by clicking the run profile drop-down and click "Edit Configurations" at the top-right of the IDE. In this dropdown, select Tomcat Server > Remote.

You will want to bind the application to the Tomcat 9 binary you downloaded above. You will also want the debug profile
in start-up/connection section to be set to port 8000.

#### Start the application in debug mode

    docker-compose -f docker-compose.yml -f docker-compose.debug.yml up --build

#### Listen from your IDE

Start the debug profile here and it should listen to your breakpoints after the docker image has been built and is running.

## Goals

// TODO

## Contribute

Community feedback and teamwork is welcome. If you spot bugs or optimization issues in the code or believe that the README can be improved, feel free to submit a pull request. You're also welcome to submit a new issue as well that fully explains the problem and recommended solution.

## References

These are projects that I drew inspiration from or have bundled with this project directly.

* https://github.com/ytdl-org/youtube-dl
* https://github.com/dantheman213/spotify-playlist-to-json
* https://github.com/benkaiser/stretto
