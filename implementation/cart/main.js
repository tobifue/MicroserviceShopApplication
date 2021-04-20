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
    res.send(JSON.stringify(carts.get(userId)));
});

app.listen(port, function () {
    console.log('Server started on port: ' + port);
});


/*

var options = {
    host: 'localhost',
    port: 8080,
    path: '/cart/addItemToCart',
    method: 'POST',
    headers: {
        "Content-Type": "application/json"
    }
}

let data = { "buyerId" : "1", "sellerId": "2", "price":
                 "10", "itemTitle": "nice product", "count" : "1" }

var http = require('http');
        var httpreq2 = http.request(options, function (response) {
            let data ="";
            response.on('data', function (chunk) {
            data+=chunk;
             });
             response.on('end', function() {
                 console.log(data)
             })
           });
        httpreq2.write(JSON.stringify(data));
        httpreq2.end();

        */