var expect  = require('chai').expect;
var request = require('request');
const supertest= require("supertest");
let api = supertest((process.env.GATEWAYIP || "localhost")+':8085');
console.log("IP used: " + (process.env.GATEWAYIP || "localhost"));


it('expect return a 200 respronse', function (done){
    api.get('/getCart/1')
    .set('Accept', 'application/json')
    .expect(200, done);
})