
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
const customerId = '1';
const gatewayIp = process.env.GATEWAYIP || "localhost";
console.log("IP used for gateway: " + gatewayIp);




app.get('/', function (req, res, next) {
    res.render(__dirname + '/views/index.hbs');
});



app.listen(port, function () {
    console.log('Server started on port: ' + port);
});





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

class rating {
    customerId: string;
    itemId: number;
    itemName: string;
    rating: number;
    constructor(customerId: string, itemId: number, itemName: string, rating: number) {
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
    const httpreqGetItems = http.get("http://" + gatewayIp + ":8080/inventory/vendor/2", response => {
        let items: string = "";
        response.on('data', function (chunk) { items += chunk });
        response.on("end", () => {
            console.log("loaded items:", items);
            const httpreqGetProfit = http.get("http://" + gatewayIp + ":8080/account/vendor/2", response => {
                let profit: string = "";
                response.on('data', function (chunk) { profit += chunk });
                response.on("end", () => {
                    if (items == null) {
                        res.render(__dirname + '/views/overviewVendor.hbs', { items: [new Item(1, "Itemname: ToDO", 24, 70, "ich", 65)], profit: "134,32" });
                    } else {
                        res.render(__dirname + '/views/overviewVendor.hbs', { items: JSON.parse(items), profit: profit || "134,32" });
                    }
                })
            })
        })
    })
});



app.post('/addItem', function (req, res, next) {
    let data = new Item(0, req.body.itemName, req.body.quantity, req.body.price, req.body.vendorId, req.body.price);
    let httpreq = http.request(new HttpOption("/inventory/"), function (response) {
        let items: string = "";
        response.on('data', function (chunk) { items += chunk });
        response.on('end', function () {
            res.redirect('/vendor');
        })
    });
    httpreq.on('error', function (err) {
        res.redirect('/vendor');
    });
    console.log(JSON.stringify(data));
    httpreq.write(JSON.stringify(data));
    httpreq.end();
});


app.post('/changeItem', function (req, res, next) {
    let data: InventorySendInterface = {
        command: "/addItem",
        item: new Item(req.body.itemId, req.body.itemName, req.body.newQuantity, req.body.newPrice, req.body.vendorId, req.body.price)
    }
    let httpreq = http.request(new HttpOption("/inventory/addItem"), function (response) {
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



app.get('/customer', function (req, res, next) {
    const httpreqGetItems = http.get("http://" + gatewayIp + ":8080/inventory/vendor/2", response => {
        let items: string = "";
        response.on('data', function (chunk) { items += chunk });
        response.on("end", () => {
            const httpreqGenerateHistory = http.get("http://" + gatewayIp + ":8080/history/getItems/buyer/1", response => {
                let history: string = "";
                response.on('data', function (chunk) { history += chunk });
                response.on('end', function () {
                    if (JSON.parse(items).command == "ToDo") {
                        res.render(__dirname + '/views/overviewCustomer.hbs', { buyedItems: [new Item(99, "TestItem", 24, 70, "ich", 65)], items: [new Item(99, "To Do", 24, 70, "ich", 65)] });
                    } else {
                        res.render(__dirname + '/views/overviewCustomer.hbs', { buyedItems: [new Item(99, "TestItem", 24, 70, "ich", 65)], items: JSON.parse(items) });
                    }
                })
            })
        })
    });
});


app.post('/addToCart', function (req, res, next) {
    let httpreq = http.request(new HttpOption("/cart/addItemToCart/1"), function (response) {
        let items: string = "";
        response.on('data', function (chunk) { items += chunk });
        response.on('end', function () {
            console.log(items);
            res.redirect('/customer');
        })
    });
    httpreq.on('error', function (err) {
        res.redirect('/customer');
    });
    console.log("addItem DAta:", new Item(req.body.itemId, req.body.itemName, req.body.newPiece, req.body.price, req.body.vendorId, req.body.price));
    httpreq.write(JSON.stringify(new Item(req.body.itemId, req.body.itemName, req.body.newPiece, req.body.price, req.body.vendorId, req.body.price)));
    httpreq.end();
});


app.post('/markProduct', function (req, res, next) {
    let httpreq = http.request(new HttpOption("/markedproduct/mark"), function (response) {
        response.on('end', function () {
            res.redirect('/customer');
        })
    });
    httpreq.on('error', function (err) {
        res.redirect('/customer');
    });
    httpreq.write(JSON.stringify({ vendorId: req.body.vendorId, costumerId: "1", price: req.body.price, email: "keine mail boys", itemName: req.body.itemName }));
    httpreq.end();
});



app.post('/checkout', function (req, res, next) {
    console.log("checkout called");
    let httpreq = http.get("http://" + gatewayIp + ":8080/checkout/checkout/1", response => {
        response.on('end', function () {
            res.redirect('/customer');
        })
    });
    httpreq.on('error', function (err) {
        res.redirect('/customer');
    });
    httpreq.write();
    httpreq.end();
});


app.post('/deleteItem', function (req, res, next) {
    let httpreq = http.request(new HttpOption("/inventory/delete/" + req.body.itemId), function (response) {
        response.on('end', function () {
            res.redirect('/customer');
        })
    });
    httpreq.on('error', function (err) {
        res.redirect('/customer');
    });
    httpreq.write();
    httpreq.end();
});

app.post('/rateItem', function (req, res, next) {
    let httpreq = http.request(new HttpOption("/rating/add"), function (response) {
        response.on('end', function () {
            res.redirect('/customer');
        })
    });
    httpreq.on('error', function (err) {
        res.redirect('/customer');
    });
    console.log("rating item" + JSON.stringify(new rating("1", req.body.itemId, req.body.itemName, req.body.rate)))
    httpreq.write(JSON.stringify(new rating("1", req.body.itemId, req.body.itemName, req.body.rate)));
    httpreq.end();
});


app.get('/Administrator', function (req, res, next) {
    //TODO get this from gateway
    res.render(__dirname + '/views/overviewAdmin.hbs', {
        services: {
            ratingService: "up",
            inventoryService: "up",
            cartService: "up",
            priceadjustmentService: "down",
            notificationService: "up",
            markedProductService: "up",
            checkoutService: "up",
            historyService: "up",
            shipmentService: "up"
        }
    });
});
let id;
app.post('/login', (req, res) => {
    id = req.body.ID;
    if (id == 1) {//customer
        res.redirect('/customer');
    } else if (id == 2) {//customer
        res.redirect('/vendor');
    } else {
        res.redirect('/administrator');
    }
})

