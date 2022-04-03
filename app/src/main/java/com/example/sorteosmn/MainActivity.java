package com.example.sorteosmn;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    Login objL = new Login();
    String matricula = objL.Matricula,CURP,Nombres,ApellidoPat,ApellidoMat,Correo,Num_Tel;
    String Num_Placa,Num_Seccion,Num_Liberacion,Fecha_Recepcion,Nombre_Ins,ApellidoPat_Ins,ApellidoMat_Ins;
    int enseña;
    Button Bperfil,Bescuadron,Bnoticias;
    TextView Usuario, tipo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this); //LLAMADA UNIVERSAL A PHP

        Usuario = (TextView) findViewById(R.id.tvTipoP);
        tipo = (TextView) findViewById(R.id.tvTipoP2);

        Usuario.setText(objL.Correo);
        tipo.setText(objL.tipoA);

        consultaUsuario(null); //manda a llamar a metodo

        Bperfil = (Button) findViewById(R.id.bperfil);
        botonPerfil(null);

        Bescuadron = (Button) findViewById(R.id.bescuadron);
        botonEscuadron(null);

        Bnoticias = (Button) findViewById(R.id.bnoticias);
        botonNoticias(null);
    }

    public void botonPerfil(String xd){ //decide si muestra pantalla de informacion de Encuadrado o de Reserva
        Bperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(enseña == 1){PopUpE(null);}else if(enseña == 2){PopUpR(null);};

            }
        });
    }

    public void botonEscuadron(String xd){ //manda a llamar popUp de informacion
        Bescuadron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            consultaEscuadronE(null);

            }
        });
    }
    public void botonNoticias(String xd){ //manda a llamar guardado de noticias en BD y su muestreo en popUp
        Bnoticias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LeeEncuadrado(null);
                popUpNoticias(total);
            }
        });
    }
//==========================================================
    public void consultaUsuario (String xd) { //recoge informacion del usuario en BD
        String URL = "http://192.168.56.1/android/fetchMainA.php?matricula=" + objL.Matricula;
        System.out.println("CURP "+objL.Matricula);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                System.out.println("response1: "+response);
                for (int x = 0; x < response.length(); x++) {
                    System.out.println("a ");
                    try {
                        jsonObject = response.getJSONObject(x); //informacion de encuadrado
                        CURP = (jsonObject.getString("CURP_Enc"));
                        Nombres = (jsonObject.getString("Nombres_Enc"));
                        ApellidoPat = (jsonObject.getString("ApellidoPat_Enc"));
                        ApellidoMat = (jsonObject.getString("ApellidoMat_Enc"));
                        Correo = (jsonObject.getString("Correo_Enc"));
                        enseña = 1;

                        //readUser2 ( objL.Matricula);

                    } catch (JSONException e) {
                        System.out.println("Error1 " + e.getMessage());
                    }
                    try {
                        jsonObject = response.getJSONObject(x); //informacion de reserva
                        CURP=(jsonObject.getString("CURP_Res"));
                        Nombres=(jsonObject.getString("Nombres_Res"));
                        ApellidoPat=(jsonObject.getString("ApellidoPat_Res"));
                        ApellidoMat=(jsonObject.getString("ApellidoMat_Res"));
                        Correo = (jsonObject.getString("Correo_Res"));
                        Num_Tel = (jsonObject.getString("Num_Tel"));
                        enseña = 2;

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
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    public void PopUpE(String bola){ //muestreo de informacion de encuadrado
        AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
        alerta.setMessage("DATOS DEL ENCUADRADO: "+matricula+"\nNombre: "+Nombres
                                                        +"\nApellidos: "+ApellidoPat+" "+ApellidoMat
                                                        +"\nCorreo: "+Correo
                                                        +"\nCURP: "+CURP).setCancelable(false)
                .setNeutralButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                        dialog.dismiss();
                    }
                });

        AlertDialog titulo = alerta.create();
        titulo.setTitle("PERFIL DE USUARIO");
        titulo.show();
    }
    public void PopUpR(String bola){ //muestreo de informacion de reserva
        AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
        alerta.setMessage("DATOS DEL ENCUADRADO: "+matricula+"\nNombre: "+Nombres
                +"\nApellidos"+ApellidoPat+" "+ApellidoMat
                +"\nCorreo: "+Correo
                +"\nCURP: "+CURP
                +"\nNum_Tel: "+Num_Tel).setCancelable(false)
                .setNeutralButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();

                    }
                });

        AlertDialog titulo = alerta.create();
        titulo.setTitle("SORTEO");
        titulo.show();
    }
//==========================================================
    public void consultaEscuadronE(String xd){ //consulta de datos para escuadron
        String URL = "http://192.168.56.1/android/fetchMainAescuadron.php?matricula=" + objL.Matricula;
        System.out.println("CURP "+objL.Matricula);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                System.out.println("response1: "+response);
                for (int x = 0; x < response.length(); x++) {
                    System.out.println("a ");
                    try {
                        jsonObject = response.getJSONObject(x);
                        Num_Placa = (jsonObject.getString("Num_Placa"));
                        Num_Seccion = (jsonObject.getString("N_Seccion"));
                        consultaEscuadronE2(null);
                        //readUser2 ( objL.Matricula);

                    } catch (JSONException e) {
                        System.out.println("Error1 " + e.getMessage());
                    }
                    try {
                        jsonObject = response.getJSONObject(x);
                        Num_Liberacion=(jsonObject.getString("Num_Liberacion"));
                        Fecha_Recepcion=(jsonObject.getString("Fecha_Recepcion"));
                        PopUpRescuadron(null);

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
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
    public void consultaEscuadronE2(String xd){ //metodo de complemento para consultaEscuadron
        String URL = "http://192.168.56.1/android/fetchMainAescuadron2.php?matricula=" + objL.Matricula;
        System.out.println("CURP "+objL.Matricula);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                System.out.println("response1: "+response);
                for (int x = 0; x < response.length(); x++) {
                    System.out.println("a ");
                    try {
                        jsonObject = response.getJSONObject(x);
                        Num_Liberacion=(jsonObject.getString("Num_Liberacion"));
                        consultaEscuadronE3(null);

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
    public void consultaEscuadronE3(String xd){ //metodo complementario para consultaEscuadronE2
        String URL = "http://192.168.56.1/android/fetchMainAescuadron3.php?placa=" + Num_Placa;
        System.out.println("CURP "+objL.Matricula);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                System.out.println("response1: "+response);
                for (int x = 0; x < response.length(); x++) {
                    System.out.println("a ");
                    try {
                        jsonObject = response.getJSONObject(x);
                        Nombre_Ins=(jsonObject.getString("Nombre_Ins"));
                        ApellidoPat_Ins=(jsonObject.getString("ApellidoPat_Ins"));
                        ApellidoMat_Ins=(jsonObject.getString("ApellidoMat_Ins"));
                        PopUpEescuadron(null);
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

    //========================================METODOS POPUP QUE MUESTRAN INFORMACION
    public void PopUpEescuadron(String bola){ //muestreo de informacion para los metodos consultaEscuadronE
        AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
        alerta.setMessage("DATOS DEL ENCUADRADO: "+matricula+"\nPlaca de instructor: "+Num_Placa
                +"\nNumero de seccion: "+Num_Seccion
                +"\nNum Liberacion: "+Num_Liberacion
                +"\nNombre de instructor: "+Nombre_Ins+" "+ApellidoPat_Ins+" "+ApellidoMat_Ins).setCancelable(false)
                .setNeutralButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                        dialog.dismiss();
                    }
                });

        AlertDialog titulo = alerta.create();
        titulo.setTitle("PERFIL DE USUARIO");
        titulo.show();
    }
    public void PopUpRescuadron(String bola){
        AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
        alerta.setMessage("DATOS DEL RESERVA: "+matricula+"\nNum Liberacion: "+Num_Liberacion
                +"\nFecha de recepcion: "+Fecha_Recepcion
                ).setCancelable(false)
                .setNeutralButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                        dialog.dismiss();
                    }
                });

        AlertDialog titulo = alerta.create();
        titulo.setTitle("PERFIL DE USUARIO");
        titulo.show();
    }

    public void popUpListado(View view){

        AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
        alerta.setMessage("Elija un tipo de adscrito a lista"+"\nNOTA: se mostraran resultados del año en curso").setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {


                        dialog.cancel();
                    }
                });
        AlertDialog titulo = alerta.create();
        titulo.setTitle("Listado de adscritos");
        titulo.show();
    }

  public static  String acumulaTitulo,acumulaCuerpo,acumulaDestino, total;
    public void LeeEncuadrado(String xd){//consulta de noticias
        String URL = "http://192.168.56.1/android/fetchNoticiasE.php?tipoA="+objL.tipoA;
        System.out.println("=======url=== "+objL.tipoA);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                System.out.println("response1: "+response);
                for (int x = 0; x < response.length(); x++) {
                    System.out.println("a ");
                    try {
                        jsonObject = response.getJSONObject(x);
                        acumulaTitulo= jsonObject.getString("titulo")+"\n";
                        acumulaCuerpo = jsonObject.getString("cuerpo")+"\n";
                        acumulaDestino = jsonObject.getString("destinatario")+"\n";
                       // System.out.println("AAAAAAAAA"+acumulaTitulo+acumulaCuerpo+acumulaDestino);
                        total=total+acumulaTitulo+acumulaCuerpo+acumulaDestino+"\n";
                        System.out.println("TOOOOTALL "+total);
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

    public void popUpNoticias(String total){ //muestreo de noticias

        AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
        alerta.setMessage(total).setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                        dialog.cancel();
                    }
                });
        AlertDialog titulo = alerta.create();
        titulo.setTitle("NOTICIAS RELEVANTES");
        titulo.show();
    }

    public void popUpCuartel(View view){//muestreo de informacion para INFO CUARTEL
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cuartel");
        builder.setMessage("Edicion de sorteo No 2022"+"\nDirección: 17a zona militar San Juan del Río para el 7mo regimiento mecanizado"+"nLocalizado en la ex-hacienda ‘La Llave’, en el rancho La Llave"+"\nCon línea de atención al número: 427 118 0758.");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog dialog = builder.show();

// Must call show() prior to fetching views
        TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);

        TextView titleView = (TextView)dialog.findViewById(this.getResources().getIdentifier("alertTitle", "id", "android"));
        if (titleView != null) {
            titleView.setGravity(Gravity.CENTER);
        }


    }
//pases de pantalla
    public void pasaJust (View view) {
        Intent v1 =new Intent(this,justificar.class);
        startActivity(v1);
    }
    public void pasaV3 (View view) {
        Intent v1 =new Intent(this,justificar.class);
        startActivity(v1);
    }

}