package com.example.myfirsttestapplication;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static com.example.myfirsttestapplication.MainActivity.PORT;
import static com.example.myfirsttestapplication.MainActivity.mServerIP;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActionBar actionBar = getSupportActionBar();
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
            Intent intentHome = new Intent (this, MainActivity.class);
            Log.d("XXX", "Home selected");
            startActivity(intentHome);
            break;
        case R.id.about:
            Intent intentAbout = new Intent(this, About.class);
            startActivity(intentAbout);
            Log.d("XXX", "About selected");
            break;
        case R.id.settings:
            break;
    }
        return(super.onOptionsItemSelected(item));
    }

    public void onClickSubmit(View view){
        /* get input data and give it to streamSensorData */
        Log.d("XXX","button pressed");

        if (MainActivity.sendingData){
            //stop streamSensorData
            Intent intent = new Intent(getApplicationContext(), streamSensorData.class);
            stopService(intent);
            MainActivity.sendingData = false;
        }

        EditText userInputIp = (EditText) findViewById(R.id.userInputIp);
        mServerIP = userInputIp.getText().toString();
        EditText userInputPort = (EditText)findViewById(R.id.userInputPort);
        //int theNewPort = Integer.parseInt(userInputPort.getText().toString());
        PORT = Integer.parseInt(userInputPort.getText().toString());

        Log.d("XXX", "New IP set to: " + mServerIP);
        Log.d("XXX", "New Port set to: " + PORT);

        Intent intent = new Intent(getApplicationContext(), streamSensorData.class);
        startService(intent);
        MainActivity.sendingData = true;

    }
}