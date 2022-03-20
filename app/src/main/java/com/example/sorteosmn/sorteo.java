package com.example.sorteosmn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class sorteo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorteo);
    }
    public void pasaV7 (View view) {
        Intent v1 =new Intent(this,Login.class);
        startActivity(v1);
    }
}