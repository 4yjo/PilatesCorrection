package com.example.myfirsttestapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

public class About extends AppCompatActivity {
    /* Displays Text about this App */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
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
            startActivity(intentHome);
            break;
        case R.id.about:
            break;
        case R.id.settings:
            Intent intentSettings = new Intent(this, Settings.class);
            startActivity(intentSettings);
            break;
    }
        return(super.onOptionsItemSelected(item));
    }
}