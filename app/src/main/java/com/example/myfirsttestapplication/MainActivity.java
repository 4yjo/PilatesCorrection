package com.example.myfirsttestapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager; //create instance of sensor manager system service following https://developer.android.com/codelabs/advanced-android-training-sensor-data#2
    private Sensor mAccelerometer;
    private TextView mAccelerometerText;
    private Vibrator mVibrator;
    public String data;
    private Object streamSensorData;


    //TODO: onCreate send data all the time, also when app is not running in the foreground > better use onStart

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE); //get Sensor Information from Phone
        String sensor_error = getResources().getString(R.string.error_no_sensor);
        mAccelerometerText = (TextView) findViewById(R.id.label_Accelerometer);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);


        if (mAccelerometer == null){
            mAccelerometerText.setText(sensor_error);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //creates Menu
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){switch(item.getItemId()){
        case R.id.settings:
            //go to Settings page
            Intent intentSettings = new Intent(this, Settings.class);
            startActivity(intentSettings);
            return(true);
        case R.id.exit:
            //close app
            this.finishAffinity();
        case R.id.about:
            Intent intentAbout = new Intent(this, About.class);
            startActivity(intentAbout);
        }
        return(super.onOptionsItemSelected(item));
    }

    public void showSensorList(View view){
        Log.d("AAA", "Button pressed");
        //called on Button Click show Sensors
        Intent intent = new Intent (this, listSensors.class);
        startActivity(intent);
    }

    public void startSendingData(View view){
        //create connection to server and send Accelerometer Data in real time
        Intent intent = new Intent(getApplicationContext(), streamSensorData.class);
        startService(intent);
    }

   /* public void onClickSettings(View view){
        //create connection to server and send Accelerometer Data in real time
        Intent intent = new Intent(getApplicationContext(), Settings.class);
        startService(intent);
    }


    public void onClickAbout(View view){
        //create connection to server and send Accelerometer Data in real time
        Intent intent = new Intent(getApplicationContext(), About.class);
        startService(intent);

        //NavHostFragment.findNavController(SecondFragment.this)
        //                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
    }

    */
    @Override
    protected void onStart() {
        super.onStart();

        // register Listener for Accelerometer
        if (mAccelerometer != null){
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            mAccelerometerText = (TextView)findViewById(R.id.label_Accelerometer);
        }
    }



    @Override
    protected void onStop(){
        super.onStop();
        mSensorManager.unregisterListener(this); // unregisters all registered Listeners
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float currentValueX = sensorEvent.values[0]; // 0 = X-Axis, 1 = Y-Axis, 2 = Z-Axis
        float currentValueY = sensorEvent.values[1];
        float currentValueZ = sensorEvent.values[2];


            mAccelerometerText.setText(getResources().getString(R.string.label_Accelerometer,
                    currentValueX, currentValueY));

            if (currentValueX > 1.5) {
                // TODO: test what value is good (maybe > 2?)

                // TODO: check for Android API Level maybe in switch-case instead of Type Accelerometer, so it only has to be done once
                mVibrator.vibrate(50);

            } else if (currentValueX < -1.5) {

                mVibrator.vibrate(50);
            } else {
               // do not vibrate
            }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}