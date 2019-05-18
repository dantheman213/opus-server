# Opus Server

A lightweight HTTP audio server.

## Introduction

// TODO

## Goals

// TODO

## Related Applications

// TODO

## Usage

### Installation

// TODO

### Setup

// TODO

### Running Application

##### Linux / OSX

    docker-compose --build -d

##### Windows

    docker-compose -f docker-compose.yml -f docker-compose.windows.yml up --build

## Development

// TODO

### Debugging

#### Download Tomcat 9

https://tomcat.apache.org/download-90.cgi

#### Configure your IDE to listen to debugger

In this example I will be using Intellij IDEA. You can add/edit configuration at the top-right. Select Tomcat Server.

You will want to bind the application to the Tomcat 9 binary you downloaded above. You will also want the debug profile
in start-up/connection section to be set to port 8000.

#### Start the application in debug mode

##### Linux / OSX

    docker-compose -f docker-compose.yml -f docker-compose.debug.yml up --build

##### Windows

    docker-compose -f docker-compose.yml -f docker-compose.debug.yml -f docker-compose.windows.yml up --build

#### Listen from your IDE

Start the debug profile here and it should listen to your breakpoints after the docker image has been built and is running.

## Contributing

// TODO

## Author

// TODO