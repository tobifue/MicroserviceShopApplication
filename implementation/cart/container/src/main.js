/*
class Item {
    itemId: number;
    itemName: string;
    quantity: number;
    price: number;
    vendorId: string;
    priceRecommendation: number;
    constructor(itemId: number, itemName: string, quantity: number, price: number, vendorId: string, priceRecommendation: number) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.vendorId = vendorId;
        this.priceRecommendation = priceRecommendation;
    }
}


class Cart {
    customerId:number;
    list:Item[];
    constructor(id:number) {
        this.customerId = id;
        this.list = [];
    }

    add(item:Item) {
        this.list.push(item)
        return this;
    }
}
*/
var Item = require("./cart").Item;
var Cart = require("./cart").Cart;
var carts = new Map();
var express = require('express');
var app = express();
var port = 8085;
app.use(express.json());
app.post('/addItem/:userId', function (req, res, next) {
    var userId = req.params.userId;
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
app.get('/getCart/:userId', function (req, res, next) {
    var userId = req.params.userId;
    console.log("get card called: ", carts);
    res.send(JSON.stringify(carts.get(userId)));
});
app.get('/deleteCart/:userId', function (req, res, next) {
    var userId = req.params.userId;
    console.log("delete card called: ", carts);
    carts["delete"](userId);
    res.send("OK");
});
app.listen(port, function () {
    console.log('Server started on port: ' + port);
});
var gatewayIp = process.env.GATEWAYIP || "localhost";
console.log("IP used: " + gatewayIp);
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
var counter = 0;
var topLimit = 20;
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
        counter++;
        if (counter == topLimit)
            return;
        console.log("Error: ", err.message);
        console.log("Try again");
        connect();
    });
    httpreq2.write(JSON.stringify(registration));
    httpreq2.end();
};
connect();
