package com.example.myfirsttestapplication;

import android.hardware.SensorEvent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class connectToServer extends AsyncTask<Void, Void, Void> {
    //starts new class to connect to Server
    //if Permission is denied, make sure Server is running >> using server.pde Processing file for game
    //also make sure permission INTERNET is granted in Android Manifest

    //using Dataoutputstream as in https://stackoverflow.com/questions/5680259/using-sockets-to-send-and-receive-data

    @Override
    protected Void doInBackground(Void... voids) {
        Log.d("BBB", "entered connectToServer");
            try {
                InetAddress address = InetAddress.getLocalHost();
                Log.d("BBB", address.toString());
            }
            catch (UnknownHostException e) {
                Log.d("BBB", "unknown host exception");
            }
            accessServer();
        return null;
    }

    private static void accessServer() {
        Socket link = null;
        try {
            link = new Socket("192.168.0.200.", 12345); //important: put ip adress of server's host machine
            Log.d("BBB", "successfully connected to socket");
            OutputStream out = link.getOutputStream();
            // byte[] data = ...
            //out.write(data);  //send data to server
            PrintWriter writer = new PrintWriter(out, true);
            writer.println("Hello Server");

            //TODO: put sensor data here instead


        }
        catch(IOException e){
            e.printStackTrace();
            Log.d("BBB", e.getMessage());
        }
    }
}
