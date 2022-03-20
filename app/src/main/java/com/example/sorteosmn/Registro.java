package com.example.sorteosmn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Registro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
    }
    public void pasaV6 (View view) {
        Intent v1 =new Intent(this,sorteo.class);
        startActivity(v1);
    }
}