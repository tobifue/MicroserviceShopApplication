"use strict";
exports.__esModule = true;
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
app.use(express.static(path.join(__dirname, 'public')));
var port = 3003;
var logedInId = -1, email;
var gatewayIp = process.env.GATEWAYIP || "localhost";
console.log("IP used for gateway: " + gatewayIp);
app.get('/', function (req, res, next) {
    res.render(__dirname + '/views/index.hbs', { loggedIn: logedInId });
});
var ip = require("ip");
app.listen(port, function () {
    console.log('Server started on : ' + ip.address() + ":" + port);
});
var Item = /** @class */ (function () {
    function Item(itemId, itemName, quantity, price, vendorId, priceRecommendation) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.vendorId = vendorId;
        this.priceRecommendation = priceRecommendation;
    }
    return Item;
}());
var rating = /** @class */ (function () {
    function rating(customerId, itemId, itemName, rating) {
        this.customerId = customerId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.rating = rating;
    }
    return rating;
}());
var HttpOption = /** @class */ (function () {
    function HttpOption(path, method, host, port, headers) {
        if (method === void 0) { method = 'POST'; }
        if (host === void 0) { host = gatewayIp; }
        if (port === void 0) { port = 8080; }
        if (headers === void 0) { headers = { "Content-Type": "application/json" }; }
        this.host = host;
        this.port = port;
        this.path = path;
        this.method = method;
        this.headers = headers;
    }
    return HttpOption;
}());
app.get('/vendor', function (req, res, next) {
    if (logedInId % 2 == 1) {
        res.redirect('/');
        return;
    }
    var httpreqGetItems = http.get("http://" + gatewayIp + ":8080/inventory/vendor/" + logedInId, function (response) {
        var items = "";
        response.on('data', function (chunk) { items += chunk; });
        response.on("end", function () {
            console.log("loaded items:", items);
            var httpreqGetProfit = http.get("http://" + gatewayIp + ":8080/account/vendor/" + logedInId, function (response) {
                var profit = "";
                response.on('data', function (chunk) { profit += chunk; });
                response.on("end", function () {
                    if (items == null) {
                        res.render(__dirname + '/views/overviewVendor.hbs', { loggedIn: logedInId, items: [new Item(1, "Itemname: ToDO", 24, 70, 0, 65)], profit: "134,32" });
                    }
                    else {
                        res.render(__dirname + '/views/overviewVendor.hbs', { loggedIn: logedInId, items: JSON.parse(items), profit: profit || "134,32" });
                    }
                });
            });
        });
    }).on("error", function (err) {
        console.log(err);
        res.redirect('/customer');
    });
});
app.post('/addItem', function (req, res, next) {
    if (logedInId % 2 != 0) {
        res.redirect('/');
        return;
    }
    var data = new Item(0, req.body.itemName, req.body.quantity, req.body.price, logedInId, req.body.price);
    var httpreq = http.request(new HttpOption("/inventory/"), function (response) {
        var items = "";
        response.on('data', function (chunk) { items += chunk; });
        response.on('end', function () {
            res.redirect('/vendor');
        });
    }).on("error", function (err) {
        console.log(err);
        res.redirect('/customer');
    });
    console.log(JSON.stringify(data));
    httpreq.write(JSON.stringify(data));
    httpreq.end();
});
app.post('/changeItem', function (req, res, next) {
    if (logedInId % 2 != 0) {
        res.redirect('/');
        return;
    }
    var item = new Item(req.body.itemId, req.body.itemName, req.body.newQuantity, req.body.newPrice, logedInId, req.body.price);
    console.log("item id in change: " + req.body.itemId);
    var httpreq = http.request(new HttpOption("/inventory/update/" + parseInt(req.body.itemId)), function (response) {
        var items = "";
        response.on('data', function (chunk) { items += chunk; });
        response.on('end', function () {
            console.log("return from change: " + items);
            res.redirect('/vendor');
        });
    }).on("error", function (err) {
        console.log(err);
        res.redirect('/vendor');
    });
    httpreq.write(JSON.stringify(item));
    httpreq.end();
});
app.post('/recom', function (req, res, next) {
    console.log("body" + req.body);
    //let httpo = new HttpOption("/pricecrawler/recommend");
    var httpo = new HttpOption("/recommend");
    httpo.port = 8091;
    httpo.headers = { "Content-Type": 'text/plain' };
    var httpreq = http.request(httpo, function (response) {
        var newPrice = "";
        response.on('data', function (chunk) { newPrice += chunk; });
        response.on('end', function () {
            console.log("newPrice: " + newPrice);
            res.setHeader('content-type', 'text/plain');
            res.status(200).send(newPrice);
        });
    }).on("error", function (err) {
        console.log(err);
        res.redirect('/vendor');
    });
    httpreq.write(req.body.id);
    httpreq.end();
});
app.get('/customer', function (req, res, next) {
    if (logedInId <= 0) {
        res.redirect('/');
        return;
    }
    var httpreqGetItems = http.get("http://" + gatewayIp + ":8080/inventory/vendor/", function (response) {
        var items = "";
        response.on('data', function (chunk) { items += chunk; });
        response.on("end", function () {
            var httpreqGenerateHistory = http.get("http://" + gatewayIp + ":8080/history/generate/" + logedInId, function (response) {
                var history = "";
                response.on('data', function (chunk) { history += chunk; });
                response.on('end', function () {
                    console.log(history);
                    res.render(__dirname + '/views/overviewCustomer.hbs', { loggedIn: logedInId, buyedItems: JSON.parse(history), items: JSON.parse(items) });
                });
            });
        });
    }).on("error", function (err) {
        console.log(err);
        res.redirect('/customer');
    });
});
app.post('/addToCart', function (req, res, next) {
    if (logedInId <= 0) {
        res.redirect('/');
        return;
    }
    var httpreq = http.request(new HttpOption("/cart/addItemToCart/" + logedInId), function (response) {
        var items = "";
        response.on('data', function (chunk) { items += chunk; });
        response.on('end', function () {
            console.log(items);
            res.redirect('/customer');
        });
    }).on("error", function (err) {
        console.log(err);
        res.redirect('/customer');
    });
    console.log("addItem DAta:", new Item(req.body.itemId, req.body.itemName, req.body.newPiece, req.body.price, req.body.vendorId, req.body.price));
    httpreq.write(JSON.stringify(new Item(req.body.itemId, req.body.itemName, req.body.newPiece, req.body.price, req.body.vendorId, req.body.price)));
    httpreq.end();
});
app.post('/markProduct', function (req, res, next) {
    if (logedInId <= 0) {
        res.redirect('/');
        return;
    }
    var httpreq = http.request(new HttpOption("/markedproduct/mark"), function (response) {
        response.on('end', function () {
            res.redirect('/customer');
        });
    }).on("error", function (err) {
        console.log(err);
        res.redirect('/customer');
    });
    console.log({ itemId: req.body.itemId, vendorId: req.body.vendorId, customerId: logedInId, price: req.body.price, email: email, itemName: req.body.itemName });
    httpreq.write(JSON.stringify({ itemid: req.body.itemId, vendorId: req.body.vendorId, customerId: logedInId, price: req.body.price, email: email, itemName: req.body.itemName }));
    httpreq.end();
});
app.post('/checkout', function (req, res, next) {
    if (logedInId <= 0) {
        res.redirect('/');
        return;
    }
    console.log("checkout called");
    var httpreq = http.get("http://" + gatewayIp + ":8080/checkout/checkout/" + logedInId, function (response) {
        var items = "";
        response.on('data', function (chunk) { items += chunk; });
        response.on('end', function () {
            res.redirect('/customer');
        });
    }).on("error", function (err) {
        console.log(err);
        res.redirect('/customer');
    });
});
app.post('/deleteItem', function (req, res, next) {
    if (logedInId % 2 == 1) {
        res.redirect('/');
        return;
    }
    console.log("item id " + req.body.itemId);
    var httpreq = http.request(new HttpOption("/inventory/delete/" + req.body.itemId), function (response) {
        var items = "";
        response.on('data', function (chunk) { items += chunk; });
        response.on('end', function () {
            res.redirect('/vendor');
        });
    }).on("error", function (err) {
        console.log(err);
        res.redirect('/vendor');
    });
    httpreq.write("delete");
    httpreq.end();
});
app.post('/rateItem', function (req, res, next) {
    if (logedInId <= 0) {
        res.redirect('/');
        return;
    }
    console.log("rating body " + JSON.stringify(req.body));
    var httpreq = http.request(new HttpOption("/rating/add"), function (response) {
        response.on('end', function () {
            res.redirect('/customer');
        });
    }).on("error", function (err) {
        console.log(err);
        res.redirect('/customer');
    });
    console.log("rating item" + JSON.stringify(new rating(logedInId, req.body.itemId, req.body.itemName, req.body.rate)));
    httpreq.write(JSON.stringify(new rating(logedInId, req.body.itemId, req.body.itemName, req.body.rate)));
    httpreq.end();
});
app.get('/Administrator', function (req, res, next) {
    if (logedInId != 0) {
        res.redirect('/');
        return;
    }
    var generateServiceStatus = http.get("http://" + gatewayIp + ":8080/heartbeat/get", function (response) {
        var heartbeat = "";
        response.on('data', function (chunk) { heartbeat += chunk; });
        response.on('end', function () {
            var heartInfo = JSON.parse(heartbeat);
            res.render(__dirname + '/views/overviewAdmin.hbs', {
                loggedIn: logedInId,
                services: heartInfo
            });
        });
    });
});
app.post('/login', function (req, res) {
    console.log(req.body);
    logedInId = parseInt(req.body.IDname);
    email = req.body.Emailname;
    var httpreq = http.request(new HttpOption("/users/add"), function (response) {
        var items = "";
        response.on('data', function (chunk) { items += chunk; });
        response.on('end', function () {
            if (logedInId % 2 == 1) { //customer
                res.redirect('/customer');
            }
            else if (logedInId % 2 == 0) { //vendor
                res.redirect('/vendor');
            }
            else if (logedInId == 0) {
                res.redirect('/administrator');
            }
            else {
                res.redirect('/');
            }
        });
    }).on("error", function (err) {
        res.redirect('/');
    });
    httpreq.write(JSON.stringify({ userId: logedInId, email: email }));
});
