package com.example.myapplication;

import android.widget.EditText;

class DonateModel {
    private String donate_id;
    private EditText description;
    private String foodType;
    private String food_img;

    public void setDonate_id(String donate_id) {
        this.donate_id = donate_id;
    }

    public String getDonate_id() {
        return donate_id;
    }

    public void setDescription(EditText description) {
        this.description = description;
    }

    public EditText getDescription() {
        return description;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFood_img(String food_img) {
        this.food_img = food_img;
    }

    public String getFood_img() {
        return food_img;
    }
}