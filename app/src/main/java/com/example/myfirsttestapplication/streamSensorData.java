package com.example.myfirsttestapplication;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class streamSensorData extends Service implements SensorEventListener {
    /* this class takes sensor data from SensorEvents
    and sends it to a server running in processing file server.pde
    if Permission is denied, make sure Server is running
    also make sure permission INTERNET is granted in Android Manifest

     the code is oriented on the following tutorial by Shane Conder & Lauren Darcey:
     https://code.tutsplus.com/tutorials/android-barometer-logger-acquiring-sensor-data--mobile-10558
     using Dataoutputstream as described in https://stackoverflow.com/questions/5680259/using-sockets-to-send-and-receive-data
     more Info on sensor events found here: https://developer.android.com/reference/android/hardware/SensorEvent
     */


    private SensorManager sensorManager = null;
    private Sensor sensor = null;
    public String data;

    public int onStartCommand(Intent intent, int flags, int startId ){
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        return START_STICKY;
    }


    private class SensorLogger extends AsyncTask<SensorEvent, Void, Void> {
        @Override
        protected Void doInBackground(SensorEvent... events){
            SensorEvent event = events[0];
            Log.d("CCC", String.valueOf(event));


            try {
                InetAddress address =  InetAddress.getByName("192.168.0.200."); //checking for Laptop ip that runs processing server
                Log.d("BBB", address.toString());
            }
            catch (UnknownHostException e) {
                Log.d("BBB", "unknown host exception");
            }
            accessServer(event);

            return null;
        }
    }

    private static void accessServer(SensorEvent event) {
        Socket link = null;
        try {
            link = new Socket("192.168.0.200.", 12345); //important: put ip adress of server's host machine
            Log.d("BBB", "successfully connected to socket");
            OutputStream out = link.getOutputStream();
            // byte[] data = ...
            //out.write(data);  //send data to server
            PrintWriter writer = new PrintWriter(out, true);
            //writer.println("Hello Server"); to test connection

            //send sensor data to server
            //TODO: make it bytes instead of string?
            byte xValues = (byte) event.values[0]; //checking for hollow back
            byte yValues = (byte) event.values[1]; //checking if hip turns left/right

            byte[] coord = {xValues,yValues};

            //writer.println( xValues + ";" + yValues); //Sending X and Y values as String
            writer.println(coord);

        }
        catch(IOException e){
            e.printStackTrace();
            Log.d("BBB", e.getMessage());
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float currentValueX = sensorEvent.values[0]; // 0 = X-Axis, 1 = Y-Axis, 2 = Z-Axis
        float currentValueY = sensorEvent.values[1];
        float currentValueZ = sensorEvent.values[2];

        data = Float.toString(currentValueX);


        new SensorLogger().execute(sensorEvent);
        //TODO: Do the same for y, z


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //do nothing
    }
}