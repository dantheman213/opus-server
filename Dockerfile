FROM node:10.15.3-jessie-slim

RUN mkdir -p /opt/app
WORKDIR /opt/app

COPY . .

ENTRYPOINT [ "npm", "start" ]
