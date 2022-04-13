package com.example.nasaapiapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonService {



    public ArrayList<NasaPhoto> getNasaData(String jsonString) {
        ArrayList<NasaPhoto> photos = new ArrayList(0);
        try {
            JSONObject fullObject = new JSONObject(jsonString);
            JSONArray allPhotos = fullObject.getJSONArray("photos");
            for (int i = 0 ; i < allPhotos.length(); i++){
                NasaPhoto photo = new NasaPhoto();

                 JSONObject obj =  allPhotos.getJSONObject(i);
                 photo.img_url = obj.getString("img_src");
                 photo.id = obj.getInt("id");
                 photo.date = obj.getString("earth_date");
                Log.d("img",photo.img_url);
                photos.add(photo );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  photos;

    }


}
