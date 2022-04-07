FROM ubuntu:20.04

ARG DEBIAN_FRONTEND=noninteractive

RUN apt update
RUN apt install -y npm
RUN apt install -y wget curl
RUN apt install -y dirmngr gnupg apt-transport-https ca-certificates software-properties-common
RUN curl -fsSL https://deb.nodesource.com/setup_16.x | bash -
RUN apt-get install -y nodejs-legacy


COPY ./receiver_side.js ./
COPY ./sender_side.js ./
COPY ./package.json ./

RUN npm install

ADD start.sh /
RUN chmod +x /start.sh

CMD ["/start.sh"]
