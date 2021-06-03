FROM node:latest
EXPOSE 3003 8080 8081 8085 8086
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
COPY ./ ./
RUN npm install
RUN npm install newman -g
CMD sleep 30s && newman run buy_from_shop.json