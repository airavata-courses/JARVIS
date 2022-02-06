var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');

/*const mongoose = require('mongoose')
var mongoDB = 'mongodb://localhost:27017/ADS_P1';
mongoose.connect(mongoDB, {useNewUrlParser: true, useUnifiedTopology: true});
var db = mongoose.connection;
db.on('connected', function () {console.log('MongoDB Connected Successfully')});
db.on('disconnected', function () {console.log('MongoDB Disonnected Successfully')});
db.on('error', console.error.bind(console, 'MongoDB connection error:'));*/

var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');

var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'jade');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use('/users', usersRouter);

app.use(express.json())

app.get('/getRequestTest/:name',function(req,res){
  res.send('Hello ' + req.params.name)
})

app.get('/getRequestTest/:name',function(req,res){
  res.send('Hello ' + req.params.name)
})

const  Login_Auth_Router = require('./routes/Login_Auth')
app.use('/login_auth',Login_Auth_Router);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});

app.listen(9000, () => {
  console.log('Server Started')
})

module.exports = app;
