package com.example.sorteosmn;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class justificar extends AppCompatActivity {

    RequestQueue requestQueue; //Llamada a mysql
    Login objL = new Login();
    String Matricula = objL.Matricula,link;
    int contador;
    EditText E1,E2,E3,E4;
    Button enviar;
    TextView numero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_justificar);
        requestQueue = Volley.newRequestQueue(this); //llamada universal a php (Myslq)

        funa(null); // manda a llamar metodo para contar faltas

//-------------------------------------------------------------//ligaciones a visual
        E4 = (EditText) findViewById(R.id.Edoc4);
        link = E4.getText().toString();
        enviar = (Button) findViewById(R.id.benviar4);
        numero = (TextView) findViewById(R.id.tvNumeros);
        numero.setText(contador); //imprime numero de faltas

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(contador <5){ //permite o no insertar una nueva justificacion (maximo 4)
                    InsertaJ(null); //manda a llamar metodo de insercion
                    pasaMain(null); //sale a ventana principal
                }else{
                    Toast.makeText(justificar.this, "Se le han acabado"+"\n los intentos de justificacion", Toast.LENGTH_LONG).show();
                    pasaMain(null);
                }

            }
        });



    }

    public void funa(String xd){ // cuenta cuantas justificaciones existen en BD de dicho encuadrado
        String URL ="http://192.168.56.1/android/cuentaJ.php?Matricula_Enc="+Matricula;
        System.out.println("url "+URL);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int x = 0; x < response.length(); x++) {
                    try {
                        jsonObject = response.getJSONObject(x);

                        contador =Integer.parseInt(jsonObject.getString("Encuadrado"));

                        Toast.makeText(justificar.this, "Usted cuenta con "+contador+" justificaciones", Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        System.out.println("Error1 " + e.getMessage());
                        Toast.makeText(justificar.this, "Revise sus datos.", Toast.LENGTH_LONG).show();
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

    public void InsertaJ(String xd) { //hace insercion en BD del link de justificacion ingresado en el edit text
        String url = "http://192.168.56.1/android/saveJustifica.php?Matricula_Enc="+Matricula+"&L_DocJust="+link;

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(justificar.this, "Se ha mandado su link", Toast.LENGTH_LONG).show();
                        System.out.println("response " + response);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(justificar.this, "Revise sus datos.", Toast.LENGTH_LONG).show();
                        System.out.println("error " + error);
                    }
                }
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("Matricula_Enc", Matricula);
                params.put("L_DocJust", link);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void pasaMain (String xd) { //pasa de pantalla
        Intent v1 =new Intent(this,MainActivity.class);
        startActivity(v1);
    }
    public void pasaMain1 (View view) { //pasa de pantalla
        Intent v1 =new Intent(this,MainActivity.class);
        startActivity(v1);
    }
}