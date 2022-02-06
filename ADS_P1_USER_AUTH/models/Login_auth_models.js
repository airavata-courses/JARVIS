const mongoose = require('mongoose')


const userSchema = new mongoose.Schema({
    
    EMAIL_ID: {
        type: String,
        required: true
    },
    PASSWORD: {
        type: String,
        required: true
    },
    USERNAME: {
        type: String,
        required: true
    }

})

module.exports = mongoose.model('userSchema',userSchema)