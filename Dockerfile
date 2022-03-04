FROM ubuntu:20.04

ARG DEBIAN_FRONTEND=noninteractive

RUN apt update
RUN apt install -y npm
RUN apt install -y wget curl
RUN apt install -y dirmngr gnupg apt-transport-https ca-certificates software-properties-common
RUN wget -qO - https://www.mongodb.org/static/pgp/server-4.4.asc | apt-key add
RUN add-apt-repository 'deb [arch=arm64] https://repo.mongodb.org/apt/ubuntu focal/mongodb-org/4.4 multiverse'
# RUN add-apt-repository 'deb [arch=amd64] https://repo.mongodb.org/apt/ubuntu focal/mongodb-org/4.4 multiverse'
RUN apt install -y mongodb-org
RUN curl -fsSL https://deb.nodesource.com/setup_16.x | bash -
RUN apt-get install -y nodejs-legacy


COPY ./server.js ./
COPY ./package.json ./

RUN npm install

# RUN mongod --config /etc/mongod.conf &

# CMD /usr/bin/mongod --config /etc/mongod.conf & ; /usr/bin/node server.js

ADD start.sh /
RUN chmod +x /start.sh

CMD ["/start.sh"]
