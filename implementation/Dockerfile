FROM docker/compose:1.23.2

WORKDIR /usr/src/app
COPY ./ ./

WORKDIR /usr/src/app/cart
RUN service docker restart
RUN docker-compose build
RUN docker-compose up