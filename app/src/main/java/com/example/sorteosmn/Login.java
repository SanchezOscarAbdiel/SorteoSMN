package com.example.sorteosmn;

import  androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Login extends AppCompatActivity{

//------------------------------------------------------------------------------
    RequestQueue requestQueue; //llamada a php

    private static final String url = "http://localhost:3306/android.save.php"; //manda a llamar dicho script de php

    EditText correo, matricula;
   public static String Correo,Matricula,tipoA;
   public static int desvio;
// ------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        correo = (EditText) findViewById(R.id.id_user);
        matricula =  (EditText) findViewById(R.id.id_contra);



    }
//http://localhost/android/fetchlogin.php?matricula=D123691&correo=qwe

    String z;
    public void readUser (View view){
        Correo = correo.getText().toString();
        Matricula = matricula.getText().toString();
        desvio = 1;
        String URL ="http://192.168.56.1/android/fetchlogin.php?matricula="+Matricula+"&correo="+Correo; //toma ambos campos de texto y los inserta en una consulta para validar su existencia en BD
        System.out.println("url "+URL);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int x = 0; x < response.length(); x++) {
                    try {
                        jsonObject = response.getJSONObject(x);

                        z = response.toString(); // response es el resultado de la consulta
                        if( ((z.charAt(7)) == ('D')) && (z.charAt(8)) == ('-')){  pasaV3(); tipoA="Encuadrado";} //revisa si la respuesta tiene un "D-" para encuadrado
                        else if( ((z.charAt(7)) == ('D')) && (z.charAt(8)) != ('-')){pasaV3(); tipoA="Reserva";}
                        else if( ((z.charAt(7)) == ('A'))){pasaV4(); tipoA="Admin";}

                    } catch (JSONException e) {
                        System.out.println("Error1 " + e.getMessage());
                        Toast.makeText(Login.this, "Revise sus datos.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error2 " + error.getMessage());
            }
        }
        );
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    //========================================================================PASA DE PANTALLA
    public void pasaV2 (View view) {
        desvio = 2;
        Intent v1 =new Intent(this,Registro.class);
        startActivity(v1);
    }

    public void pasaV3 () {
        Intent v1 =new Intent(this,MainActivity.class);
        startActivity(v1);
    }
    public void pasaV4 () {
        Intent v1 =new Intent(this,panelControl.class);
        startActivity(v1);
    }
}