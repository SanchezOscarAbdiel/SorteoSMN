package com.example.sorteosmn;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


import android.os.Bundle;

public class justificar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_justificar);


    }
    public void pasaV4 (View view) {
        Intent v1 =new Intent(this,panelControl.class);
        startActivity(v1);
    }
}