package com.example.myfirsttestapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;



import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager; //create instance of sensor manager system service following https://developer.android.com/codelabs/advanced-android-training-sensor-data#2
    private Sensor mGyroscope;
    private TextView mGyroscopeText;

    //onCreate send data all the time, also when app is not running in the foreground > better use onStart

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE); //get Sensor Information from Phone
        mGyroscopeText = (TextView) findViewById(R.id.label_gyroscope);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        String sensor_error = getResources().getString(R.string.error_no_sensor);

        if (mGyroscope == null){
            mGyroscopeText.setText(sensor_error);
        }
    }

       /* List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL); // and store in List

        StringBuilder sensorText = new StringBuilder();

        for (Sensor currentSensor : sensorList){
            sensorText.append(currentSensor.getName()).append(System.getProperty("line.separator"));
        }
        //TextView sensorTextView = (TextView) findViewById(R.id.sensor_list);
        //sensorTextView.setText(sensorText);

        mGyroscopeText = (TextView) findViewById(R.id.label_gyroscope);

        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        String sensor_error = getResources().getString(R.string.error_no_sensor);

        if (mGyroscope==null){
            mGyroscopeText.setText(sensor_error);
        }
    }*/

    @Override
    protected void onStart() {
        super.onStart();

        if (mGyroscope != null){
            mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
            mGyroscopeText = (TextView)findViewById(R.id.label_gyroscope);
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        mSensorManager.unregisterListener(this);
    }

    public void showSensorList(){
        Intent intent = new Intent (this, listSensors.class);
        startActivity(intent);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
    //int sensorType = sensorEvent.sensor.getType(); //only necessary if > 1 sensor used
    float currentValue = sensorEvent.values[0]; //?
    mGyroscopeText.setText(getResources().getString(R.string.label_gyroscope, currentValue)); //?
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}