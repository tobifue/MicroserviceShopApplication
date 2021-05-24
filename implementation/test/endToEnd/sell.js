var expect = require('chai').expect;
var request = require('request');
const supertest = require("supertest");
let gateway = supertest('http://localhost:8080');

const Item = require("../../cart/container/src/cart").Item;
const Cart = require("../../cart/container/src/cart").Cart;
const itemId = Math.floor(Math.random() * 100),
    itemName = Math.random().toString(36).substring(5),
    quantity = Math.floor(Math.random() * 100),
    price = Math.floor(Math.random() * 1000)/10,
    vendorId = Math.floor(Math.random() * 100),
    priceRecommendation = Math.floor(Math.random() * 1000)/10,
    userId = Math.floor(Math.random() * 100);


let rndItem = new Item(
    itemId,
    itemName,
    quantity,
    price,
    vendorId,
    priceRecommendation
);

let start = () => {
    describe('End to End test sell', function () {
        it('add rnd item', function (done) {
            gateway.post('/inventory/').set("Content-Type", 'application/json').send(JSON.stringify(rndItem)).end((err, res)=>{
                rndItem.itemId = JSON.parse(res.text).itemId;
                expect(JSON.stringify(JSON.parse(res.text))).equal(JSON.stringify(rndItem));
                done();
            })
        })

        it('check if item is in iventory', function (done) {
            gateway.get('/inventory/vendor/')
                .set('Content-Type', 'application/json')
                .end((err, res)=>{
                    items = JSON.parse(res.text);
                    for(let i=0; i<items.length; ++i){
                        if(items[i].itemName == rndItem.itemName){
                            expect(true)
                            done();
                            return;
                        }
                    }
                    expect(false)
                    done();
                });
        })

        it('add to cart', function (done) {
            gateway.post('/cart/addItemToCart/'+userId).set("Content-Type", 'application/json').send(JSON.stringify(rndItem)).end((err, res)=>{
                console.log(res)
                expect("OK").equal(res.text);
                done();
            })
        })
    })
}


module.exports.start = start;