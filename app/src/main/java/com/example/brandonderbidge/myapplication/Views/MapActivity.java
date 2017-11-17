package com.example.brandonderbidge.myapplication.Views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.brandonderbidge.myapplication.R;

public class MapActivity extends AppCompatActivity {

    private FragmentManager fm = this.getSupportFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Fragment mapFragment = fm.findFragmentById(R.id.map_frag);
        fm.beginTransaction().add(R.id.map_frag, mapFragment).commit();
    }

}
