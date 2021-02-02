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

public class streamSensorData extends Service implements SensorEventListener {
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
            float xValues = event.values[0];
            Log.d("CCC", Float.toString(xValues));
            return null;
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