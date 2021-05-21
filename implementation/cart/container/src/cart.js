var Cart = /** @class */ (function () {
    function Cart(id) {
        this.customerId = id;
        this.list = [];
    }
    Cart.prototype.add = function (item) {
        this.list.push(item);
        return this;
    };
    Cart.prototype.find = function (item) {
        return this.list.includes(item);
    };
    Cart.prototype.remove = function (item) {
        this.list.splice(this.list.indexOf(item), 1);
    };
    return Cart;
}());
var Item = /** @class */ (function () {
    function Item(itemId, itemName, quantity, price, vendorId, priceRecommendation) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.vendorId = vendorId;
        this.priceRecommendation = priceRecommendation;
    }
    return Item;
}());
module.exports.Item = Item;
module.exports.Cart = Cart;
