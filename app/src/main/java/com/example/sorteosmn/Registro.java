package com.example.sorteosmn;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class Registro extends AppCompatActivity {



    //------------------------------------------------------------------------------
    RequestQueue requestQueue;
    private static final String url = "http://localhost:3306/android.save.php";
// ------------------------------------------------------------------------------
    private Spinner spinner;
    public Spinner spEstadoc;
    TextView estado;
// ------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);



    }

    // ------------------------------------------------------------------------------
    public void llenaSpin() throws IOException{

        List<String> spin = new ArrayList<String>();
        String linea;

        InputStream is =this.getResources().openRawResource(R.raw.colonias);
        BufferedReader reader =new BufferedReader(new InputStreamReader(is));
        if (is!=null){
            while ((linea=reader.readLine())!=null){
                spin.add(linea.split("")[0]);
            }
        }
        is.close();

        String datos[] = spin.toArray(new String[spin.size()]);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, datos);
        spinner.setAdapter(adapter);

    }
    // ------------------------------------------------------------------------------
    private void createUser(final String name){

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Registro.this, "correct", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("", name);//variables min 18
                return params;
            }
        };

    }
    // ------------------------------------------------------------------------------
    public void pasaV6 (View view) {
        Intent v1 =new Intent(this,sorteo.class);
        startActivity(v1);
    }
    public void spinnercolonia()throws IOException{////////////Revisa que si funcione el spinner de colonia /////////////
        requestQueue = Volley.newRequestQueue(this);
        Spinner spinner = (Spinner) findViewById(R.id.spColonia);

        List<String> spin = new ArrayList<String>();
        String linea = null;

        InputStream is =this.getResources().openRawResource(R.raw.colonias);
        BufferedReader reader =new BufferedReader(new InputStreamReader(is));
        if (is!=null){
            while (true){
                try {
                    if (!((linea=reader.readLine())!=null)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                spin.add(linea.split(",")[0]);
            }
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String datos[] = spin.toArray(new String[spin.size()]);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, datos);
        spinner.setAdapter(adapter);
    }
    public void spinnerestado()throws IOException{
        spEstadoc=(Spinner) findViewById(R.id.spEstadoc);
        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this,R.array.combo_Estado, android.R.layout.simple_spinner_item);

        spEstadoc.setAdapter(adapter);
    }

}