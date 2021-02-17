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
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import static com.example.myfirsttestapplication.MainActivity.PORT;
import static com.example.myfirsttestapplication.MainActivity.serverIP;

public class streamSensorData extends Service implements SensorEventListener {
    /* Take sensor data from SensorEvents and send sensor data to a server in real time.
    The server is running on a second device (laptop) in processing file named "server.pde"
    If Permission is denied, make sure Server is running
    also make sure permission INTERNET is granted in Android Manifest

     ----------------------------------------------------------------------------------------------
     the code is oriented on the following tutorial by Shane Conder & Lauren Darcey:
     https://code.tutsplus.com/tutorials/android-barometer-logger-acquiring-sensor-data--mobile-10558
     using Dataoutputstream as described in https://stackoverflow.com/questions/5680259/using-sockets-to-send-and-receive-data
     more Info on sensor events found here: https://developer.android.com/reference/android/hardware/SensorEvent
     */


    private static Socket link; //created in createConnection


    public int onStartCommand(Intent intent, int flags, int startId){
        // access accelerometer of device
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        new createConnection().execute();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // processes Data of Accelerometer as onStartCommand() registered a
        // listener to the devices Accelerometer Sensor.
        // When Accelerometer data changes write updated data into output stream
        // (only works if connection is already created >> link !=null
        if (link!=null) {
            try {
                sendToServer(sensorEvent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //do nothing
    }


    private static class createConnection extends AsyncTask<Void, Void, Void> {
        /*set up a tcp socket and connect it to server (running as server.pde on laptop)*/

        @Override
        protected Void doInBackground(Void... voids){

            // first, see if IP Adress of laptop can be found
            try {
                InetAddress address =  InetAddress.getByName(serverIP);
                Log.d("BBB", "Found Server on ip " + address.toString());
            }
            catch (UnknownHostException e) {
                Log.d("BBB", "unknown host exception");
            }

            // then, create socket that connects to this ip adsress
            //(Port must be same as in server.pde; may be changed by user in settings of the app)
            try {
                link = new Socket(serverIP, PORT);
                Log.d("BBB", "successfully connected to server");

            }
            catch(IOException e){
                //throws exception if connection failed
                e.printStackTrace();
                Log.d("BBB", e.getMessage());
            }

            return null;
        }
    }

    private static void sendToServer(SensorEvent event) throws IOException {
        /*Accelerometer Data is send to processing file server.pde for visualisation*/


        //create outputstream from socket link
        //this outputstream can then be read by server.pde on the specified port
        OutputStream out = link.getOutputStream();

        //to write data to this output stream, wrap it with PrintWriter
        //the printwriter writer sends chunks of one line; uncomment command below to test
        PrintWriter writer = new PrintWriter(out, true);
        // writer.println("Hello Server"); //to test connection


        //access sensor values from phone with SensorEvent via onSensorChanged()
        //accelerometer values hold float Array with current values for each Axis
        //(more info on Accelerometer Coordinate System here:
        //https://developer.android.com/guide/topics/sensors/sensors_overview#sensors-coords)
        float xValues = event.values[0]; //x-Axis to detect hollow back
        float yValues = event.values[1]; //y-Axis to detect hips falling to one side

        //now flush those values to outputstream using the PrintWriter
        writer.println(xValues + ";" + yValues);
        }
}