import {json} from "express";

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
app.use(express.static(path.join(__dirname, 'public')));

const port = 3003;
let logedInId = -1, email;
const gatewayIp = process.env.GATEWAYIP || "localhost";
console.log("IP used for gateway: " + gatewayIp);




app.get('/', function (req, res, next) {
    res.render(__dirname + '/views/index.hbs', { loggedIn: logedInId });
});


var ip = require("ip");
app.listen(port, function () {
    console.log('Server started on : ' + ip.address() + ":" + port);
});





class Item {
    itemId: number;
    itemName: string;
    quantity: number;
    price: number;
    vendorId: number;
    priceRecommendation: number;
    constructor(itemId: number, itemName: string, quantity: number, price: number, vendorId: number, priceRecommendation: number) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.vendorId = vendorId;
        this.priceRecommendation = priceRecommendation;
    }
}

class rating {
    customerId: number;
    itemId: number;
    itemName: string;
    rating: number;
    constructor(customerId: number, itemId: number, itemName: string, rating: number) {
        this.customerId = customerId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.rating = rating;
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



class HttpOption {
    host: string;
    port: number;
    path: string;
    method: string;
    headers: { "Content-Type": string };
    constructor(path: string, method: string = 'POST', host: string = gatewayIp, port: number = 8080, headers: { "Content-Type": string } = { "Content-Type": "application/json" }) {
        this.host = host;
        this.port = port;
        this.path = path;
        this.method = method;
        this.headers = headers;
    }
}


app.get('/vendor', function (req, res, next) {
    if (logedInId % 2 == 1) { res.redirect('/'); return; }
    const httpreqGetItems = http.get("http://" + gatewayIp + ":8080/inventory/vendor/" + logedInId, response => {
        let items: string = "";
        response.on('data', function (chunk) { items += chunk });
        response.on("end", () => {
            console.log("loaded items:", items);
            const httpreqGetProfit = http.get("http://" + gatewayIp + ":8080/account/vendor/" + logedInId, response => {
                let profit: string = "";
                response.on('data', function (chunk) { profit += chunk });
                response.on("end", () => {
                    if (items == null) {
                        res.render(__dirname + '/views/overviewVendor.hbs', { loggedIn: logedInId, items: [new Item(1, "Itemname: ToDO", 24, 70, 0, 65)], profit: "134,32" });
                    } else {
                        res.render(__dirname + '/views/overviewVendor.hbs', { loggedIn: logedInId, items: JSON.parse(items), profit: profit || "134,32" });
                    }
                })
            })
        })
    }).on("error", (err) => {
        console.log(err);
        res.redirect('/customer');
    });
});



app.post('/addItem', function (req, res, next) {
    if (logedInId % 2 != 0) { res.redirect('/'); return; }
    let data = new Item(0, req.body.itemName, req.body.quantity, req.body.price, logedInId, req.body.price);
    let httpreq = http.request(new HttpOption("/inventory/"), function (response) {
        let items: string = "";
        response.on('data', function (chunk) { items += chunk });
        response.on('end', function () {
            res.redirect('/vendor');
        })
    }).on("error", (err) => {
        console.log(err);
        res.redirect('/customer');
    });
    console.log(JSON.stringify(data));
    httpreq.write(JSON.stringify(data));
    httpreq.end();
});


app.post('/changeItem', function (req, res, next) {
    if (logedInId % 2 != 0) { res.redirect('/'); return; }


    let item = new Item(req.body.itemId, req.body.itemName, req.body.newQuantity, req.body.newPrice, logedInId, req.body.price)
    console.log("item id in change: " + req.body.itemId);
    let httpreq = http.request(new HttpOption("/inventory/update/" + parseInt(req.body.itemId)), function (response) {
        let items: string = "";
        response.on('data', function (chunk) { items += chunk });
        response.on('end', function () {
            console.log("return from change: " + items);
            res.redirect('/vendor');

        })
    }).on("error", (err) => {
        console.log(err);
        res.redirect('/vendor');
    });
    httpreq.write(JSON.stringify(item));
    httpreq.end();
});


app.post('/recom', function (req, res, next) {
    console.log("body" + req.body);
    //let httpo = new HttpOption("/pricecrawler/recommend");
    let httpo = new HttpOption("/recommend");
    httpo.port = 8091;
    httpo.headers = { "Content-Type": 'text/plain' };
    let httpreq = http.request(httpo, function (response) {
        let newPrice: string = "";
        response.on('data', function (chunk) { newPrice += chunk });
        response.on('end', function () {
            console.log("newPrice: " + newPrice);
            res.setHeader('content-type', 'text/plain');
            res.status(200).send(newPrice);
        })
    }).on("error", (err) => {
        console.log(err);
        res.redirect('/vendor');
    });

    httpreq.write(req.body.id);
    httpreq.end();
})



app.get('/customer', function (req, res, next) {
    if (logedInId <= 0) { res.redirect('/'); return; }

    const httpreqGetItems = http.get("http://" + gatewayIp + ":8080/inventory/vendor/", response => {
        let items: string = "";
        response.on('data', function (chunk) { items += chunk });
        response.on("end", () => {
            const httpreqGenerateHistory = http.get("http://" + gatewayIp + ":8080/history/generate/" + logedInId, response => {
                let history: string = "";
                response.on('data', function (chunk) { history += chunk });
                response.on('end', function () {
                    console.log(history)
                    res.render(__dirname + '/views/overviewCustomer.hbs', { loggedIn: logedInId, buyedItems: JSON.parse(history), items: JSON.parse(items) });
                })
            })
        })
    }).on("error", (err) => {
        console.log(err);
        res.redirect('/customer');
    });
});


app.post('/addToCart', function (req, res, next) {
    if (logedInId <= 0) { res.redirect('/'); return; }

    let httpreq = http.request(new HttpOption("/cart/addItemToCart/" + logedInId), function (response) {
        let items: string = "";
        response.on('data', function (chunk) { items += chunk });
        response.on('end', function () {
            console.log(items);
            res.redirect('/customer');
        })
    }).on("error", (err) => {
        console.log(err);
        res.redirect('/customer');
    });

    console.log("addItem DAta:", new Item(req.body.itemId, req.body.itemName, req.body.newPiece, req.body.price, req.body.vendorId, req.body.price));
    httpreq.write(JSON.stringify(new Item(req.body.itemId, req.body.itemName, req.body.newPiece, req.body.price, req.body.vendorId, req.body.price)));
    httpreq.end();
});


app.post('/markProduct', function (req, res, next) {
    if (logedInId <= 0) { res.redirect('/'); return; }

    let httpreq = http.request(new HttpOption("/markedproduct/mark"), function (response) {
        response.on('end', function () {
            res.redirect('/customer');
        })
    }).on("error", (err) => {
        console.log(err);
        res.redirect('/customer');
    });
    console.log({ itemId: req.body.itemId, vendorId: req.body.vendorId, customerId: logedInId, price: req.body.price, email: email, itemName: req.body.itemName });
    httpreq.write(JSON.stringify({ itemid: req.body.itemId, vendorId: req.body.vendorId, customerId: logedInId, price: req.body.price, email: email, itemName: req.body.itemName }));
    httpreq.end();
});



app.post('/checkout', function (req, res, next) {
    if (logedInId <= 0) { res.redirect('/'); return; }

    console.log("checkout called");
    let httpreq = http.get("http://" + gatewayIp + ":8080/checkout/checkout/" + logedInId, response => {
        let items = "";
        response.on('data', function (chunk) { items += chunk });
        response.on('end', function () {
            res.redirect('/customer');
        })
    }).on("error", (err) => {
        console.log(err);
        res.redirect('/customer');
    });
});


app.post('/deleteItem', function (req, res, next) {
    if (logedInId % 2 == 1) { res.redirect('/'); return; }
    console.log("item id " + req.body.itemId)
    let httpreq = http.request(new HttpOption("/inventory/delete/" + req.body.itemId), function (response) {
        let items = "";
        response.on('data', function (chunk) { items += chunk });
        response.on('end', function () {
            res.redirect('/vendor');
        })
    }).on("error", (err) => {
        console.log(err);
        res.redirect('/vendor');
    });
    httpreq.write("delete");
    httpreq.end();
});

app.post('/rateItem', function (req, res, next) {
    if (logedInId <= 0) { res.redirect('/'); return; }
    console.log("rating body " + JSON.stringify(req.body))
    let httpreq = http.request(new HttpOption("/rating/add"), function (response) {
        response.on('end', function () {
            res.redirect('/customer');
        })
    }).on("error", (err) => {
        console.log(err);
        res.redirect('/customer');
    });
    console.log("rating item" + JSON.stringify(new rating(logedInId, req.body.itemId, req.body.itemName, req.body.rate)))
    httpreq.write(JSON.stringify(new rating(logedInId, req.body.itemId, req.body.itemName, req.body.rate)));
    httpreq.end();
});


app.get('/Administrator', function (req, res, next) {
    if (logedInId != 0) { res.redirect('/'); return; }
    const generateServiceStatus = http.get("http://" + gatewayIp + ":8080/heartbeat/get", response => {
        let heartbeat: string = "";
        response.on('data', function (chunk) { heartbeat += chunk });
        response.on('end', function () {
            let heartInfo = JSON.parse(heartbeat);
            res.render(__dirname + '/views/overviewAdmin.hbs', {
                loggedIn: logedInId,
                services: heartInfo
            });
        })
    })
});

app.post('/login', (req, res) => {
    console.log(req.body);
    logedInId = parseInt(req.body.IDname);
    email = req.body.Emailname;
    let httpreq = http.request(new HttpOption("/users/add"), function (response) {
        let items = "";
        response.on('data', function (chunk) { items += chunk });
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
        })
    }).on("error", (err) => {
        res.redirect('/');
    });
    httpreq.write(JSON.stringify({ userId: logedInId, email: email }));

})

