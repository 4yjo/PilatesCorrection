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

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mSensorManager; //create instance of sensor manager system service following https://developer.android.com/codelabs/advanced-android-training-sensor-data#2
    private Sensor mAccelerometer;
    private TextView mAccelerometerText;
    private Vibrator mVibrator;

    public static boolean sendingData;

    public static String mServerIP = "192.168.0.200."; // defaults to IP Address of Laptop that runs server.pde
                                                      // may be changed by user in settings of the app
    public static int PORT = 12345;



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
        Log.d("AAA", "Button pressed");
        //called on Button Click show Sensors
        Intent intent = new Intent (this, listSensors.class);
        startActivity(intent);
    }

    public void onClickSubmit(View view){
        /* get input data and give it to streamSensorData */
        Log.d("XXX","button pressed");

        stopSendingData();

        EditText userInputIp = (EditText) findViewById(R.id.userInputIp);
        mServerIP = userInputIp.getText().toString();
        EditText userInputPort = (EditText)findViewById(R.id.userInputPort);
        //int theNewPort = Integer.parseInt(userInputPort.getText().toString());
        PORT = Integer.parseInt(userInputPort.getText().toString());

        Log.d("XXX", "New IP set to: " + mServerIP);
        Log.d("XXX", "New Port set to: " + PORT);

        Intent intent = new Intent(getApplicationContext(), streamSensorData.class);
        startService(intent);

    }

    public void startSendingData(View view){
        //create connection to server and send Accelerometer Data in real time when "Los geht's" is clicked

        //TODO: show connection status // toast if connection failed

        //Remove Button "Los geht's" and replace it with Button "Pause"

        LinearLayout myLayout = findViewById(R.id.buttonHolder);

        // remove Let's Go Button
        Button myButtonStart = (Button)findViewById(R.id.letsGoButton);
        //ViewGroup layout = (ViewGroup) myButton.getParent();
        if (null!= myLayout){
            myLayout.removeAllViews();
        }


        // Create Pause Button
        Button myButtonPause = new Button(this);
        myButtonPause.setText(R.string.pause);
        myButtonPause.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        myButtonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               stopSendingData();
            }
        });

        // Add Button to LinearLayout
        if (myLayout != null) {
            myLayout.addView(myButtonPause);
        }

        // Create Text View to display Accelerometer Data in Real Time

        mAccelerometerText.setText(R.string.label_Accelerometer);



        sendingData=true;
        Intent intent = new Intent(getApplicationContext(), streamSensorData.class);
        startService(intent);
    }

    public void stopSendingData(){
        if (sendingData){
            //stop streamSensorData
            Intent intent = new Intent(getApplicationContext(), streamSensorData.class);
            stopService(intent);
            sendingData = false;
        }

        //Remove Button "Pause" and replace it with Button "Los geht's"

        LinearLayout myLayout = findViewById(R.id.buttonHolder);

        // remove Pause Button & Text View
        if (myLayout != null){
                myLayout.removeAllViews();
        }

        // Create Lets Go Button
        Button myButtonStart = new Button(this);
        myButtonStart.setText(R.string.start);
        myButtonStart.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        myButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSendingData(view);
            }
        });

        // Add Button to LinearLayout
        if (myLayout != null) {
            myLayout.addView(myButtonStart);
        }

        mAccelerometerText.setText(R.string.label_None);
    }


    @Override
    protected void onStart() {
        super.onStart();
        //TODO can this be moved to startSendingData?

        // register Listener for Accelerometer
        if (mAccelerometer != null){
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
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
       // float currentValueZ = sensorEvent.values[2];

            if(mAccelerometerText != null) {
                mAccelerometerText.setText(getResources().getString(R.string.label_Accelerometer,
                        currentValueX, currentValueY));
            }

            if (currentValueX > 1.5) {
                // TODO: test what value is good (maybe > 2?)


                // TODO: make this if else somewhere else so it just checks version once
                if (Build.VERSION.SDK_INT >= 26) {
                    mVibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                }
                else {
                    mVibrator.vibrate(50);
                }

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