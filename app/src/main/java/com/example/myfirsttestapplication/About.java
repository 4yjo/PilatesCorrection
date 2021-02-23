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

        case R.id.exit:
            //close app
            this.finishAffinity();
        case R.id.about:
            Intent intentAbout = new Intent(this, About.class);
            startActivity(intentAbout);
        case R.id.settings:
            Intent intentSettings = new Intent(this, Settings.class);
            startActivity(intentSettings);
    }
        return(super.onOptionsItemSelected(item));
    }
}