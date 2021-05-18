class Cart {
    customerId:number;
    list:Item[];
    constructor(id:number) {
        this.customerId = id;
        this.list = [];
    }

    add(item:Item) {
        this.list.push(item)
        return this;
    }


}


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

module.exports.Item = Item;
module.exports.Cart = Cart;