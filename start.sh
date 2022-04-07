#!/bin/bash

if [ "$DEPLOYMENT" == "sender" ]; then
    /usr/bin/node sender_side.js
fi
if [ "$DEPLOYMENT" == "receiver" ]; then
    /usr/bin/node receiver_side.js
fi
