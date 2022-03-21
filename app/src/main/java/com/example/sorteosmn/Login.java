package com.example.sorteosmn;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Login extends AppCompatActivity {
//comentario random

 Button show;
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        text = (TextView) findViewById(R.id.tvConexion);

        String error="";
        try {
            Class.forName("com.mysql.jdbc.Driver");

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/proyecto_smn_isw", "root", "12345678");

            Statement statement = connection.createStatement();

            text.setText("Conexion: Establecida");

        }
        catch(Exception e) { error = e.toString();  text.setText("Conexion Fallidad"+error);}


    }

    class Async extends AsyncTask<Void, Void, Void> {
        String error="";
        @Override

        protected Void doInBackground(Void... voids) {


            return null;
        }

//------------------------------------------------------------------------------------


    }

    //========================================================================
    public void pasaV2 (View view) {
        Intent v1 =new Intent(this,MainActivity.class);
        startActivity(v1);
    }
}