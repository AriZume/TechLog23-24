package com.example.geosnap.databases;

public class DatabaseData {
    private String dataAuthor, dataName, dataDesc, dataDate, dataImage, key;

    public DatabaseData(String dataAuthor, String dataName, String dataDesc, String dataDate, String dataImage) {
        this.dataAuthor = dataAuthor;
        this.dataName = dataName;
        this.dataDesc = dataDesc;
        this.dataDate = dataDate;
        this.dataImage = dataImage;
    }
    public DatabaseData(){
    }

    public String getDataAuthor() {
        return dataAuthor;
    }

    public void setDataAuthor(String dataAuthor) {
        this.dataAuthor = dataAuthor;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }

    public String getDataDesc() {
        return dataDesc;
    }

    public void setDataDesc(String dataDesc) {
        this.dataDesc = dataDesc;
    }

    public String getDataDate() {
        return dataDate;
    }

    public void setDataDate(String dataDate) {
        this.dataDate = dataDate;
    }

    public String getDataImage() {
        return dataImage;
    }

    public void setDataImage(String dataImage) {
        this.dataImage = dataImage;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
