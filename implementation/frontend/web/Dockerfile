FROM node:latest
EXPOSE 3003
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
COPY ./ ./
RUN npm install
CMD npm start