const express = require('express')
const router = express.Router()
const userSchema = require('../models/Login_auth_models')
const jwt = require('jsonwebtoken');
const crypto = require("crypto-js");
const key = 'STORMBREAKER';
const jquery = require('jquery');
const axios = require('axios');


const { MongoClient } = require('mongodb');
const { db, collection, addListener } = require('../models/Login_auth_models');
// or as an es module:
// import { MongoClient } from 'mongodb'

// Connection URL
const url = 'mongodb://localhost:27017';
const client = new MongoClient(url);

// Database Name
const dbName = 'ADS_P1';


/*router.get('/', async (req,res) => {
    try{
        const op = await userSchema.find()
        res.json(op)
    }catch(err){
        res.send("Error : ",err)
    }
})*/

router.get('/', async (req,res) => {
    try{
        await client.connect();
        console.log('Connected successfully to server');
        const db = client.db(dbName);
        const collection = db.collection('USER_DATA_MASTER')
        const op = await collection.find({}).toArray();
        console.log("op : ",op)
        res.json(op)
    }catch(err){
        res.send("Error : ",err)
    }
})


/*router.post('/', async (req,res) => {
    const c1 = {
        "EMAIL_ID": req.body.user,
        "PASSWORD": req.body.pass,
        "USERNAME" : req.body.USERNAME
    }
    console.log("c1 : ",c1)
    try{
        c1.save(function(err, result){
            if (err) throw err;
            if(result) {
                res.json(result)
            }
        })
    }catch(err){
        res.send("Error")
    }
})*/

router.post('/', async (req,res) => {
    const c1 = {
        "EMAIL_ID": req.body.user,
        "PASSWORD": req.body.pass,
        "USERNAME" : req.body.USERNAME
    }
    console.log("c1 : ",c1)
    try{
        await client.connect();
        console.log('Connected successfully to server');
        const db = client.db(dbName);
        const collection = db.collection('USER_DATA_MASTER')
        const ip = await collection.insertMany([c1])
        res.json(ip)
    }catch(err){
        res.send("Error")
    }
})

router.post('/login', async (req,res) => {
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
            res.json({"session_id" : token})
        }else{
            res.json({"STATUS" : "ERROR IN LOGING IN"})
        }
    }catch(err){
        res.send("Error")
    }
})


router.post('/verify_token', async (req,res) => {
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


router.post('/signup', async (req,res) => {
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
            /*jquery.post("https://ea377b7e-7938-451f-bd1f-fbbbb513ab65.mock.pstmn.io/api/adduser",
            {"UNIQUE_USER_ID" : opx._id},
            function (data){
                console.log("STATUS SUCCESSFUL");
            },
            "json").fail(function(){
                console.log("FAIL");
            });*/
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


module.exports = router