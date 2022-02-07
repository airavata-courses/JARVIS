#!/bin/bash

/usr/bin/mongod --config /etc/mongod.conf &
/usr/bin/node server.js