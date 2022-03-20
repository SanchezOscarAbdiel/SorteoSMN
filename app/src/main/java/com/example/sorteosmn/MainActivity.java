package com.example.sorteosmn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
    public void pasaV3 (View view) {
        Intent v1 =new Intent(this,justificar.class);
        startActivity(v1);
    }
}