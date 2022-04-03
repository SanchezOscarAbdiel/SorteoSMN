package com.example.sorteosmn;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class panelControl extends AppCompatActivity {

    Login objL = new Login();
    String admin = objL.Correo;
    EditText etAdmin, etTitulo,Cuerpo,destino;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_control);
        requestQueue = Volley.newRequestQueue(this);

        etAdmin = (EditText) findViewById(R.id.edadmin);
        etAdmin.setText(admin);

        Cuerpo = (EditText) findViewById(R.id.ed);
        etTitulo = (EditText) findViewById(R.id.edTituloN);
        destino = (EditText) findViewById(R.id.edDestinatario);


    }

    public void LeeNoticias(View view){ //consulta todas las noticias en BD y las lista
        String URL = "http://192.168.56.1/android/fetchnoticias.php";
        //System.out.println("CURP "+objR.EnviaCurp);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                System.out.println("response1: "+response);
                for (int x = 0; x < response.length(); x++) {
                    System.out.println("a ");
                    try {
                        jsonObject = response.getJSONObject(x);
                        etTitulo.setText(jsonObject.getString("titulo"));
                        Cuerpo.setText(jsonObject.getString("cuerpo"));

                    } catch (JSONException e) {
                        System.out.println("Error1 " + e.getMessage());
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

    public void popUpListado(View view){ //da a elegir que tipo de tabla de encuadrado o reserva dependiendo del boton elegido
        Cuerpo.setText("");
        etTitulo.setText(" ");//limpieza de variables
        AlertDialog.Builder alerta = new AlertDialog.Builder(panelControl.this);
        alerta.setMessage("Elija un tipo de adscrito a lista"+"\nNOTA: se mostraran resultados del año en curso").setCancelable(false)
                .setPositiveButton("Encuadrados", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                        LeeEncuadrado("encuadrado");
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Reservas", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        LeeEncuadrado("reserva");
                        dialog.cancel();
                    }
                });
            AlertDialog titulo = alerta.create();
            titulo.setTitle("Listado de adscritos");
            titulo.show();
    }

    String acumula;
    public void LeeEncuadrado(String tipo){ //enlista la tabla elegida en popUpListado y la imprime
        String URL = "http://192.168.56.1/android/fetchEncuadrado.php?tabla="+tipo;
        //System.out.println("CURP "+objR.EnviaCurp);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                System.out.println("response1: "+response);
                for (int x = 0; x < response.length(); x++) {
                    System.out.println("a ");
                    try {
                        jsonObject = response.getJSONObject(x);
                        etTitulo.setText("Listado de "+tipo);
                        acumula = jsonObject.getString("Matricula")+" "+jsonObject.getString("Nombre")+"\n";
                        Cuerpo.setText(Cuerpo.getText().toString() + acumula) ;

                    } catch (JSONException e) {
                        System.out.println("Error1 " + e.getMessage());
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

    String acumulaFaltas;
    public void LeeFaltas(View view){ //enlista e imprime la tabla donde se guardan las faltas de encuadrados
        Cuerpo.setText("");
        etTitulo.setText("");
        String URL = "http://192.168.56.1/android/fetchFaltas.php";
        //System.out.println("CURP "+objR.EnviaCurp);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                System.out.println("response1: "+response);
                for (int x = 0; x < response.length(); x++) {
                    System.out.println("a ");
                    try {
                        jsonObject = response.getJSONObject(x);
                        etTitulo.setText("Listado de encuadrados con faltas");
                        acumulaFaltas = jsonObject.getString("Matricula_Enc")+" "+jsonObject.getString("L_DocJust")+"\n";
                        Cuerpo.setText(Cuerpo.getText().toString() + acumulaFaltas) ;

                    } catch (JSONException e) {
                        System.out.println("Error1 " + e.getMessage());
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

    String MatriculaBaja;
    public void popUpBaja(View view){ //recibe una matricula para modificar su correo y asi restringir su acceso a la aplicacion
        Cuerpo.setText("");
        etTitulo.setText(" ");
        AlertDialog.Builder alerta = new AlertDialog.Builder(panelControl.this);

        final EditText input = new EditText(panelControl.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alerta.setView(input);

        alerta.setMessage("Escriba la matricula del ENCUADRADO"+"\nAl que se desea restringir el acceso").setCancelable(false)
                .setPositiveButton("Baja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        MatriculaBaja = input.getText().toString();
                        System.out.println("matricula Baja"+ MatriculaBaja);
                        updateBaja(MatriculaBaja);

                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                        dialog.cancel();
                    }
                });
        AlertDialog titulo = alerta.create();
        titulo.setTitle("Listado de adscritos");
        titulo.show();
    }

    private void updateBaja(String matricula) { //con la matricula recogida en popUpBaja se hace efectivo el cambio en la base de datos
        String URL ="http://192.168.56.1/android/editBaja.php";
        StringRequest stringRequest =new StringRequest(
                Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(panelControl.this,"SE HA RESTRINGIDO CORRECTAMENTE" ,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(panelControl.this,"REVISE LA MATRICULA" ,Toast.LENGTH_LONG).show();

                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params =new HashMap<>();
               params.put("matricula",matricula);


                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    String titulo, Mensaje, destinatario;
    public void InsertaNoticias(View view) { //agrega un nuevo comunicado en la base de datos
        titulo = etTitulo.getText().toString();
        Mensaje = Cuerpo.getText().toString();
        destinatario = destino.getText().toString();

        if(destinatario.equalsIgnoreCase("General") || destinatario.equalsIgnoreCase("Encuadrado") || destinatario.equalsIgnoreCase("Reserva")){
            String url = "http://192.168.56.1/android/saveNoticias.php?titulo="+titulo+"&cuerpo="+Mensaje+"&destinatario="+destinatario;

            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(panelControl.this, "SE HA PUBLICADO CORRECTAMENTE", Toast.LENGTH_LONG).show();
                            System.out.println("response " + response);


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(panelControl.this, "Revise sus datos.", Toast.LENGTH_LONG).show();
                            System.out.println("error " + error);
                        }
                    }
            ) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.put("titulo", titulo);
                    params.put("cuerpo", Mensaje);
                    params.put("destinatario", destinatario);

                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }else{
            Toast.makeText(panelControl.this,"REVISE EL TIPO DE DESTINATARIO" ,Toast.LENGTH_LONG).show();
        }

    }

    public void pasaV4 (View view) {//pase de pantalla
        Intent v1 =new Intent(this,Registro.class);
        startActivity(v1);
    }
}