package com.example.myfirsttestapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class listSensors extends AppCompatActivity {
    private SensorManager mSensorManager; //create instance of sensor manager system service following https://developer.android.com/codelabs/advanced-android-training-sensor-data#2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sensors);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE); //get Sensor Information from Phone



        List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL); // and store in List

         StringBuilder sensorText = new StringBuilder();

         for (Sensor currentSensor : sensorList){
             sensorText.append(currentSensor.getName()).append(System.getProperty("line.separator"));
             }


         TextView sensorTextView = (TextView) findViewById(R.id.sensor_list);
         sensorTextView.setText(sensorText);

     }
}
