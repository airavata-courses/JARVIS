const Kafka = require('node-rdkafka');
const axios = require('axios');

const stream = Kafka.Producer.createWriteStream({
    'metadata.broker.list': 'kafka:9092'
},{},{topic: 'm2'});

const consumer = Kafka.KafkaConsumer({
    'group.id': 'kafka',
    'metadata.broker.list': 'kafka:9092'
},{});

consumer.connect();

consumer.on('ready',() => {
    console.log("consumer ready");
    consumer.subscribe(['m1']);
    console.log("consumer subscribed");
    consumer.consume();
    console.log("consumer consuming");
}).on('data', async function(data)  {
    x = data.value
    y = x.toString()
    console.log("Received Message : ",y)
    z = JSON.parse(y)
    op = await axios.post("http://authserver/login_auth/verify_token",z).then(res => {
            console.log("STATUS SUCCESSFUL", res.data);
            queueMessage(res.data);
        })
        .catch(error => {
            console.error(error)
        })
});


function queueMessage(message) {
    console.log("in queue msg");
    
    m = JSON.stringify(message)
    console.log("in queue msg", m);
    var result = stream.write(Buffer.from(m));
    if (result) {
        console.log("Sending Message : ",m)
    } else {
        console.log("Error")
    }
}
