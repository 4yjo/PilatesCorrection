package com.example.myfirsttestapplication;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

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
        case R.id.settings:
            //go to Settings page
            return(true);
        case R.id.exit:
            //close app
            this.finishAffinity();
        case R.id.about:
            //go to About page
    }
        return(super.onOptionsItemSelected(item));
    }


    public void onClickMain(View view){
        //create connection to server and send Accelerometer Data in real time
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startService(intent);
    }


    public void onClickAbout(View view){
        //create connection to server and send Accelerometer Data in real time
        Intent intent = new Intent(getApplicationContext(), About.class);
        startService(intent);

        //NavHostFragment.findNavController(SecondFragment.this)
        //                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
    }

}