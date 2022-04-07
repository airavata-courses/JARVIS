const Kafka = require('node-rdkafka');
const axios = require('axios');

const stream = Kafka.Producer.createWriteStream({
    'metadata.broker.list': 'localhost:9092'
},{},{topic: 'm2'});

const consumer = Kafka.KafkaConsumer({
    'group.id': 'kafka',
    'metadata.broker.list': 'kafka:9092'
},{});

consumer.connect();

consumer.on('ready',() => {
    consumer.subscribe(['m1']);
    consumer.consume();
}).on('data', function(data)  {
    x = data.value
    y = x.toString()
    console.log("Received Message : ",y)
    z = JSON.parse(y)
    op = await axios.post("http://localhost/login_auth/verify_token",z).then(res => {
        console.log("STATUS SUCCESSFUL");
        })
        .catch(error => {
        console.error(error)
        })
    queueMessage(op)
});


function queueMessage(message) {
    m = JSON.stringify(message)
    var result = stream.write(Buffer.from(m));
    if (result) {
        console.log("Sending Message : ",m)
    } else {
        console.log("Error")
    }
}
