FROM node:latest
EXPOSE 3003 8080 8081
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
COPY ./ ./
RUN npm install
CMD npm start
