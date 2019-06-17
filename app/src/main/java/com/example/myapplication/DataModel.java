package com.example.myapplication;

public class DataModel {

    String strData;
    int imgData;
    String strLang;
    private int imgLang;

    public DataModel(String strLang, int imgLang) {
        this.strLang = strLang;
        this.imgLang = imgLang;
    }

    public String getStrData() {
        return strData;
    }

    public void setStrData(String strData) {
        this.strData = strData;
    }

    public int getImgData() {
        return imgData;
    }

    public void setImgData(int imgData) {
        this.imgData = imgData;
    }

    public String getStrLang() {
        return strLang;
    }


    public int getImgLang() {
        return imgLang;
    }
}
