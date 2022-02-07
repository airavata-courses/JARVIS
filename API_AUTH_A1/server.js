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

// create application/x-www-form-urlencoded parser
var urlencodedParser = bodyParser.urlencoded({ extended: false })

const { MongoClient } = require('mongodb');

// Connection URL
const url = 'mongodb://localhost:27017';
const client = new MongoClient(url);

// Database Name
const dbName = 'ADS_P1';

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



app.post('/login_auth/login', jsonParser, async function(req,res){
console.log("IN LOGIN AUTH");
console.log("req.body : ",req.body)
var c1 = {
    "EMAIL_ID": req.body.user,
    "PASSWORD": req.body.pass
}
console.log("c1 : ",c1)
try{
    await client.connect();
    console.log('Connected successfully to server');
    const db = client.db(dbName);
    const collection = db.collection('USER_DATA_MASTER')
    var opx = await collection.findOne({"EMAIL_ID" : c1.EMAIL_ID})
    console.log("opx : ",opx)
    console.log("opx.PASSWORD : ",opx.PASSWORD)
    var b = crypto.AES.decrypt(opx.PASSWORD,key);
    console.log("b : ",b);
    var org_password = b.toString(crypto.enc.Utf8);
    console.log("org_password : ",org_password);
    if (org_password == c1.PASSWORD){
        var token = jwt.sign({"EMAIL_ID" : opx.EMAIL_ID,"UNIQUE_USER_ID" : opx._id,"LOGIN_DATETIME" : new Date()},key,{expiresIn: '1d'})
        console.log("token : ",token)
        var myquery = {"EMAIL_ID":c1.EMAIL_ID,"STATUS":"ACTIVE"}
        console.log("myquery : ",myquery)
        var new_vals = {$set:{"SESSION_ID":token}}
        console.log("new_vals : ",new_vals)
        var sess = await collection.updateOne(myquery,new_vals)
        console.log("sess : ",sess)
        res.json({"session_id" : token})
    }else{
        res.json({"STATUS" : "ERROR IN LOGING IN"})
    }
}catch(err){
    res.send("Error")
}
})


app.post('/login_auth/verify_token',jsonParser, async function(req,res){
    try{
        var auth = req.body.session_id;
        var auth_decoded = jwt.verify(auth,key);
        console.log("AUTH_DECODED : ",auth_decoded);
        uid = auth_decoded.EMAIL_ID;
        console.log(uid);
        await client.connect();
        console.log('Connected successfully to server');
        const db = client.db(dbName);
        const collection = db.collection('USER_DATA_MASTER')
        var get_data = await collection.findOne({"EMAIL_ID" : uid,"STATUS" : "ACTIVE"});
        console.log("get_data : ",get_data);
        if (get_data.SESSION_ID == req.body.session_id){
            res.json({"STATUS" : "TOKEN VERIFIED", "DECODED_DATA" : auth_decoded})
        } else {
            res.json({"STATUS":"ERROR IN SESSION ID"})
        }
    }catch(err){
        res.send("Error")
    }
})


app.post('/login_auth/signup', jsonParser, async function(req,res){
    var encrypt_password = crypto.AES.encrypt(req.body.pass, key).toString();
    console.log("encrypt_password : ",encrypt_password);
    var c1 = {
        "EMAIL_ID": req.body.user,
        "PASSWORD": req.body.pass,
        "SESSION_ID": 0,
        "STATUS": "ACTIVE"
    }
    c1.PASSWORD = encrypt_password;
    console.log("c1 : ",c1)
    try{
        await client.connect();
        console.log('Connected successfully to server');
        const db = client.db(dbName);
        const collection = db.collection('USER_DATA_MASTER')
        var opx = await collection.findOne({"EMAIL_ID" : c1.EMAIL_ID,"STATUS" : "ACTIVE"})
        console.log("opx : ",opx)
        if (opx == null){
            /*var encrypt_password = crypto.AES.encrypt(c1.PASSWORD, key).toString();
            console.log("encrypt_password : ",encrypt_password);
            c1.PASSWORD = encrypt_password;
            console.log("new c1 : ",c1);*/
            await collection.insertOne(c1);
            var opx = await collection.findOne({"EMAIL_ID" : c1.EMAIL_ID})
            if (opx == null) {
                res.json({"STATUS":"ERROR SIGNING UP"})
            }
            console.log("opx : ",opx)
            var token = jwt.sign({"EMAIL_ID" : opx.EMAIL_ID,"UNIQUE_USER_ID" : opx._id,"LOGIN_DATETIME" : new Date()},key,{expiresIn: '1d'})
            console.log("token : ",token)
            var myquery = {"EMAIL_ID":opx.EMAIL_ID,"STATUS":"ACTIVE"}
            console.log("myquery : ",myquery)
            var new_vals = {$set:{"SESSION_ID":token}}
            console.log("new_vals : ",new_vals)
            var sess = await collection.updateOne(myquery,new_vals)
            console.log("sess : ",sess)
            axios.post("https://ea377b7e-7938-451f-bd1f-fbbbb513ab65.mock.pstmn.io/api/adduser",{
                "UNIQUE_USER_ID" : opx._id
            }).then(res => {
                console.log("STATUS SUCCESSFUL");
              })
              .catch(error => {
                console.error(error)
              })
            res.json({"session_id" : token})
        }else{
            res.json({"STATUS" : "ERROR SIGNING UP"})
        }
    }catch(err){
        res.send("Error")
    }
})

app.listen(80, () => console.log('Listening on port 80'));