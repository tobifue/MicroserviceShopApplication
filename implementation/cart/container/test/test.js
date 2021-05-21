var expect = require('chai').expect;
var request = require('request');
const supertest = require("supertest");
let api = supertest((process.env.GATEWAYIP || "localhost") + ':8085');
console.log("IP used: " + (process.env.GATEWAYIP || "localhost"));




const Item = require("../src/cart").Item;
const Cart = require("../src/cart").Cart;
const itemId = Math.random(),
    itemName = Math.random().toString(36).substring(5),
    quantity = Math.random(),
    price = Math.random(),
    vendorId = Math.random().toString(36).substring(5),
    priceRecommendation = Math.random(),
    userId = Math.random();


let rndItem = new Item(
    itemId,
    itemName,
    quantity,
    price,
    vendorId,
    priceRecommendation
);

describe('Integration Test Cart - 1', function () {

    it('connection', function (done) {
        api.get('/getCart/1')
            .set('Accept', 'application/json')
            .expect(200, done);
    })

    it('addItem endpoint', function (done) {
        api.post('/addItem/' + userId)
            .set("Content-Type", 'application/json')
            .send(JSON.stringify(rndItem))
            .end((err, res) => {
                if (err) console.log(res.body)
                expect(res.text).equal("OK");
                done();
            })
    })

    it('removeItem endpoint', function (done) {
        api.post('/removeItem/' + userId)
            .set("Content-Type", 'application/json')
            .send(JSON.stringify(rndItem))
            .end((err, res) => {
                expect(res.text).equal("OK");
                done();
            })
    })


    it('check empty Cart', function (done) {
        api.get('/getCart/' + userId)
            .set('Content-Type', 'application/json')
            .end((err, res) => {
                let tmpCart = new Cart(userId).add(rndItem);
                tmpCart.remove(rndItem);
                expect(res.text).equal(JSON.stringify(tmpCart));
                done();
            })
    })

    it('addItem again', function (done) {
        api.post('/addItem/' + userId)
            .set("Content-Type", 'application/json')
            .send(JSON.stringify(rndItem))
            .end((err, res) => {
                if (err) console.log(res.body)
                expect(res.text).equal("OK");
                done();
            })
    })


    it('getCart', function (done) {
        api.get('/getCart/' + userId)
            .set('Content-Type', 'application/json')
            .end((err, res) => {
                let tmpCart = new Cart(userId).add(rndItem);
                tmpCart.remove(rndItem);
                tmpCart.add(rndItem);
                expect(res.text).equal(JSON.stringify(tmpCart));
                done();
            })
    })



    it('deleteCart', function (done) {
        api.get('/deleteCart/' + userId)
            .set('Content-Type', 'application/json')
            .end((err, res) => {
                expect(res.text).equal("OK");
                done();
            })
    })

    it('empty Cart', function (done) {
        api.get('/getCart/' + userId)
            .set('Content-Type', 'application/json')
            .end((err, res) => {
                expect(res.text).equal('');
                done();
            })
    })
});





describe('Integration Test Cart - 2', function () {

    let rndItem2 = new Item(
        itemId*2,
        itemName+'2',
        quantity*2,
        price*2,
        vendorId+'2',
        priceRecommendation*2
    );
    it('addItem', function (done) {
        api.post('/addItem/' + userId)
            .set("Content-Type", 'application/json')
            .send(JSON.stringify(rndItem))
            .end((err, res) => {
                if (err) console.log(res.body)
                expect(res.text).equal("OK");
                done();
            })
    })

    it('addItem', function (done) {
        api.post('/addItem/' + userId)
            .set("Content-Type", 'application/json')
            .send(JSON.stringify(rndItem2))
            .end((err, res) => {
                expect(res.text).equal("OK");
                done();
            })
    })


    it('getCart', function (done) {
        api.get('/getCart/' + userId)
            .set('Content-Type', 'application/json')
            .end((err, res) => {
                let tmpCart = new Cart(userId).add(rndItem);
                tmpCart.add(rndItem2);
                expect(res.text).equal(JSON.stringify(tmpCart));
                done();
            })
    })



    it('deleteCart', function (done) {
        api.get('/deleteCart/' + userId)
            .set('Content-Type', 'application/json')
            .end((err, res) => {
                expect(res.text).equal("OK");
                done();
            })
    })

    it('empty Cart', function (done) {
        api.get('/getCart/' + userId)
            .set('Content-Type', 'application/json')
            .end((err, res) => {
                expect(res.text).equal('');
                done();
            })
    })
});


