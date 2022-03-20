package com.example.sorteosmn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void pasaV2 (View view) {
        Intent v1 =new Intent(this,MainActivity.class);
        startActivity(v1);
    }
}