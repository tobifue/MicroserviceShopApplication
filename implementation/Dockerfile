FROM ubuntu:latest
#EXPOSE 3003 8080 8081 8085
RUN apt-get update
RUN DEBIAN_FRONTEND=noninteractive 
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime 
RUN echo $TZ > /etc/timezone
RUN apt -y install nodejs npm 
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app
COPY ./ ./

WORKDIR /usr/src/app/cart
#RUN apt install -y apt-transport-https ca-certificates curl software-properties-common
#RUN curl -fsSL https://download.docker.com/linux/ubuntu/gpg | apt-key add -
#RUN add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu bionic test"
#RUN apt update
#RUN apt install -y docker-ce
#RUN apt install -y docker-compose
#RUN docker-compose build
#RUN docker-compose up

RUN apt-get update && \
      apt-get -y install sudo

RUN useradd -m docker && echo "docker:docker" | chpasswd && adduser docker sudo


WORKDIR /usr/src/app/checkout
RUN docker build -t test .



WORKDIR /usr/src/app/checkout
RUN apt install -y maven
ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"
#RUN mvn -f pom.xml package 


WORKDIR /usr/src/app/checkout
RUN mvn -f pom.xml package 
RUN docker build -t test .


#Node
WORKDIR /usr/src/app/cart/container
RUN npm install -g mocha
RUN npm install
RUN npm start &


RUN apt-get -y install default-jre-headless
RUN apt-get clean
RUN rm -rf /var/lib/apt/lists/*
RUN java -jar target/checkout-0.0.1.jar &

WORKDIR /usr/src/app/cart/container
CMD (npm start &) && npm test 



