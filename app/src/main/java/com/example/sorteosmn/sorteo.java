package com.example.sorteosmn;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class sorteo extends AppCompatActivity {

    public static Registro objR = new Registro();
    public static Boolean res = objR.Reserva;
    public static EditText Matricula,NumLiberacion,Nombre,Apellidop,Apellidom,Resultado,TipoA;
    public static String ConsultaMatricula;
    RequestQueue requestQueue;
    //-----------------------txt
    public FloatingActionButton btGuarda;
    public Button btLee;
    Login objL = new Login();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorteo);

        requestQueue = Volley.newRequestQueue(this);


        if(objR.EnviaBola == "Negra"){ //dependiendo del tipo de bola muestra una pantalla u otra para un ingreso de datos correspondiente
            System.out.println("res 2 "+res);
            PopUpReserva();
        }else{
            PopUpEncuadrado();
        }

        Matricula = (EditText)findViewById(R.id.etMatricula);
        NumLiberacion = (EditText)findViewById(R.id.etLiberacion);
        Nombre = (EditText)findViewById(R.id.etNombreS);
        Apellidop = (EditText)findViewById(R.id.etApellidop);
        Apellidom = (EditText)findViewById(R.id.etApellidom);
        TipoA = (EditText)findViewById(R.id.etTipoA);
        Resultado =(EditText)findViewById(R.id.etResultado);

        readUser1(null); //muestra los datos en los campos

        Resultado.setText("Bola "+objR.EnviaBola);//muestra resultados en pantalla
        if(objR.EnviaBola == "Blanca"){TipoA.setText("Encuadrado");}else{TipoA.setText("Reserva");}
        ///------------------txt

        btGuarda = findViewById(R.id.btSave);
        btGuarda.setOnClickListener(new View.OnClickListener() {//guarda la matricula (contraseña de inicio de sesion) en el portapapeles del dispositivo y abre la pantalla de inicio de sesion
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("EditText", Matricula.getText().toString());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(sorteo.this, "TU CONTRASEÑA (MATRICULA)"+"\nHa sido copiada al portapapeles.",Toast.LENGTH_LONG).show();

                    pasaV8(null);

            }
        });
    }



    //====================================================

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText telefono;public String Num_Tel;
    public Button aceptar;
    public Switch captcha;

    public void PopUpReserva(){ //muestra una pantalla emergente con campos de texto correspondientes a la actualizacion de datos del reserva
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

    private EditText peso,altura,sangre;public String TipoSangre,EnviaPeso,EnviaAltura; int Peso; Double Altura;
    public Button aceptar1;
    public void PopUpEncuadrado(){//muestra una pantalla emergente con campos de texto correspondientes a la actualizacion de datos del encuadrado
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popupenc,null);

        peso = (EditText) contactPopupView.findViewById(R.id.etPesoEnc);
        altura = (EditText) contactPopupView.findViewById(R.id.etAlturaEnc);
        sangre = (EditText) contactPopupView.findViewById(R.id.etSangre);
        aceptar1 = (Button) contactPopupView.findViewById(R.id.btAceptaEnc);

        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        aceptar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Peso = Integer.parseInt(peso.getText().toString()) ;
                Altura =Double.parseDouble(altura.getText().toString()) ;
                TipoSangre = sangre.getText().toString();

                //revisa que los datos ingresados sean correctos
                if(TipoSangre.equalsIgnoreCase("A+") ||TipoSangre.equalsIgnoreCase("A-")
                        ||TipoSangre.equalsIgnoreCase("B+") ||TipoSangre.equalsIgnoreCase("B-")
                        ||TipoSangre.equalsIgnoreCase("O+") ||TipoSangre.equalsIgnoreCase("O-")
                        ||TipoSangre.equalsIgnoreCase("AB-") ||TipoSangre.equalsIgnoreCase("AB+")
                        && (Altura>1.3 && Altura<2.1) && (Peso>40 && Peso <180)){
                    System.out.println("sangre "+TipoSangre+"Peso "+Peso+" Altura "+Altura);

                    EnviaAltura = String.valueOf(Altura);
                    EnviaPeso = String.valueOf(Peso);
                    updateUserE();
                    dialog.dismiss(); //cierra la ventana
                }else {
                    System.out.println("sangre "+TipoSangre+"Peso "+Peso+" Altura "+Altura);
                    Toast.makeText(sorteo.this,"REVISA TUS DATOS" ,Toast.LENGTH_LONG).show();
                }


            }
        });

    }

    public void updateUserE(){ //si los datos de PopUpEncuadrado son correctos, en este metodo se manda a llamar al llenado en la base de datos
        String URL ="http://192.168.56.1/android/editE.php";
        StringRequest stringRequest =new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(sorteo.this,"SE HAN ACTUALIZADO LOS DATOS" ,Toast.LENGTH_LONG).show();
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
                params.put("TipoSangre",TipoSangre);
                params.put("Peso",EnviaPeso);
                params.put("Altura",EnviaAltura);
                params.put("CURP_Enc",objR.EnviaCurp);//

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void updateUser(String xd) {// toma los datos de PopUpReserva y los ingresa en la base de datos
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
                params.put("CURP_Res",objR.EnviaCurp);// define a que reserva actualizar

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    public void readUser1 (String xd) { //recoge los datos de la base de datos para su muestreo
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
                        ConsultaMatricula = jsonObject.getString("Matricula_Res");
                        readUser2 (ConsultaMatricula);
                    } catch (JSONException e) {
                        System.out.println("Error2 " + e.getMessage());
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
        requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    public void readUser2 (String mat) {//metodo de complementacion de readUser2 para datos faltantes
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

    public void pasaV8 (String xd) {
        Intent v1 =new Intent(this,Login.class);
        startActivity(v1);
    }
    public void pasaV9 (String xd) {
        Intent v1 =new Intent(this,MainActivity.class);
        startActivity(v1);
    }

}