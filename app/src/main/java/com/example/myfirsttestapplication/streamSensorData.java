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


    private static final String serverIP = "192.168.0.200."; // defaults to IP Address of Laptop that runs server.pde
                                                // may be changed by user in settings of the app
    private static final int PORT = 12345;

    public int onStartCommand(Intent intent, int flags, int startId){
        // access accelerometer of device
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        return START_STICKY;
    }


    private static class SensorLogger extends AsyncTask<SensorEvent, Void, Void> {
        @Override
        protected Void doInBackground(SensorEvent... events){
            SensorEvent event = events[0];
            Log.d("CCC", String.valueOf(event));

            try {
                InetAddress address =  InetAddress.getByName(serverIP);
                Log.d("BBB", "connected to server with ip: " + address.toString());
            }
            catch (UnknownHostException e) {
                //Todo toast error check ip adress in settings
                Log.d("BBB", "unknown host exception");
            }
            sendToServer(event);
            return null;
        }
    }

    private static void sendToServer(SensorEvent event) {
        //processes Data of Accelerometer as onStartCommand() registered a
        //listener to the devices Accelerometer Sensor


        //Accelerometer Data is send to processing file server.pde for visualisation
        //therefore tcp connection is set up in the following:
        //if connection breaks/cannot be established, exception is thrown and try again

        Socket link;
        try {
            // create socket for connection with server; make sure port equals port in server.pde
            //must be same as in server.pde; may be changed by user in settings of the app
            link = new Socket(serverIP, PORT);
            Log.d("BBB", "successfully connected to socket");

            //create outputstream from this socket
            //this outputstream can then be read by server.pde on the specified port
            OutputStream out = link.getOutputStream();

            //to write data to output stream, wrap it with PrintWriter
            //the printwriter writer sends chunks of one line; uncomment command below to test
            PrintWriter writer = new PrintWriter(out, true);
            // writer.println("Hello Server"); //to test connection


            //access sensor values from phone with SensorEvent
            //accelerometer values hold float Array with current values for each Axis
            //(more info on Accelerometer Coordinate System here:
            //https://developer.android.com/guide/topics/sensors/sensors_overview#sensors-coords)
            float xValues = event.values[0]; //x-Axis to detect hollow back
            float yValues = event.values[1]; //y-Axis to detect hips falling to one side

            //now flush those values to outputstream using the PrintWriter
            writer.println( xValues + ";" + yValues);
        }
        catch(IOException e){
            //throws exception if connection failed
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
        // detect change in position and call SensorLogger for sensorEvent specified in OnStartCommand
        new SensorLogger().execute(sensorEvent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //do nothing
    }
}