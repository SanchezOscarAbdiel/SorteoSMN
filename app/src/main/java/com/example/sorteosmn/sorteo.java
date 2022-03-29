package com.example.sorteosmn;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
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

public class sorteo extends AppCompatActivity {

    public static Registro objR = new Registro();
    public static Boolean res = objR.Reserva;
    public static EditText Matricula,NumLiberacion,Nombre,Apellidop,Apellidom,Resultado,TipoA;
    public static String ConsultaMatricula;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorteo);

        requestQueue = Volley.newRequestQueue(this);

        System.out.println("res 1 "+res);
        if(res=true){
            System.out.println("res 2 "+res);
            PopUpReserva();
        }

        Matricula = (EditText)findViewById(R.id.etMatricula);
        NumLiberacion = (EditText)findViewById(R.id.etLiberacion);
        Nombre = (EditText)findViewById(R.id.etNombreS);
        Apellidop = (EditText)findViewById(R.id.etApellidop);
        Apellidom = (EditText)findViewById(R.id.etApellidom);
        TipoA = (EditText)findViewById(R.id.etTipoA);
        Resultado =(EditText)findViewById(R.id.etResultado);

        readUser1(null); //muestra los datos en los campos
        System.out.println("set bola "+objR.EnviaBola);
        Resultado.setText("Bola "+objR.EnviaBola);
        if(objR.EnviaBola == "Blanca"){TipoA.setText("Encuadrado");}else{TipoA.setText("Reserva");}
    }

    //====================================================

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText telefono;public String Num_Tel;
    public Button aceptar;
    public Switch captcha;

    public void PopUpReserva(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup,null);

        telefono = (EditText) contactPopupView.findViewById(R.id.etCelular);
        aceptar = (Button) contactPopupView.findViewById(R.id.btnAceptar);
        captcha = (Switch) contactPopupView.findViewById(R.id.swCaptcha);

        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Num_Tel = telefono.getText().toString();
                System.out.println("telefono "+Num_Tel);
                updateUser(null);
                dialog.dismiss(); //cierra la ventana


            }
        });

    }

    private void updateUser(String xd) {
        String URL ="http://192.168.56.1/android/edit.php";
        StringRequest stringRequest =new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(sorteo.this,"SE HA ACTUALIZADO EL NUMERO DE CELULAR" ,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params =new HashMap<>();
                params.put("Num_Tel",Num_Tel);
                params.put("CURP_Res",objR.EnviaCurp);//

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    public void readUser1 (String xd) {
        String URL = "http://192.168.56.1/android/fetchsorteo.php?curp=" + objR.EnviaCurp;
        System.out.println("CURP "+objR.EnviaCurp);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                System.out.println("response1: "+response);
                for (int x = 0; x < response.length(); x++) {
                    System.out.println("a ");
                    try {
                        jsonObject = response.getJSONObject(x);
                        Matricula.setText(jsonObject.getString("Matricula_Enc"));
                        Nombre.setText(jsonObject.getString("Nombres_Enc"));
                        Apellidop.setText(jsonObject.getString("ApellidoPat_Enc"));
                        Apellidom.setText(jsonObject.getString("ApellidoMat_Enc"));
                        ConsultaMatricula = jsonObject.getString("Matricula_Enc");
                        System.out.println("matricula1: "+ConsultaMatricula);

                        readUser2 (ConsultaMatricula);


                    } catch (JSONException e) {
                        System.out.println("Error1 " + e.getMessage());
                    }
                    try {
                        jsonObject = response.getJSONObject(x);
                        Matricula.setText(jsonObject.getString("Matricula_Res"));
                        Nombre.setText(jsonObject.getString("Nombres_Res"));
                        Apellidop.setText(jsonObject.getString("ApellidoPat_Res"));
                        Apellidom.setText(jsonObject.getString("ApellidoMat_Res"));

                        System.out.println("matricula2: "+jsonObject.getString("Matricula_Res"));

                    } catch (JSONException e) {
                        System.out.println("Error2 " + e.getMessage());
                    }
                }
                readUser2 (ConsultaMatricula);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error2 " + error.getMessage());
            }
        }
        );
        requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    public void readUser2 (String mat) {
        String URL = "http://192.168.56.1/android/fetchsorteo2.php?matricula=" + mat;
        System.out.println("CURP "+mat);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                System.out.println("response1: "+response);
                for (int x = 0; x < response.length(); x++) {
                    System.out.println("a ");
                    try {
                        jsonObject = response.getJSONObject(x);
                        NumLiberacion.setText(jsonObject.getString("Num_Liberacion"));

                        System.out.println("liberacion1: ");

                    } catch (JSONException e) {
                        System.out.println("Error1 " + e.getMessage());
                    }
                    try {
                        jsonObject = response.getJSONObject(x);
                        NumLiberacion.setText(jsonObject.getString("Num_Liberacion"));

                        System.out.println("liberacion2: ");

                    } catch (JSONException e) {
                        System.out.println("Error2 " + e.getMessage());
                    }
                }
                //String setBola =objR.EnviaBola;

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error2 " + error.getMessage());
            }
        }
        );
        requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

        public void pasaV7 (View view) {
        Intent v1 =new Intent(this,Login.class);
        startActivity(v1);
    }
}


