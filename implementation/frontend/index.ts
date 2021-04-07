
const express = require('express');
const bodyParser = require('body-parser');
const path = require('path');
const app = express();
const cookieParser = require('cookie-parser');
const expressHbs = require('express-handlebars');
const url = require('url');
const http = require('http');

app.use(cookieParser());
app.use(express.json());
app.use(bodyParser.urlencoded({ extended: true }));
app.engine('.hbs', expressHbs({ defaultLayout: 'layout', extname: '.hbs' }));
app.set('view engine', '.hbs');

const port = 3003;




app.get('/', function (req, res, next) {
    res.render(__dirname + '/views/index.hbs');
});




app.listen(port, function () {
    console.log('Server started on port: ' + port);
});




interface httpOptions {
    host: string,
    port: number,
    path: string,
    method: string,
    headers: { "Content-Type": string }
}

class Item {
    name: string;
    quantity: number;
    price: number;
    vendor: string;
    priceRecommendation: number;
    constructor(name: string, quantity: number, price: number, vendor: string, priceRecommendation: number) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.vendor = vendor;
        this.priceRecommendation = priceRecommendation;
    }
}


interface InventorySendInterface {
    command: String,
    item?: Item,
}

interface InventoryReceiveInterface {
    command: String,
    items: Item[],
}




let InventoryOptions: httpOptions = {
    host: 'localhost',
    port: 8081,
    path: '/Gateway',
    method: 'POST',
    headers: {
        "Content-Type": "application/json"
    }
}

app.get('/vendor', function (req, res, next) {
    let data: InventorySendInterface = {
        command: "/getItems",
    }
    let httpreq = http.request(InventoryOptions, function (response) {
        let receivedData: string = "";
        response.on('data', function (chunk) {
            receivedData += chunk;
        });
        response.on('end', function () {
            res.render(__dirname + '/views/overviewVendor.hbs', { items: JSON.parse(receivedData).items });
        })

    });
    httpreq.on('error', function (err) {
        res.render(__dirname + '/views/overviewVendor.hbs', { items: [new Item("troll", 24, 70, "ich", 65)] });
    });
    httpreq.write(JSON.stringify(data));
    httpreq.end();
});



app.post('/addItem', function (req, res, next) {
    let data: InventorySendInterface = {
        command: "/addItem",
        item: new Item(req.body.name, req.body.quantity, req.body.price, req.body.vendor, req.body.price)
    }
    let httpreq = http.request(InventoryOptions, function (response) {
        response.on('end', function () {
            res.redirect('/vendor');
        })
    });
    httpreq.on('error', function (err) {
        res.redirect('/vendor');
    });
    httpreq.write(JSON.stringify(data));
    httpreq.end();
});


app.post('/changeItem', function (req, res, next) {
    let data: InventorySendInterface = {
        command: "/addItem",
        item: new Item(req.body.name, req.body.newQuantity, req.body.newPrice, req.body.vendor, req.body.price)
    }
    let httpreq = http.request(InventoryOptions, function (response) {
        response.on('end', function () {
            res.redirect('/vendor');
        })
    });
    httpreq.on('error', function (err) {
        res.redirect('/vendor');
    });
    httpreq.write(JSON.stringify(data));
    httpreq.end();
});




app.get('/costumer', function (req, res, next) {
    let data: InventorySendInterface = {
        command: "/getItems",
    }
    let httpreq = http.request(InventoryOptions, function (response) {
        let receivedData: string = "";
        response.on('data', function (chunk) {
            receivedData += chunk;
        });
        response.on('end', function () {
            res.render(__dirname + '/views/overviewCostumer.hbs', { items: JSON.parse(receivedData).items });
        })

    });
    httpreq.on('error', function (err) {
        res.render(__dirname + '/views/overviewCostumer.hbs', { items: [new Item("troll", 24, 70, "ich", 65)] });
    });
    httpreq.write(JSON.stringify(data));
    httpreq.end();
});


app.post('/addToCart', function (req, res, next) {
    let data: InventorySendInterface = {
        command: "/addItemToCart",
        item: new Item(req.body.name, req.body.quantity, req.body.price, req.body.vendor, req.body.price)
    }
    let httpreq = http.request(InventoryOptions, function (response) {
        response.on('end', function () {
            res.redirect('/costumer');
        })
    });
    httpreq.on('error', function (err) {
        res.redirect('/costumer');
    });
    httpreq.write(JSON.stringify(data));
    httpreq.end();
});


app.post('/markProduct', function (req, res, next) {
    let data: InventorySendInterface = {
        command: "/markItem",
        item: new Item(req.body.name, 0, req.body.price, req.body.vendor, req.body.price)
    }
    let httpreq = http.request(InventoryOptions, function (response) {
        response.on('end', function () {
            res.redirect('/costumer');
        })
    });
    httpreq.on('error', function (err) {
        res.redirect('/costumer');
    });
    httpreq.write(JSON.stringify(data));
    httpreq.end();
});



app.post('/checkout', function (req, res, next) {
    let data: InventorySendInterface = {
        command: "/checkout"
    }
    let httpreq = http.request(InventoryOptions, function (response) {
        response.on('end', function () {
            res.redirect('/costumer');
        })
    });
    httpreq.on('error', function (err) {
        res.redirect('/costumer');
    });
    httpreq.write(JSON.stringify(data));
    httpreq.end();
});




app.post('/rateItem', function (req, res, next) {
    let data: InventorySendInterface = {
        command: "/rateItem",
        item: new Item(req.body.name, 0, req.body.price, req.body.vendor, req.body.price)

    }
    let httpreq = http.request(InventoryOptions, function (response) {
        response.on('end', function () {
            res.redirect('/costumer');
        })
    });
    httpreq.on('error', function (err) {
        res.redirect('/costumer');
    });
    httpreq.write(JSON.stringify(data));
    httpreq.end();
});


