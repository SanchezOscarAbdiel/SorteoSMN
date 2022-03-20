package com.example.sorteosmn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class panelControl extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_control);
    }
    public void pasaV4 (View view) {
        Intent v1 =new Intent(this,Registro.class);
        startActivity(v1);
    }
}