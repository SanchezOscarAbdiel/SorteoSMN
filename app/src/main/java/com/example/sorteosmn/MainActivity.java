package com.example.sorteosmn;

//IMPORT: ELEMENTOS DE LA PANTALLA
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
//IMPORT: ELEMENTOS PARA EL TRATAMIENTO DE DATOS EN LA APLICACIÓN CON FORMATO JSON (ARRAY DE ELEMENTOS)
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//MENU PRINCIPAL DE LA APLICACION (DONDE SE UBICAN LOS BOTONES PRINCIPALES)
public class MainActivity extends AppCompatActivity {
    public static sorteo objIP = new sorteo();
    public static String IP = objIP.IP;

    //LLAMADO GENERAL A SQL
    RequestQueue requestQueue;
    //VARIABLES DE ENTRONO GLOBAL
    Login objL = new Login();
    public static String acumulaTitulo, acumulaCuerpo, acumulaDestino, total;
    String matricula = objL.Matricula, CURP = "", Nombres = "", ApellidoPat = "", ApellidoMat = "", Correo = "", Num_Tel = "";
    String Num_Placa = "", Num_Seccion = "", Num_Liberacion = "", Fecha_Recepcion = "", Nombre_Ins = "", ApellidoPat_Ins = "", ApellidoMat_Ins = "";
    int enseña = 0,ContadorNoticias;
    //LLAMADA A LOS ELEMENTOS DE LA INTERFAZ DE LOGEO EN DONDE SE INTRODUCEN LOS DATOS
    Button Bperfil, Bescuadron, Bnoticias;
    FloatingActionButton btnCerrarSesion;
    ImageView FotoPerfil;
    TextView Usuario, tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //LLAMADA UNIVERSAL A PHP
        requestQueue = Volley.newRequestQueue(this);
        //RECOGIDA DE DATOS DE LOS ELEMENTOS DE LA INTERFAZ EN VARIABLES
        Usuario = (TextView) findViewById(R.id.tvTipoP);
        tipo = (TextView) findViewById(R.id.tvTipoP2);
        Usuario.setText(objL.Correo);
        tipo.setText(objL.tipoA);
        //IDENTIFICA BOTON PARA CERRAR SESION
        btnCerrarSesion = (FloatingActionButton) findViewById(R.id.btnLogOut);
        //LLAMADA AL METODO CONSULTA USUARIO
        consultaUsuario(null);
        //RECOGIDA DE DATOS DE LOS ELEMENTOS DE LA INTERFAZ EN VARIABLES
        Bperfil = (Button) findViewById(R.id.bperfil);
        botonPerfil(null);
        Bescuadron = (Button) findViewById(R.id.bescuadron);
        botonEscuadron(null);
        Bnoticias = (Button) findViewById(R.id.bnoticias);
        botonNoticias(null);
        FotoPerfil = (ImageView) findViewById(R.id.ImgPerfil);
        if(objL.tipoA.equals("Encuadrado")){
            FotoPerfil.setImageResource(R.drawable.militar);
        }else{
            FotoPerfil.setImageResource(R.drawable.reserva);
        }
    }

    //METODO PARA LA RESOLUCION DE QUE PANTALLA MOSTRAR SEGUN EL LOGEO (ENCUADRADO/RESERVA O ADMINISTRADOR)
    public void botonPerfil(String xd){
        Bperfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enseña == 1) {
                    PopUpE(null);
                } else if (enseña == 2) {
                    PopUpR(null);
                }
            }
        });
    }

    //METODO PARA MOSTRAR UN POPUP DE INFORMACION DE ESCUADRON
    public void botonEscuadron(String xd){
        Bescuadron.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            consultaEscuadronE(null);
            }
        });
    }

    //METODO DE LLAMADA AL GUARDADO DE LAS NOTICIAS Y AVISOS PARA SU MUESTREO EN UN POPUP
    public void botonNoticias(String xd){
        Bnoticias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LeeEncuadrado(null);
            }
        });
    }

    //METODO PARA CONSULTAR LA INFORMACION DEL ENCUADRADO O RESERVA EN EL PERFIL
    public void consultaUsuario(String xd) {
        //LLAMADA AL METODO SQL PARA PARA OBTENER LA INFORMACION, LLEVANDO COMO PARAMETRO LA MATRICULA
        String URL = "https://"+IP+"/19590323_SMN/fetchMainA.php?matricula=" + objL.Matricula;
        //INICIA EL TRATAMIENTO DE LOS DATOS CON LOS OBJETOS JSON
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int x = 0; x < response.length(); x++) {
                    try {
                        //PARA TRAER LA INFORMACION DEL ENCUADRADO
                        jsonObject = response.getJSONObject(x);
                        CURP = (jsonObject.getString("CURP_Enc"));
                        Nombres = (jsonObject.getString("Nombres_Enc"));
                        ApellidoPat = (jsonObject.getString("ApellidoPat_Enc"));
                        ApellidoMat = (jsonObject.getString("ApellidoMat_Enc"));
                        Correo = (jsonObject.getString("Correo_Enc"));
                        enseña = 1;
                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, "¡ADVERTENCIA!: Revise los datos, ERROR: "+e.getMessage()+".", Toast.LENGTH_LONG).show();
                    }
                    try {
                        //PARA TRAER LA INFORMACION DEL RESERVA
                        jsonObject = response.getJSONObject(x);
                        CURP = (jsonObject.getString("CURP_Res"));
                        Nombres = (jsonObject.getString("Nombres_Res"));
                        ApellidoPat = (jsonObject.getString("ApellidoPat_Res"));
                        ApellidoMat = (jsonObject.getString("ApellidoMat_Res"));
                        Correo = (jsonObject.getString("Correo_Res"));
                        Num_Tel = (jsonObject.getString("Num_Tel"));
                        enseña = 2;
                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, "¡ADVERTENCIA!: Revise los datos, ERROR: "+e.getMessage()+".", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "¡ADVERTENCIA!: Revise los datos, ERROR: "+error.getMessage()+".", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    //METODO PARA EL MUESTREO DE LA INFORMACION DE LOS ENCUADRADOS MEDIANTE UN POPUP
    public void PopUpE(String bola){
        AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
        alerta.setMessage("DATOS DEL ENCUADRADO: " + matricula + ".\n- NOMBRE(S): " + Nombres
                + "\n- APPELLIDOS: " + ApellidoPat + " " + ApellidoMat
                + "\n- CORREO: " + Correo
                + "\n- CURP: " + CURP).setCancelable(false)
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

    //METODO PARA EL MUESTREO DE LA INFORMACION DE LOS RESERVA MEDIANTE UN POPUP
    public void PopUpR(String bola) {
        AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
        alerta.setMessage("DATOS DEL RESERVA: " + matricula + ".\n- NOMBRE(S): " + Nombres
                + "\n- APELLIDOS" + ApellidoPat + " " + ApellidoMat
                + "\n- CORREO: " + Correo
                + "\n- CURP: " + CURP
                + "\n- TELEFONO: " + Num_Tel).setCancelable(false)
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

    //METODO PARA CONSULTAR DATOS DE 3 TABLAS COMPLEMENTARIAS A LA INFORMACION DEL ENCUADRADO
        //TRAER LOS DATOS DEL ESCUADRON DEL ENCUADRADO (TABLA ESCUADRON)
    public void consultaEscuadronE(String xd){
        //LLAMADA AL METODO SQL PARA HACER LA CONSULTA ADECUADA LLEVANDO COMO PARAMETRO LA MATRICULA DEL ENCUADRADO
        String URL = "https://"+IP+"/19590323_SMN/fetchMainAescuadron.php?matricula=" + objL.Matricula;
        //INICIA EL TRATAMIENTO DE LOS DATOS MEDIANTE LOS OBJETOS JSON
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int x = 0; x < response.length(); x++) {
                    try {
                        jsonObject = response.getJSONObject(x);
                        Num_Placa = (jsonObject.getString("Num_Placa"));
                        Num_Seccion = (jsonObject.getString("N_Seccion"));
                        consultaEscuadronE2(null);
                    } catch (JSONException e) {
                       // Toast.makeText(MainActivity.this, "¡ADVERTENCIA!: Revise los datos, ERROR: "+e.getMessage()+".", Toast.LENGTH_LONG).show();
                    }
                    try {
                        jsonObject = response.getJSONObject(x);
                        Num_Liberacion=(jsonObject.getString("Num_Liberacion"));
                        Fecha_Recepcion=(jsonObject.getString("Fecha_Recepcion"));
                        PopUpRescuadron(null);
                    } catch (JSONException e) {
                       // Toast.makeText(MainActivity.this, "¡ADVERTENCIA!: Revise los datos, ERROR: "+e.getMessage()+".", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(MainActivity.this, "¡ADVERTENCIA!: Revise los datos, ERROR: "+error.getMessage()+".", Toast.LENGTH_LONG).show();
            }
        }
        );
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
        //TRAER LOS DATOS DEL NUMERO DE LIBERACION DE LA CARTILLA (TABLA CARTILLA)
    public void consultaEscuadronE2(String xd) {
        //LLAMADA AL METODO SQL PARA HACER LA CONSULTA ADECUADA LLEVANDO COMO PARAMETRO LA MATRICULA DEL ENCUADRADO
        String URL = "https://"+IP+"/19590323_SMN/fetchMainAescuadron2.php?matricula=" + objL.Matricula;
        //INICIA EL TRATAMIENTO DE LOS DATOS MEDIANTE LOS OBJETOS JSON
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int x = 0; x < response.length(); x++) {
                    try {
                        jsonObject = response.getJSONObject(x);
                        Num_Liberacion = (jsonObject.getString("Num_Liberacion"));
                        consultaEscuadronE3(null);
                    } catch (JSONException e) {
                      //  Toast.makeText(MainActivity.this, "¡ADVERTENCIA!: Revise los datos, ERROR: "+e.getMessage()+".", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // Toast.makeText(MainActivity.this, "¡ADVERTENCIA!: Revise los datos, ERROR: "+error.getMessage()+".", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
        //TRAER LOS DATOS DEL INSTRUCTOR SEGUN EL ESCUADRON (TABLA INSTRUCTOR)
    public void consultaEscuadronE3(String xd){
        //LLAMADA AL METODO SQL PARA HACER LA CONSULTA ADECUADA LLEVANDO COMO PARAMETRO LA PLACA DEL INSTRUCTOR
        String URL = "https://"+IP+"/19590323_SMN/fetchMainAescuadron3.php?placa=" + Num_Placa;
        //INICIA EL TRATAMIENTO DE LOS DATOS MEDIANTE LOS OBJETOS JSON
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int x = 0; x < response.length(); x++) {
                    try {
                        jsonObject = response.getJSONObject(x);
                        Nombre_Ins = (jsonObject.getString("Nombre_Ins"));
                        ApellidoPat_Ins = (jsonObject.getString("ApellidoPat_Ins"));
                        ApellidoMat_Ins = (jsonObject.getString("ApellidoMat_Ins"));
                        PopUpEescuadron(null);
                    } catch (JSONException e) {
                       // Toast.makeText(MainActivity.this, "¡ADVERTENCIA!: Revise los datos, ERROR: "+e.getMessage()+".", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(MainActivity.this, "¡ADVERTENCIA!: Revise los datos, ERROR: "+error.getMessage()+".", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    //METODO POPUP QUE MUESTRA INFORMACION EN POPUP DEL ENCUADRADO (NUMERO DE LIBERACION, NUMERO DE SECCION, PLACA DEL INSTRUCTOR, NOMBRE DEL INSTRUCTOR)
    public void PopUpEescuadron(String bola) {
        AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
        alerta.setMessage("DATOS DEL ENCUADRADO: " + matricula + ".\n- N° LIBERACIÓN: " + Num_Liberacion
                + "\n- N° SECCIÓN: " + Num_Seccion
                + "\n- PLACA DEL INSTRUCTOR: " + Num_Placa
                + "\n- NOMBRE INSTRUCTOR: " + Nombre_Ins + " " + ApellidoPat_Ins + " " + ApellidoMat_Ins).setCancelable(false)
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
    //METODO POPUP QUE MUESTRA INFORMACION EN POPUP DEL RESERVA (NUMERO DE LIBERACION Y LA FECHA DE RECOGIDA PARA LA CARTILLA)
    public void PopUpRescuadron(String bola){
        AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
        alerta.setMessage("DATOS DEL RESERVA: "+matricula+".\n- N° LIBERACIÓN: "+Num_Liberacion
                +"\n- FECHA DE RECEPCIÓN PARA CARTILLA: "+Fecha_Recepcion
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
    //METODO QUE MUESTRA UN LISTADO DE LOS ADSCRITOS EN EL ANIO O EDICION ACTUAL DEL SMN
    public void popUpListado(View view) {
        AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
        alerta.setMessage("¡CUIDADO!: Elija un tipo de adscrito a lista.").setCancelable(false)
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });
        AlertDialog titulo = alerta.create();
        titulo.setTitle("LISTADO DE ADSCRITOS");
        titulo.show();
    }

    //METODO PARA MOSTRAR LOS ANUNCIOS/NOTICIAS EN BASE DE SER UN ENCUADRADO/RESERVA
    public void LeeEncuadrado(String xd) {
        //LLAMADO AL METODO SQL PARA HACER LA SELECCION DE LAS NOTICIAS
        String URL = "https://"+IP+"/19590323_SMN/fetchNoticiasE.php?tipoA=" + objL.tipoA;
        //INICIA EL TRATAMIENTO DE LA INFORAMCION CON LOS OBJETOS JSON
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int x = 0; x < response.length(); x++) {
                    try {
                        jsonObject = response.getJSONObject(x);
                        acumulaTitulo = jsonObject.getString("titulo") + "\n";
                        acumulaCuerpo = jsonObject.getString("cuerpo") + "\n";
                        acumulaDestino = jsonObject.getString("destinatario") + "\n";
                        //AL ESTAR X=0 NO ACUMULA EL PRIMER "TOTAL" EVITANDO LA IMPRESION DE UN NULL
                        if(x==0){
                        total = acumulaTitulo + acumulaCuerpo + acumulaDestino + "\n";
                        }else if(x>0){
                            total = total + acumulaTitulo + acumulaCuerpo + acumulaDestino + "\n";
                        }
                    } catch (JSONException e) {
                        Toast.makeText(MainActivity.this, "¡ADVERTENCIA!: Revise los datos, ERROR: "+e.getMessage()+".", Toast.LENGTH_LONG).show();
                    }
                }
                //TOMA TODAS LAS NOTICIAS ACUMULADAS Y LAS MANDA A UN POPUP PARA SU IMPRESION
                popUpNoticias(total);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "¡ADVERTENCIA!: Revise los datos, ERROR: "+error.getMessage()+".", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    //METODO PARA EL MUESTREO DE LAS NOTICIAS MEDIANTE UN POPUP
    public void popUpNoticias(String total) {
        AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);

        alerta.setMessage(total).setCancelable(false)
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });
        AlertDialog titulo = alerta.create();
        titulo.setTitle("NOTICIAS RELEVANTES");
        titulo.show();
    }

    //METODO PARA MOSTRAR INFORMACION SOBRE EL CUARTEL DONDE SE LLEVA A CABO EL SMN
    public void popUpCuartel(View view){//muestreo de informacion para INFO CUARTEL
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("CUARTEL.");
        builder.setMessage("EDICIÓN DEL SORTEO N°: 2022."+"\nDIRECCIÓN: 17a Zona Militar San Juan del Río para el 7mo Regimiento Mecanizado, Ex-Hacienda ‘La Llave’."+"\nNÚMERO TEL.: 427 118 0758.");
        builder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog dialog = builder.show();
        //HACE LA GRAVEDAD DEL TEXTO AL CENTRO (ALINEACION CENTRAL)
        TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
        TextView titleView = (TextView)dialog.findViewById(this.getResources().getIdentifier("alertTitle", "id", "android"));
        if (titleView != null) {
            titleView.setGravity(Gravity.CENTER);
        }
    }

    //METODOS DE PASE ENTRE INTERFACES
        //INTERFAZ DE JUSTIFICACION
    public void pasaJust (View view) {
        Intent v1 =new Intent(this,justificar.class);
        startActivity(v1);
    }
    public void pasaV3 (View view) {
        Intent v1 =new Intent(this,justificar.class);
        startActivity(v1);
    }

    //LIMPIA VARIABLES Y REINICIA PANTALLAS PARA LLEVAR AL USUARIO DE NUEVO AL INICIO DE SESION
    public void CierraSesion (View view) {
    objL.tipoA ="";
    total ="";
        Toast.makeText(MainActivity.this, "CERRANDO SESION...\nHASTA LUEGO", Toast.LENGTH_LONG).show();
        finish();
        startActivity(getIntent());
        Intent v1 = new Intent(this, Login.class);
        startActivity(v1);
    }

    //EVITA QUE SE CIERRE LA APLICACION SIN EL METODO ADECUADO
    public void onBackPressed(){
        Toast.makeText(MainActivity.this, "PARA CERRAR SESION \nTOQUE EL BOTON CORRESPONDIENTE", Toast.LENGTH_LONG).show();
    }

}