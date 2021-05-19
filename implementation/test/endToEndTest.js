var expect  = require('chai').expect;
var request = require('request');
const supertest= require("supertest");
let api = supertest('http://localhost:8085');

it('expect return a 200 respronse', function (done){
    api.get('/getCart/1')
    .set('Accept', 'application/json')
    .expect(200, done);
})