class Item{
    constructor(name, quantity, price, vendor){
        this.name=name;
        this.quantity=quantity;
        this.price=price;
        this.vendor=vendor;

    }
}

Item1 = new Item("Toast1", 2, 23, "blubVencor");
Item2 = new Item("Toast2", 2, 23, "blubVencor44");
Item3 = new Item("Toast3", 2, 23, "blubVencor");

class Cart{
    constructor(id){
        this.id =id;
        this.list=[];
    }

    add(item){

    }

    getSum(){
        return this.list.reduce(function (a, b) {
            return a.price + b.price;
        }, 0);
    }
}

cartP1 = new Cart("0asd");
console.log(cartP1);


const express = require('express')
let app = express();
const port = 3000;
app.use(express.json());


//answering a get request
app.post('/addItem', function(req, res, next) {
    console.log("body der POST anfrage", req.body)
   res.send("OK");
  });

app.listen(port, function() {
    console.log('Server started on port: ' + port);
 });




//sending a http request

   /*
var options = {
    host: 'localhost',
    port: 8081,
    path: '/checkout',
    method: 'POST',
    headers: {
		"Content-Type": "application/json"
	}
}

let data = {
    items : [Item1, Item2, Item3],
    price : 99, //eigentlich summe der items
    costumerId : "-19",
}

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

        