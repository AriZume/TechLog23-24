package com.example.geosnap.databases;

import java.util.ArrayList;

public class DatabaseData {

    private double latitude, longitude, imgSize;
    String imgHeight, imgWidth, imageURL;
    String dateTime, tag, desc;

    ArrayList<String> imagesUrl;

    public DatabaseData() {
    }

    public DatabaseData(double latitude, double longitude, double imgSize, String imgHeight, String imgWidth, String imageURL, String dateTime) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.imgSize = imgSize;
        this.imgHeight = imgHeight;
        this.imgWidth = imgWidth;
        this.imageURL = imageURL;
        this.dateTime = dateTime;
    }

    public DatabaseData(double latitude, double longitude, double imgSize, String imgHeight, String imgWidth, String imageURL, String dateTime, String tag, String desc) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.imgSize = imgSize;
        this.imgHeight = imgHeight;
        this.imgWidth = imgWidth;
        this.imageURL = imageURL;
        this.dateTime = dateTime;
        this.tag = tag;
        this.desc = desc;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getImgSize() {
        return imgSize;
    }

    public void setImgSize(double imgSize) {
        this.imgSize = imgSize;
    }

    public String getImgHeight() {
        return imgHeight;
    }

    public void setImgHeight(String imgHeight) {
        this.imgHeight = imgHeight;
    }

    public String getImgWidth() {
        return imgWidth;
    }

    public void setImgWidth(String imgWidth) {
        this.imgWidth = imgWidth;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }


    public ArrayList<String> getImagesUrl() {
        return imagesUrl;
    }

    public void setImagesUrl(ArrayList<String> imagesUrl) {
        this.imagesUrl = imagesUrl;
    }
}
