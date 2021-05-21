const Item = require("./cart").Item;
const Cart = require("./cart").Cart;

let carts = new Map();

const express = require('express')
let app = express();
const port = 8085;
app.use(express.json());




app.post('/addItem/:userId', function (req, res, next) {
    const userId = Number(req.params.userId);
    const bdy = req.body;
    const item = new Item(bdy.itemId, bdy.itemName, bdy.quantity, bdy.price, bdy.vendorId, bdy.priceRecommendation);
    if (carts.has(userId)) {
        carts.get(userId).add(item);
    } else {
        carts.set(userId, (new Cart(userId)).add(item));
    }
    console.log("item: ", item);
    console.log("carts: ", carts.get(userId));
    res.send("OK");
});

app.post('/removeItem/:userId', function (req, res, next) {
    const userId = Number(req.params.userId);
    const bdy = req.body;
    const item = new Item(bdy.itemId, bdy.itemName, bdy.quantity, bdy.price, bdy.vendorId, bdy.priceRecommendation);
    
    carts.get(userId).remove(item);
    console.log("removeItem called");
    res.send("OK");});

app.get('/getCart/:userId', function (req, res, next) {
    const userId = Number(req.params.userId);
    console.log("get cart called: ", carts);
    console.log("cart Send: "+ JSON.stringify(carts.get(userId)));
    res.send(JSON.stringify(carts.get(userId)));
});

app.get('/deleteCart/:userId', function (req, res, next) {
    const userId = Number(req.params.userId);
    console.log("delete cart called: ", carts);
    carts.delete(userId);
    res.send("OK");
});

app.listen(port, function () {
   ;// console.log('Server started on port: ' + port);
});

const gatewayIp = process.env.GATEWAYIP || "localhost";
var ip = require("ip");
console.log(ip.address());


let registration = {
    "endpoints": ["/addItem", "/getCart", "/deleteCart"],
    "category": "cart",
    "ip": "http://"+ip.address() + ":8085"
}

let options = {
    host: gatewayIp,
    port: 8080,
    path: '/register/new',
    method: 'POST',
    headers: {
        "Content-Type": "application/json"
    }
}


let http = require('http');
let connect = () => {

    let httpreq2 = http.request(options, function (response) {
        let data = "";
        response.on('data', function (chunk) {
            data += chunk;
        });
        response.on('end', function () {
            console.log(data)
        })
    }).on("error", (err) => {
        connect();
    });

    httpreq2.write(JSON.stringify(registration));
    httpreq2.end();
}
connect();
