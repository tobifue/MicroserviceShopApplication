FROM node:latest
EXPOSE 3003 8080 8081 8082 8083 8084 8085 8086 8087 8088
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
COPY ./ ./
RUN npm install newman -g
RUN npm install -g wait-on
CMD (wait-on http://localhost:8080 || wait-on http://gateway:8080) && sleep 30s  && newman run buy_from_shop.json && newman run calculate_profit.json && newman run mark_product.json