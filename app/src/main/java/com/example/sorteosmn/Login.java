package com.example.sorteosmn;

import  androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Login extends AppCompatActivity{

//------------------------------------------------------------------------------
    RequestQueue requestQueue;

    private static final String url = "http://localhost:3306/android.save.php";
// ------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }


    //========================================================================
    public void pasaV2 (View view) {
        Intent v1 =new Intent(this,MainActivity.class);
        startActivity(v1);
    }
}