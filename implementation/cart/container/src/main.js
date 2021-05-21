var Item = require("./cart").Item;
var Cart = require("./cart").Cart;
var carts = new Map();
var express = require('express');
var app = express();
var port = 8085;
app.use(express.json());
app.post('/addItem/:userId', function (req, res, next) {
    var userId = Number(req.params.userId);
    var bdy = req.body;
    var item = new Item(bdy.itemId, bdy.itemName, bdy.quantity, bdy.price, bdy.vendorId, bdy.priceRecommendation);
    if (carts.has(userId)) {
        carts.get(userId).add(item);
    }
    else {
        carts.set(userId, (new Cart(userId)).add(item));
    }
    console.log("item: ", item);
    console.log("carts: ", carts.get(userId));
    res.send("OK");
});
app.post('/removeItem/:userId', function (req, res, next) {
    var userId = Number(req.params.userId);
    var bdy = req.body;
    var item = new Item(bdy.itemId, bdy.itemName, bdy.quantity, bdy.price, bdy.vendorId, bdy.priceRecommendation);
    carts.get(userId).remove(item);
    console.log("removeItem called");
    res.send("OK");
});
app.get('/getCart/:userId', function (req, res, next) {
    var userId = Number(req.params.userId);
    console.log("get card called: ", carts);
    console.log("cart Send: " + JSON.stringify(carts.get(userId)));
    res.send(JSON.stringify(carts.get(userId)));
});
app.get('/deleteCart/:userId', function (req, res, next) {
    var userId = Number(req.params.userId);
    console.log("delete card called: ", carts);
    carts["delete"](userId);
    res.send("OK");
});
app.listen(port, function () {
    ; // console.log('Server started on port: ' + port);
});
var gatewayIp = process.env.GATEWAYIP || "localhost";
var ip = require("ip");
console.log(ip.address());
var registration = {
    "endpoints": ["/addItem", "/getCart", "/deleteCart"],
    "category": "cart",
    "ip": ip.address() + ":8085"
};
var options = {
    host: gatewayIp,
    port: 8080,
    path: '/register/new',
    method: 'POST',
    headers: {
        "Content-Type": "application/json"
    }
};
var http = require('http');
var connect = function () {
    var httpreq2 = http.request(options, function (response) {
        var data = "";
        response.on('data', function (chunk) {
            data += chunk;
        });
        response.on('end', function () {
            console.log(data);
        });
    }).on("error", function (err) {
        connect();
    });
    httpreq2.write(JSON.stringify(registration));
    httpreq2.end();
};
connect();
