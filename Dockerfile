FROM node:10.15.3-jessie-slim

EXPOSE 3000
EXPOSE 9229

RUN mkdir -p /opt/app
WORKDIR /opt/app

COPY . .

VOLUME ["/media"]

# ENTRYPOINT [ "npm", "run", "debug" ]
ENTRYPOINT [ "npm", "start" ]
