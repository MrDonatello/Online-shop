package net.thumbtack.onlineshop.model;

import java.util.List;

public class Basket {

    private List<Product> products;
    private int clientId;


    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
}
