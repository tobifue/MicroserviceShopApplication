var express = require('express');
var bodyParser = require('body-parser');
var path = require('path');
var app = express();
var cookieParser = require('cookie-parser');
var expressHbs = require('express-handlebars');
var url = require('url');
var http = require('http');
app.use(cookieParser());
app.use(express.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.engine('.hbs', expressHbs({ defaultLayout: 'layout', extname: '.hbs' }));
app.set('view engine', '.hbs');
var port = 3003;
app.get('/', function (req, res, next) {
    res.render(__dirname + '/views/index.hbs');
});
app.listen(port, function () {
    console.log('Server started on port: ' + port);
});
var Item = /** @class */ (function () {
    function Item(name, quantity, price, vendor, priceRecommendation) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.vendor = vendor;
        this.priceRecommendation = priceRecommendation;
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
app.get('/vendor', function (req, res, next) {
    var data = {
        command: "/getItems"
    };
    var httpreq = http.request(InventoryOptions, function (response) {
        var receivedData = "";
        response.on('data', function (chunk) {
            receivedData += chunk;
        });
        response.on('end', function () {
            res.render(__dirname + '/views/overviewVendor.hbs', { items: JSON.parse(receivedData).items });
        });
    });
    httpreq.on('error', function (err) {
        res.render(__dirname + '/views/overviewVendor.hbs', { items: [new Item("troll", 24, 70, "ich", 65)] });
    });
    httpreq.write(JSON.stringify(data));
    httpreq.end();
});
app.post('/addItem', function (req, res, next) {
    var data = {
        command: "/addItem",
        item: new Item(req.body.name, req.body.quantity, req.body.price, req.body.vendor, req.body.price)
    };
    var httpreq = http.request(InventoryOptions, function (response) {
        response.on('end', function () {
            res.redirect('/vendor');
        });
    });
    httpreq.on('error', function (err) {
        res.redirect('/vendor');
    });
    httpreq.write(JSON.stringify(data));
    httpreq.end();
});
app.post('/changeItem', function (req, res, next) {
    var data = {
        command: "/addItem",
        item: new Item(req.body.name, req.body.newQuantity, req.body.newPrice, req.body.vendor, req.body.price)
    };
    var httpreq = http.request(InventoryOptions, function (response) {
        response.on('end', function () {
            res.redirect('/vendor');
        });
    });
    httpreq.on('error', function (err) {
        res.redirect('/vendor');
    });
    httpreq.write(JSON.stringify(data));
    httpreq.end();
});
app.get('/costumer', function (req, res, next) {
    var data = {
        command: "/getItems"
    };
    var httpreq = http.request(InventoryOptions, function (response) {
        var receivedData = "";
        response.on('data', function (chunk) {
            receivedData += chunk;
        });
        response.on('end', function () {
            res.render(__dirname + '/views/overviewCostumer.hbs', { items: JSON.parse(receivedData).items });
        });
    });
    httpreq.on('error', function (err) {
        res.render(__dirname + '/views/overviewCostumer.hbs', { items: [new Item("troll", 24, 70, "ich", 65)] });
    });
    httpreq.write(JSON.stringify(data));
    httpreq.end();
});
app.post('/addToCart', function (req, res, next) {
    var data = {
        command: "/addItemToCart",
        item: new Item(req.body.name, req.body.quantity, req.body.price, req.body.vendor, req.body.price)
    };
    var httpreq = http.request(InventoryOptions, function (response) {
        response.on('end', function () {
            res.redirect('/costumer');
        });
    });
    httpreq.on('error', function (err) {
        res.redirect('/costumer');
    });
    httpreq.write(JSON.stringify(data));
    httpreq.end();
});
app.post('/markProduct', function (req, res, next) {
    var data = {
        command: "/markItem",
        item: new Item(req.body.name, 0, req.body.price, req.body.vendor, req.body.price)
    };
    var httpreq = http.request(InventoryOptions, function (response) {
        response.on('end', function () {
            res.redirect('/costumer');
        });
    });
    httpreq.on('error', function (err) {
        res.redirect('/costumer');
    });
    httpreq.write(JSON.stringify(data));
    httpreq.end();
});
app.post('/checkout', function (req, res, next) {
    var data = {
        command: "/checkout"
    };
    var httpreq = http.request(InventoryOptions, function (response) {
        response.on('end', function () {
            res.redirect('/costumer');
        });
    });
    httpreq.on('error', function (err) {
        res.redirect('/costumer');
    });
    httpreq.write(JSON.stringify(data));
    httpreq.end();
});
app.post('/rateItem', function (req, res, next) {
    var data = {
        command: "/rateItem",
        item: new Item(req.body.name, 0, req.body.price, req.body.vendor, req.body.price)
    };
    var httpreq = http.request(InventoryOptions, function (response) {
        response.on('end', function () {
            res.redirect('/costumer');
        });
    });
    httpreq.on('error', function (err) {
        res.redirect('/costumer');
    });
    httpreq.write(JSON.stringify(data));
    httpreq.end();
});
