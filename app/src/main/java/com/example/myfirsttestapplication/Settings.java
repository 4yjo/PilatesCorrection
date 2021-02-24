package com.example.myfirsttestapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.myfirsttestapplication.MainActivity.PORT;
import static com.example.myfirsttestapplication.MainActivity.mServerIP;

public class Settings extends AppCompatActivity {
    /* enables user to change IP Address & Port to connect to server */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        /* creates menu*/
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){switch(item.getItemId()){
        /* actions for menu items*/

        case R.id.home:
            Intent intentHome = new Intent (this, MainActivity.class);
            startActivity(intentHome);
            break;
        case R.id.about:
            Intent intentAbout = new Intent(this, About.class);
            startActivity(intentAbout);
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

        EditText userInputIp = findViewById(R.id.userInputIp);
        mServerIP = userInputIp.getText().toString();
        EditText userInputPort = findViewById(R.id.userInputPort);
        PORT = Integer.parseInt(userInputPort.getText().toString());

        Log.d("XXX", "New IP set to: " + mServerIP);
        Log.d("XXX", "New Port set to: " + PORT);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        MainActivity.sendingData = true;
    }
}