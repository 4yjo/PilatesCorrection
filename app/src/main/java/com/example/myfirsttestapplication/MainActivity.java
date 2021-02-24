package com.example.myfirsttestapplication;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager; //create instance of sensor manager system service following https://developer.android.com/codelabs/advanced-android-training-sensor-data#2
    private Sensor mAccelerometer;
    private TextView mAccelerometerText;
    private Vibrator mVibrator;

    public static boolean sendingData; //value to check if user wants to set data. set true in startSendingData()

    public static String mServerIP = "192.168.0.200."; // defaults to IP Address of Laptop that runs server.pde
                                                        // may be changed by user in settings of the app
     public static int PORT = 12345;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //create view
        setContentView(R.layout.activity_main);

        //get Sensor Information from Phone
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        String sensor_error = getResources().getString(R.string.error_no_sensor);
        mAccelerometerText = findViewById(R.id.label_Accelerometer);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (mAccelerometer == null){
            mAccelerometerText.setText(sensor_error);
        }

        //connect to device vibrator
        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        /*creates Menu*/
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){switch(item.getItemId()){
        /*create actions for each menu item*/

        case R.id.home:
            break;
        case R.id.about:
            Intent intentAbout = new Intent(this, About.class);
            startActivity(intentAbout);
            Log.d("XXX", "About selected");
            break;
        case R.id.settings:
            Intent intentSettings = new Intent(this, Settings.class);
            startActivity(intentSettings);
            Log.d("XXX", "Settings selected");
            break;
        }
        return(super.onOptionsItemSelected(item));
    }

    public void showSensorList(View view){
        /*displays a list of all available Sensors of the device*/

        //can be accessed via button click but is not set now
        Intent intent = new Intent (this, listSensors.class);
        startActivity(intent);
    }


    public void startSendingData(View view){
        /*change sendingData, connects to Server by calling streamSensorData class, also toggles button */

        //change sendingData to true, so vibrator gets activated in onSensorChanged
        sendingData=true;

       changeView();

        //create connection to server and send Accelerometer Data in real time when "Let's go" is clicked
        Intent intent = new Intent(this, streamSensorData.class);
        startService(intent);
    }

    public void stopSendingData() {
        /* cancels connection to Server and sets sendingData to false to stop Vibrator, also toggle button*/
            if (sendingData) {
                //stop streamSensorData
                Intent intent = new Intent(getApplicationContext(), streamSensorData.class);
                stopService(intent);
                sendingData = false;
            }

          changeView();
    }

    public void changeView(){
        //empty Layout Container that holds "Let's go" or "Pause" Button
        LinearLayout myLayout = findViewById(R.id.buttonHolder);
        if (null!= myLayout){
            myLayout.removeAllViews();
        }

        if (sendingData){
            // Create Pause Button
            Button myButtonPause = new Button(this);
            myButtonPause.setText(R.string.pause);
            myButtonPause.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            myButtonPause.setOnClickListener(view1 -> stopSendingData());

            // Add Button to LinearLayout
            if (myLayout != null) {
                myLayout.addView(myButtonPause);
            }

            // Show Accelerometer Data in Real Time
            mAccelerometerText.setVisibility(View.VISIBLE);
        }
        else{
            // Create Lets Go Button
            Button myButtonStart = new Button(this);
            myButtonStart.setText(R.string.start);
            myButtonStart.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            myButtonStart.setOnClickListener(this::startSendingData);

            // Add Button to LinearLayout
            if (myLayout != null) {
                myLayout.addView(myButtonStart);
            }


            mAccelerometerText.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onStart() {
        /*register Listener for Accelerometer*/
        super.onStart();
        if (mAccelerometer != null){
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
        //mAccelerometerText.setVisibility(View.INVISIBLE);
        changeView();
    }



    @Override
    protected void onStop(){
        /*unregister Listener for Accelerometer*/
        super.onStop();
        mSensorManager.unregisterListener(this); // unregisters all registered Listeners
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        /* implement haptic feedback through vibrator */

        //get Values form Accelerometer: 0 = X-Axis, 1 = Y-Axis, 2 = Z-Axis
        float currentValueX = sensorEvent.values[0];
        float currentValueY = sensorEvent.values[1];


        if(sendingData) {
        // only check sensordata if user clicked "Let's go" button
            if (mAccelerometerText != null) { //TODO Check if redundant
                mAccelerometerText.setText(getResources().getString(R.string.label_Accelerometer,
                        currentValueX, currentValueY));
            }

            if (currentValueX > 1.5) {
                // TODO: test what value is good (maybe > 2?)


                // TODO: make this if else somewhere else so it just checks version once
                if (Build.VERSION.SDK_INT >= 26) {
                    mVibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    mVibrator.vibrate(50);
                }

            } else if (currentValueX < -1.5) {

                mVibrator.vibrate(50);
            } else {
                // do not vibrate
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}