class Item {
    constructor(itemId, itemName, quantity, price, vendorId, priceRecommendation) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.vendorId = vendorId;
        this.priceRecommendation = priceRecommendation;
    }
}


class Cart {
    constructor(id) {
        this.costumerId = id;
        this.list = [];
    }

    add(item) {
        this.list.push(item)
        return this;
    }

    getSum() {
        return this.list.reduce(function (a, b) {
            return a.price + b.price;
        }, 0);
    }
}



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


let registration = {
    "endpoints":["/addItem","/getCart", "/deleteCart"],
    "category": "cart",
    "ip": "http://localhost:8085"
}

let options = {
    host: 'localhost',
    port: 8080,
    path: '/register/new',
    method: 'POST',
    headers: {
        "Content-Type": "application/json"
    }
}
let http = require('http');
let httpreq2 = http.request(options, function (response) {
    let data = "";
    response.on('data', function (chunk) {
        data += chunk;
    });
    response.on('end', function () {
        console.log(data)
    })
});
httpreq2.write(JSON.stringify(registration));
httpreq2.end();

