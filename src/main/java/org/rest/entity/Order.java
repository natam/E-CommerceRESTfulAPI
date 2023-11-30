package org.rest.entity;

public class Order {
    private int id;
    private int productId;
    private int quantity;
    private String customer;

    public Order(int id, int productId, int quantity, String customer) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.customer = customer;
    }

    public Order(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
}
