
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
const costumerId = '1';
const gatewayIp = process.env.GATEWAYIP || "localhost";
console.log("IP used: " + gatewayIp);
const getDockerHost = require('get-docker-host');
const isInDocker = require('is-in-docker');



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

class rating{
    costumerId:string;
    itemId:number;
    itemName:string;
    rating:number;
    constructor(costumerId: string, itemId: number, itemName: string, rating: number){
        this.costumerId = costumerId;
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


//Todo only items from this vendor!!
app.get('/vendor', function (req, res, next) {
    const httpreqGetItems = http.get("http://" + gatewayIp + ":8080/inventory/1", response => {
        let items: string = "";
        response.on('data', function (chunk) { items += chunk });
        response.on("end", () => {
            if (items != null) {
                res.render(__dirname + '/views/overviewVendor.hbs', { items: [new Item(1, "Itemname: ToDO", 24, 70, "ich", 65)] });
            } else {
                res.render(__dirname + '/views/overviewVendor.hbs', { items: JSON.parse(items) });
            }
        })
    })
});



app.post('/addItem', function (req, res, next) {
    let data =new Item(0, req.body.itemName, req.body.quantity, req.body.price, req.body.vendorId, req.body.price);
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



app.get('/costumer', function (req, res, next) {
    const httpreqGetItems = http.get("http://" + gatewayIp + ":8080/inventory/vendor", response => {
        let items: string = "";
        response.on('data', function (chunk) { items += chunk });
        response.on("end", () => {
            const httpreqGenerateHistory = http.get("http://" + gatewayIp + ":8080/history/getItems/buyer/1", response => {
                let history: string = "";
                response.on('data', function (chunk) { history += chunk });
                response.on('end', function () {
                    if (JSON.parse(items).command == "ToDo") {
                        res.render(__dirname + '/views/overviewCostumer.hbs', { buyedItems: [new Item(99, "TestItem", 24, 70, "ich", 65)],items: [new Item(99, "To Do", 24, 70, "ich", 65)] });
                    } else {
                        res.render(__dirname + '/views/overviewCostumer.hbs', { buyedItems: [new Item(99, "TestItem", 24, 70, "ich", 65)],items: [new Item(99, "To Do", 24, 70, "ich", 65)] });
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
            res.redirect('/costumer');
        })
    });
    httpreq.on('error', function (err) {
        res.redirect('/costumer');
    });
    console.log("addItem DAta:", new Item(req.body.itemId, req.body.itemName, req.body.newPiece, req.body.price, req.body.vendorId, req.body.price));
    httpreq.write(JSON.stringify(new Item(req.body.itemId, req.body.itemName, req.body.newPiece, req.body.price, req.body.vendorId, req.body.price)));
    httpreq.end();
});


app.post('/markProduct', function (req, res, next) {
    let httpreq = http.request(new HttpOption("/productMark/markItem"), function (response) {
        response.on('end', function () {
            res.redirect('/costumer');
        })
    });
    httpreq.on('error', function (err) {
        res.redirect('/costumer');
    });
    httpreq.write(JSON.stringify({ costumer: costumerId, item: new Item(req.body.itemId, req.body.itemName, 0, req.body.price, req.body.vendor, req.body.price) }));
    httpreq.end();
});



app.post('/checkout', function (req, res, next) {
    console.log("checkout called");
    let httpreq =http.get("http://" + gatewayIp + ":8080/checkout/checkout/1", response => {
        response.on('end', function () {
            res.redirect('/costumer');
        })
    });
    httpreq.on('error', function (err) {
        res.redirect('/costumer');
    });
    httpreq.write();
    httpreq.end();
});




app.post('/rateItem', function (req, res, next) {
    let httpreq = http.request(new HttpOption("/rating/add"), function (response) {
        response.on('end', function () {
            res.redirect('/costumer');
        })
    });
    httpreq.on('error', function (err) {
        res.redirect('/costumer');
    });
    console.log("rating item"+JSON.stringify(new rating("1", req.body.itemId, req.body.itemName, req.body.rate)) )
    httpreq.write(JSON.stringify(new rating("1", req.body.itemId, req.body.itemName, req.body.rate)));
    httpreq.end();
});


