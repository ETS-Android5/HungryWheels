package com.example.myapplication;

class CartModel {
    private String cart_id;
    private String food_name;
    private String food_price;
    private String user_cart_id;

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_price(String food_price) {
        this.food_price = food_price;
    }

    public String getFood_price() {
        return food_price;
    }

    public void setUser_cart_id(String user_cart_id) {
        this.user_cart_id = user_cart_id;
    }

    public String getUser_cart_id() {
        return user_cart_id;
    }
}
