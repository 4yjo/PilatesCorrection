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
import android.widget.EditText;

public class Settings extends AppCompatActivity {
    public static final String EXTRA_TEXT= Settings.EXTRA_TEXT;
    public static final String EXTRA_NUMBER = Settings.EXTRA_NUMBER;

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



}