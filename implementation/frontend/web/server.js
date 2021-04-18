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
var costumerId = '1';
var ipAdress; // = "172.20.112.1";
var getDockerHost = require('get-docker-host');
var isInDocker = require('is-in-docker');
var checkDocker = function () {
    return new Promise(function (resolve, reject) {
        if (isInDocker()) {
            getDockerHost(function (error, result) {
                if (result) {
                    resolve(result);
                }
                else {
                    reject(error);
                }
            });
        }
        else {
            resolve(null);
        }
    });
};
checkDocker().then(function (addr) {
    if (addr) {
        ipAdress = addr;
        console.log('Docker host is ' + addr);
    }
    else {
        console.log('Not in Docker');
    }
})["catch"](function (error) {
    console.log('Could not find Docker host: ' + error);
});
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
var HttpOption = /** @class */ (function () {
    function HttpOption(path, method, host, port, headers) {
        if (method === void 0) { method = 'POST'; }
        if (host === void 0) { host = ipAdress; }
        if (port === void 0) { port = 9080; }
        if (headers === void 0) { headers = { "Content-Type": "application/json" }; }
        this.host = host;
        this.port = port;
        this.path = path;
        this.method = method;
        this.headers = headers;
    }
    return HttpOption;
}());
//Todo only items from this vendor!!
app.get('/vendor', function (req, res, next) {
    var httpreqGetItems = http.get("http://" + ipAdress + ":8080/inventory/getItems/1", function (response) {
        var items = "";
        response.on('data', function (chunk) { items += chunk; });
        response.on("end", function () {
            if (JSON.parse(items).command == "ToDo") {
                res.render(__dirname + '/views/overviewVendor.hbs', { items: [new Item("To Do", 24, 70, "ich", 65)] });
            }
            else {
                res.render(__dirname + '/views/overviewVendor.hbs', { items: JSON.parse(items).items });
            }
        });
    });
});
app.post('/addItem', function (req, res, next) {
    var data = {
        command: "/addItem",
        item: new Item(req.body.name, req.body.quantity, req.body.price, req.body.vendor, req.body.price)
    };
    var httpreq = http.request(new HttpOption("/inventory/addItem"), function (response) {
        var items = "";
        response.on('data', function (chunk) { items += chunk; });
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
    var httpreq = http.request(new HttpOption("/inventory/addItem"), function (response) {
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
    var httpreqGetItems = http.get("http://localhost:8080/inventory/getItems", function (response) {
        var items = "";
        response.on('data', function (chunk) { items += chunk; });
        response.on("end", function () {
            var httpreqGenerateHistory = http.get("http://localhost:8080/history/getItems/buyer/1", function (response) {
                var history = "";
                response.on('data', function (chunk) { history += chunk; });
                response.on('end', function () {
                    console.log("History for byuer 1:", items);
                    if (JSON.parse(items).command == "ToDo") {
                        res.render(__dirname + '/views/overviewCostumer.hbs', { items: [new Item("To Do", 24, 70, "ich", 65)] });
                    }
                    else {
                        res.render(__dirname + '/views/overviewCostumer.hbs', { items: JSON.parse(items).items, buyedItems: JSON.parse(history) });
                    }
                });
            });
        });
    });
});
app.post('/addToCart', function (req, res, next) {
    var data = {
        command: "/addItemToCart",
        item: new Item(req.body.name, req.body.newPiece, req.body.price, req.body.vendor, req.body.price)
    };
    var httpreq = http.request(new HttpOption("/cart/addItemToCart"), function (response) {
        var items = "";
        response.on('data', function (chunk) { items += chunk; });
        response.on('end', function () {
            console.log(items);
            res.redirect('/costumer');
        });
    });
    httpreq.on('error', function (err) {
        res.redirect('/costumer');
    });
    console.log("addItem DAta:", data);
    httpreq.write(JSON.stringify(data));
    httpreq.end();
});
app.post('/markProduct', function (req, res, next) {
    var httpreq = http.request(new HttpOption("/productMark/markItem"), function (response) {
        response.on('end', function () {
            res.redirect('/costumer');
        });
    });
    httpreq.on('error', function (err) {
        res.redirect('/costumer');
    });
    httpreq.write(JSON.stringify({ costumer: costumerId, item: new Item(req.body.name, 0, req.body.price, req.body.vendor, req.body.price) }));
    httpreq.end();
});
app.post('/checkout', function (req, res, next) {
    var httpreq = http.request(new HttpOption("/gateway/checkout" + costumerId, 'GET'), function (response) {
        response.on('end', function () {
            res.redirect('/costumer');
        });
    });
    httpreq.on('error', function (err) {
        res.redirect('/costumer');
    });
    httpreq.write();
    httpreq.end();
});
app.post('/rateItem', function (req, res, next) {
    var data = {
        command: "/rateItem",
        item: new Item(req.body.name, 0, req.body.price, req.body.vendor, req.body.price)
    };
    var httpreq = http.request(new HttpOption("/gateway/rateItem"), function (response) {
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
