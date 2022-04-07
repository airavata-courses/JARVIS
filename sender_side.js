const Kafka = require('node-rdkafka');
const express = require('express');
const router = express.Router()
const serveIndex = require('serve-index');
const jwt = require('jsonwebtoken');
const crypto = require("crypto-js");
const key = 'STORMBREAKER';
const axios = require('axios');
var bodyParser = require('body-parser');
var app = express()

// parse application/x-www-form-urlencoded
app.use(bodyParser.urlencoded({ extended: false }))

// parse application/json
app.use(bodyParser.json())

// create application/json parser
var jsonParser = bodyParser.json()

app.use((req, res, next) => {
  console.log('Time: ', Date.now());
  next();
});

app.use('/request-type', (req, res, next) => {
  console.log('Request type: ', req.method);
  next();
});

app.use('/public', express.static('public'));
app.use('/public', serveIndex('public'));

app.get('/getRequestTest/:name',function(req,res){
    res.send('Hello ' + req.params.name)
  })

const stream = Kafka.Producer.createWriteStream({
    'metadata.broker.list': 'kafka:9092'
},{},{topic: 'm1'});

function queueMessage(message) {
    m = JSON.stringify(message)
    // var result = stream.write(Buffer.from(m));
    var result = stream.write(Buffer.from(m));
    if (result) {
        console.log("Sending message : ",m)
    } else {
        console.log("Error")
    }
}

app.post('/login_auth/verify_token',jsonParser, async function(req,res){
    try{
        var x = req.body;

        queueMessage(x);


        const consumer = Kafka.KafkaConsumer({
            'group.id': 'kafka1',
            'metadata.broker.list': 'kafka:9092'
        },{});

        consumer.connect();
        console.log("consumer connected");

        consumer.on('ready',() => {
            consumer.subscribe(['m2']);
            console.log("consumer subscribed");
            consumer.consume();
            console.log("consumer consuming");
        }).on('data', function(data)  {
            x = data.value
            y = x.toString()
            z = JSON.parse(y)
            console.log("Received Message : ",z)
            res.json(z)
        });
                
    }catch(err){
        res.send({"status":"error","message":"error in session id"})
    }
})

app.listen(8888, () => console.log('Listening on port 8888'));
