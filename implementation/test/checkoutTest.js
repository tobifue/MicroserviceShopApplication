
const url = require('url');
const http = require('http');




class HttpOption {
    constructor(path, method, host, port, headers) {
        this.host = host;
        this.port = port;
        this.path = path;
        this.method = method;
        this.headers = { "Content-Type": "application/json" };
    }
}
/*
let httpreq = http.request(new HttpOption("/rating/add"), function (response) {
    response.on('end', function () {
        res.redirect('/customer');
    })
}).on("error", (err) => {
    console.log(err);
    res.redirect('/customer');
});
console.log("rating item" + JSON.stringify(new rating("1", req.body.itemId, req.body.itemName, req.body.rate)))
httpreq.write(JSON.stringify(new rating("1", req.body.itemId, req.body.itemName, req.body.rate)));
httpreq.end();

*/


const httpreqGetItems = http.get("http://localhost:8080/checkout/checkout/1", response => {
        let items="";
        response.on('data', function (chunk) { items += chunk });
        response.on("end", () => {
            console.log(items);
        })
        }).on("error", (err) => {
            console.log(err);
        });