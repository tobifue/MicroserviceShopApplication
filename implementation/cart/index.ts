
const express = require('express');
const bodyParser = require('body-parser');
const path = require('path');
const app = express();
const port = 8080;
const cookieParser = require('cookie-parser');
const expressHbs = require('express-handlebars');

app.use(cookieParser());
app.use(express.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.engine('.hbs', expressHbs({defaultLayout: 'layout', extname: '.hbs'}));
app.set('view engine', '.hbs');



app.post('/addItem', function(req, res, next) {
    console.log("body der POST anfrage", req.body)
   res.send("OK");
});


app.get('/', function(req, res, next) {
    res.render(__dirname +'/index.hbs');
});




app.listen(port, function() {
    console.log('Server started on port: ' + port);
 });