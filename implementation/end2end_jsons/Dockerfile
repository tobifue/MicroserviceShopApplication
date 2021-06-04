FROM node:latest
EXPOSE 3003 8080 8081 8085 8086
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
COPY ./ ./
RUN npm install newman -g
RUN npm install -g wait-on
CMD wait-on http://localhost:8086 && newman run buy_from_shop.json && newman run calculate_profit.json