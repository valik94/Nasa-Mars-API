package com.example.nasaapiapp;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkingService {
    private String apikey = "wArFt3VHK2bJ853DS1uac88LIsGSaFyF4lfgIrkB" ;
    private String  rover = "";
    private String earthdate = "";
    private String nasaURL = "https://api.nasa.gov/mars-photos/api/v1/rovers/"+rover+"/photos?earth_date="+earthdate+"&api_key="+apikey;


    public static ExecutorService networkExecutorService = Executors.newFixedThreadPool(4);
    public static Handler networkingHandler = new Handler(Looper.getMainLooper());

    interface NetworkingListener{
        void dataListener(String josnString);
        void imageListener(Bitmap image);
    }

    public NetworkingListener listener;



    public void getNasaImages(String rover, String date){

        String url =  "https://api.nasa.gov/mars-photos/api/v1/rovers/"+rover+"/photos?earth_date="+date+"&api_key="+apikey;
        connect(url);
    }


    public void getImageData(String imgurl){

        networkExecutorService.execute(new Runnable() {
            @Override
            public void run() {

//                 //  String img = "https://images.freeimages.com/images/small-previews/a31/colorful-umbrella-1176220.jpg";
//                    URL url = new URL(imgurl);
//                  //  InputStream in = new java.net.URL(imgurl).openStream();
//                   // InputStream is = new BufferedInputStream(url.openStream());
//
//                    Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream()) ;
                HttpURLConnection httpURLConnection = null;
                // any code here will run in background thread
                try {
                    String jsonString = "";
                    URL urlObject = new URL(imgurl);
                    //jsonString = ((String)urlObject.getContent());
                    //Log.d("txt",jsonString);
                    httpURLConnection = (HttpURLConnection) urlObject.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setRequestProperty("Content-Type", "application/json");

                    InputStream in = httpURLConnection.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(in);
                    Bitmap bitmap = BitmapFactory.decodeStream(bis);


                    networkingHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.imageListener(bitmap);
                        }
                    });

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void connect(String url){
           networkExecutorService.execute(new Runnable() {
               @Override
               public void run() {
                   HttpURLConnection  httpURLConnection = null;
                   // any code here will run in background thread
                   try {
                       String jsonString = "";
                       URL urlObject = new URL(url);
                       //jsonString = ((String)urlObject.getContent());
                       //Log.d("txt",jsonString);
                        httpURLConnection= (HttpURLConnection)urlObject.openConnection();
                        httpURLConnection.setRequestMethod("GET");
                        httpURLConnection.setRequestProperty("Content-Type","application/json");

                        InputStream in = httpURLConnection.getInputStream();
                        InputStreamReader reader = new InputStreamReader(in);
                        int inputStreamData = 0;
                        while ( (inputStreamData = reader.read()) != -1){
                            char current = (char)inputStreamData;
                            jsonString+= current;
                        } // json is ready
                       // I can send it to somewhere else to decode it

                       final String finalJsonString = jsonString;
                       networkingHandler.post(new Runnable() {
                           @Override
                           public void run() {
                               listener.dataListener(finalJsonString);
                           }
                       });


                   } catch (MalformedURLException e) {
                       e.printStackTrace();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
                   finally {
                       httpURLConnection.disconnect();
                   }


               }
           });
    }

}
