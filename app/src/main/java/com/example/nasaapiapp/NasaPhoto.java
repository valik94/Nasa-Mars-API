package com.example.nasaapiapp;

public class NasaPhoto {
    int id = -1;
    String img_url = "";
    String date = "";
    public NasaPhoto() { }



    public NasaPhoto(int id, String img_url, String rover, String date) {
        this.id = id;
        this.img_url = img_url;
        this.date = date;
    }
}
