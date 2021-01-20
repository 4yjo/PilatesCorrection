package com.example.myfirsttestapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.material.button.MaterialButtonToggleGroup;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager; //create instance of sensor manager system service following https://developer.android.com/codelabs/advanced-android-training-sensor-data#2
    private Sensor mGyroscope;
    private TextView mGyroscopeText;
    private Sensor mOtherSensor;
    private TextView mOtherSensorText;
    private Button mTiltLeft;
    private Button mTiltFront;
    private Button mTiltRight;
    private Vibrator mVibrator;



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
       // mOtherSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        mOtherSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mTiltLeft = (Button) findViewById(R.id.button2);
        mTiltFront = (Button) findViewById(R.id.button3);
        mTiltRight = (Button) findViewById(R.id.button4);
        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        if (mGyroscope == null){
            mGyroscopeText.setText(sensor_error);
        }

        if (mOtherSensor == null){
            mOtherSensorText.setText(sensor_error);
        }


        // connect to Server using Async Task declared in class "connectToServer.java"
        new connectToServer().execute();
    }

    public void showSensorList(View view){
        Log.d("AAA", "Button pressed");
        //called on Button Click show Sensors
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

        float currentValueX = sensorEvent.values[0]; // 0 = X-Axis, 1 = Y-Axis, 2 = Z-Axis
        float currentValueY = sensorEvent.values[1];
        float currentValueZ = sensorEvent.values[2];

        switch (sensorType) {
            case Sensor.TYPE_GYROSCOPE:
                mGyroscopeText.setText(getResources().getString(R.string.label_gyroscope,
                        currentValueX, currentValueY, currentValueZ));
                break;

          /*  case Sensor.TYPE_ROTATION_VECTOR:  //following https://code.tutsplus.com/tutorials/android-sensors-in-depth-proximity-and-gyroscope--cms-28084
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
                break;*/

            case Sensor.TYPE_ACCELEROMETER:
                mOtherSensorText.setText(getResources().getString(R.string.label_gyroscope,
                        currentValueX, currentValueY, currentValueZ));

                if (currentValueX  > 1.5) {
                    // TODO: test what value is good (maybe > 2?)
                    mTiltFront.setBackgroundColor(Color.GREEN);
                    mVibrator.vibrate(50);

                }
                else if (currentValueX <-1.5){
                    mTiltFront.setBackgroundColor(Color.BLUE);
                    mVibrator.vibrate(50);
                }
                else{
                    mTiltFront.setBackgroundColor(Color.YELLOW);
                }

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