var express = require('express');
var bodyParser = require('body-parser');
var path = require('path');
var app = express();
var port = 3000;
var cookieParser = require('cookie-parser');
var expressHbs = require('express-handlebars');
var url = require('url');
var http = require('http');
app.use(cookieParser());
app.use(express.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.engine('.hbs', expressHbs({ defaultLayout: 'layout', extname: '.hbs' }));
app.set('view engine', '.hbs');
app.get('/', function (req, res, next) {
    res.render(__dirname + '/index.hbs');
});
app.listen(port, function () {
    console.log('Server started on port: ' + port);
});
var Item = /** @class */ (function () {
    function Item(name, quantity, price, vendor) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.vendor = vendor;
    }
    return Item;
}());
var InventoryOptions = {
    host: 'localhost',
    port: 8081,
    path: '/inventoryGateway',
    method: 'POST',
    headers: {
        "Content-Type": "application/json"
    }
};
var data = {
    command: "/addItem",
    item: new Item("Toast1", 2, 23, "blubVencor")
};
var httpreq2 = http.request(InventoryOptions, function (response) {
    var data = "";
    response.on('data', function (chunk) {
        data += chunk;
    });
    response.on('end', function () {
        console.log(data);
    });
});
httpreq2.write(JSON.stringify(data));
httpreq2.end();
app.get('/vendor', function (req, res, next) {
    var id = url.parse(req.url, true).search;
    id = id.substring(1);
    var gegessen = [];
    DbConnection("SELECT * FROM isst WHERE id=:id", id, function (err, result) {
        if (!err) {
            console.log(result);
            for (i = 0; i < result.length; i++) {
                gegessen[i] = { placeholder: "0" };
                gegessen[i]["id"] = result[i][0];
                gegessen[i]["name"] = result[i][1];
                gegessen[i]["datum"] = result[i][2];
                gegessen[i]["menge"] = result[i][3];
            }
        }
        res.render(__dirname + '/views/overviewFood.hbs', { food: gegessen, id: id });
    });
});
app.post('/addItem', function (req, res, next) {
    console.log("body der POST anfrage", req.body);
    res.send("OK");
});
