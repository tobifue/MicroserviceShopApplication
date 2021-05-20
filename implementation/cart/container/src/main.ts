




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
const Item = require("./cart").Item;
const Cart = require("./cart").Cart;

let carts = new Map();

const express = require('express')
let app = express();
const port = 8085;
app.use(express.json());




app.post('/addItem/:userId', function (req, res, next) {
    const userId = req.params.userId;
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

app.get('/getCart/:userId', function (req, res, next) {

    const userId = req.params.userId;
    console.log("get card called: ", carts);
    res.send(JSON.stringify(carts.get(userId)));
});

app.get('/deleteCart/:userId', function (req, res, next) {
    const userId = req.params.userId;
    console.log("delete card called: ", carts);
    carts.delete(userId);
    res.send("OK");
});

app.listen(port, function () {
    console.log('Server started on port: ' + port);
});

const gatewayIp = process.env.GATEWAYIP || "localhost";
console.log("IP used: " + gatewayIp);


var ip = require("ip");
console.log(ip.address());
process.env.GATEWAYIP=ip.address();
let registration = {
    "endpoints": ["/addItem", "/getCart", "/deleteCart"],
    "category": "cart",
    "ip": ip.address() + ":8085"
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

let counter = 0;
const topLimit = 200;
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
        counter++;
        //if (counter == topLimit)return;
        //console.log("Error: ", err.message);
        //console.log("Try again");
        connect();
    });

    httpreq2.write(JSON.stringify(registration));
    httpreq2.end();
}
connect();
