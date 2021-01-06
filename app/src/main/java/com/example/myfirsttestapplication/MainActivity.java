package com.example.myfirsttestapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;



import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager; //create instance of sensor manager system service following https://developer.android.com/codelabs/advanced-android-training-sensor-data#2
    private Sensor mGyroscope;
    private TextView mGyroscopeText;
    private Sensor mOtherSensor;
    private TextView mOtherSensorText;


    //onCreate send data all the time, also when app is not running in the foreground > better use onStart

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE); //get Sensor Information from Phone
        mGyroscopeText = (TextView) findViewById(R.id.label_gyroscope);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        String sensor_error = getResources().getString(R.string.error_no_sensor);
        mOtherSensorText = (TextView) findViewById(R.id.label_otherSensor);
        mOtherSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        if (mGyroscope == null){
            mGyroscopeText.setText(sensor_error);
        }

        if (mOtherSensor == null){
            mOtherSensorText.setText(sensor_error);
        }
    }

    public void showSensorList(View view){
        Intent intent = new Intent (this, listSensors.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mGyroscope != null){
            mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
            mGyroscopeText = (TextView)findViewById(R.id.label_gyroscope);
        }

        if (mOtherSensor != null){
            mSensorManager.registerListener(this, mOtherSensor, SensorManager.SENSOR_DELAY_NORMAL);
            mOtherSensorText = (TextView)findViewById(R.id.label_otherSensor);
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        mSensorManager.unregisterListener(this); // unregisters all registered Listeners
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType(); //only necessary if > 1 sensor used

        switch (sensorType) {
            case Sensor.TYPE_GYROSCOPE:
                float currentValueX = sensorEvent.values[0]; // 0 = X-Axis, 1 = Y-Axis, 2 = Z-Axis
                float currentValueY = sensorEvent.values[1];
                float currentValueZ = sensorEvent.values[2];
                mGyroscopeText.setText(getResources().getString(R.string.label_gyroscope,
                        currentValueX, currentValueY, currentValueZ));
                break;

            case Sensor.TYPE_ROTATION_VECTOR:  //following https://code.tutsplus.com/tutorials/android-sensors-in-depth-proximity-and-gyroscope--cms-28084
                float[] rotationMatrix = new float[16];
                SensorManager.getRotationMatrixFromVector(rotationMatrix, sensorEvent.values);

                //remap coordinate system for later conversion to orientations
                // while remapping new z-axis coincides with y-axis of original system

                float[] remappedRotationMatrix = new float[16];
                mSensorManager.remapCoordinateSystem(rotationMatrix, mSensorManager.AXIS_X, mSensorManager.AXIS_Z, remappedRotationMatrix);

                float[] orientations = new float[3];
                mSensorManager.getOrientation(remappedRotationMatrix, orientations);

                for (int i = 0; i <3; i++){
                    orientations[i] = (float) (Math.toDegrees(orientations[i]));
                }


                mOtherSensorText.setText(getResources().getString(R.string.label_otherSensor, orientations[0], orientations[1], orientations[2]));
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}