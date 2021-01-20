package com.example.myfirsttestapplication;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.Socket;

public class connectToServer extends AsyncTask<Void, Void, Void> {
    //starts new class to connect to Server
    //if Permission is denied, make sure Server is running >> using server.pde Processing file for game
    //also make sure permission INTERNET is granted in Android Manifest

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Socket mySocket = new Socket("192.168.0.200", 12345); //important: enter ip & port of Server here
            Log.d("BBB", "successfully connected to socket");

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("BBB", e.getMessage());
        }
        return null;
    }
}

