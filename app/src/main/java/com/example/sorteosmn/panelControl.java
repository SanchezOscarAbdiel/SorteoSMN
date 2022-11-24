package com.example.sorteosmn;

//IMPORT: ELEMENTOS DE LA PANTALLA
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
//IMPORT: ELEMENTOS PARA EL TRATAMIENTO DE DATOS EN LA APLICACIÓN CON FORMATO JSON (ARRAY DE ELEMENTOS)
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class panelControl extends AppCompatActivity {

    public static sorteo objIP = new sorteo();
    public static String IP = objIP.IP;


    //VARIABLES DE ENTRONO GLOBAL
    Login objL = new Login();
    String admin = objL.Correo, acumula = "", acumulaFaltas = "", MatriculaBaja = "", titulo = "", Mensaje = "", destinatario = "";
    //LLAMADA A LOS ELEMENTOS DE LA INTERFAZ DE LOGEO EN DONDE SE INTRODUCEN LOS DATOS
    EditText etAdmin, etTitulo, Cuerpo, destino;
    //LLAMADO GENERAL A SQL
    RequestQueue requestQueue;
    //BARRA DE PROGRESO
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_control);
        //LLAMADA UNIVERSAL A PHP
        requestQueue = Volley.newRequestQueue(this);
        //RECOGIDA DE DATOS DE LOS ELEMENTOS DE LA INTERFAZ EN VARIABLES
        etAdmin = (EditText) findViewById(R.id.edadmin);
        etAdmin.setText(admin);
        Cuerpo = (EditText) findViewById(R.id.ed);
        etTitulo = (EditText) findViewById(R.id.edTituloN);
        destino = (EditText) findViewById(R.id.edDestinatario);
        progressBar = (ProgressBar) findViewById(R.id.PBpanel);
        progressBar.setVisibility(View.GONE);
    }

    //CONSULTA LAS NOTICIAS ALOJADAS EN LA BD Y LAS LISTA
    public void LeeNoticias(View view){
        progressBar.setVisibility(View.VISIBLE);
        //LLAMADA AL METODO SQL PARA PARA OBTENER LA INFORMACION
        String URL = "https://"+IP+"/19590323_SMN/fetchnoticias.php";
        //INICIA EL TRATAMIENTO DE LOS DATOS CON LOS OBJETOS JSON
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int x = 0; x < response.length(); x++) {
                    try {
                        jsonObject = response.getJSONObject(x);
                        etTitulo.setText(jsonObject.getString("titulo"));
                        Cuerpo.setText(jsonObject.getString("cuerpo"));

                        progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        Toast.makeText(panelControl.this, "¡ERROR!:\n NO HAY NOTICIAS AUN.", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(panelControl.this, "¡ERROR:\n NO HAY NOTICIAS AUN.", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
        requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    //PERMITE LISTAR A LOS ADSCRITOS SEGUN LA OPCION QUE SE ELIJA (ENCUADRADO O RESERVA)
    public void popUpListado(View view){
        //LIMPIEZA DE VARIABLES
        Cuerpo.setText("");
        etTitulo.setText(" ");
        AlertDialog.Builder alerta = new AlertDialog.Builder(panelControl.this);
        alerta.setMessage("¡CUIDADO!: Elija un tipo de adscrito a listar").setCancelable(false)
                .setPositiveButton("ENCUADRADOS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        LeeEncuadrado("encuadrado");
                        dialog.cancel();
                    }
                })
                .setNegativeButton("RESERVAS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        LeeEncuadrado("reserva");
                        dialog.cancel();
                    }
                });
            AlertDialog titulo = alerta.create();
            titulo.setTitle("LISTADO DE ADSCRITOS");
            titulo.show();
    }

    // METODO PARA ENLISTAR LA TABLA ELEGIDA EN EL METODO ANTERIOR (ENCUADRADO O RESERVA)
    public void LeeEncuadrado(String tipo){
        progressBar.setVisibility(View.VISIBLE);
        //LLAMADA AL METODO SQL PARA PARA OBTENER LA INFORMACION, LLEVANDO COMO PARAMETRO EL TIPO DE ADSCRITO
        String URL = "https://"+IP+"/19590323_SMN/fetchEncuadrado.php?tabla="+tipo;
        //INICIA EL TRATAMIENTO DE LOS DATOS CON LOS OBJETOS JSON
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int x = 0; x < response.length(); x++) {
                    try {
                        jsonObject = response.getJSONObject(x);
                        etTitulo.setText("Listado de " + tipo);
                        acumula = jsonObject.getString("Matricula") + " " + jsonObject.getString("Nombre") + "\n";
                        Cuerpo.setText(Cuerpo.getText().toString() + acumula);
                        progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        Toast.makeText(panelControl.this, "¡ADVERTENCIA!: Revise los datos, ERROR: " + e.getMessage() + ".", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(panelControl.this, "¡ADVERTENCIA!: Revise los datos, ERROR: " + error.getMessage() + ".", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    //METODO PARA ENLISTAR E IMPRIMIR LA TABLA DONDE SE UBICAN LOS JUSTIFICANTES DE LOS ENCUADRADOS
    public void LeeFaltas(View view){
        progressBar.setVisibility(View.VISIBLE);
        //LIMPIEZA DE VARIABLES
        Cuerpo.setText("");
        etTitulo.setText("");
        //LLAMADA AL METODO SQL PARA PARA OBTENER LA INFORMACION
        String URL = "https://"+IP+"/19590323_SMN/fetchFaltas.php";
        //INICIA EL TRATAMIENTO DE LOS DATOS CON LOS OBJETOS JSON
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int x = 0; x < response.length(); x++) {
                    try {
                        jsonObject = response.getJSONObject(x);
                        etTitulo.setText("Listado de encuadrados con faltas");
                        acumulaFaltas = jsonObject.getString("Matricula_Enc") + " " + jsonObject.getString("L_DocJust") + "\n";
                        Cuerpo.setText(Cuerpo.getText().toString() + acumulaFaltas);
                        progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        Toast.makeText(panelControl.this, "¡ADVERTENCIA!: Revise los datos, ERROR: "+e.getMessage()+".", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(panelControl.this, "¡ERROR!: \n NO HAY NADA PARA MOSTRAR.", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    /*METODO PARA MODIFICAR LA INFORMACION DE UN ENCUADRADO CUANDO ESTE DESERTA (POR LEY ESTOS NO PUEDEN SER ELIMINADOS DE LA)
      BASE DE DATOS, SIN EMBARGO SE LE CONCATENAN UNA SERIE DE CARACTERES A SUS DATOS DE LOGEO PARA QUE PIERDA EL ACCESO A SU CUENTA */
    public void popUpBaja(View view){
        //LIMPIEZA DE VARIABLES
        Cuerpo.setText("");
        etTitulo.setText(" ");
        //CREAR CAMPO DE TEXTO DONDE SE BUSCARA LA MATRICULA
        AlertDialog.Builder alerta = new AlertDialog.Builder(panelControl.this);
        final EditText input = new EditText(panelControl.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alerta.setView(input);

        alerta.setMessage("¡CUIDADO!: Ingrese la matricula del ENCUADRADO " + "\nal que se desea restringir el acceso.").setCancelable(false)
                .setPositiveButton("BAJA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        MatriculaBaja = input.getText().toString();
                        updateBaja(MatriculaBaja);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });
        AlertDialog titulo = alerta.create();
        titulo.setTitle("BAJA DE ENCUADRADO");
        titulo.show();
    }

    //METODO PARA HACER EFECTIVA LA BAJA MEDIANTE LA CONSULTA SQL CON LA MATRICULA RECOGIDA ANTERIORMENTE
    private void updateBaja(String matricula) {//RECIBE: matricula del encuadrado (String mediante editText) || ENVIA: HashMap hacia script PHP para actualizacion de BD
        progressBar.setVisibility(View.VISIBLE);
        //LLAMADA AL METODO SQL PARA PARA OBTENER LA INFORMACION
        String URL ="https://"+IP+"/19590323_SMN/editBaja.php";
        StringRequest stringRequest =new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(panelControl.this,"¡ATENCIÓN!: SE RESTRINGIO AL ENCUADRADO" ,Toast.LENGTH_LONG).show(); //MENSAJE DE EXITO
                    progressBar.setVisibility(View.GONE);
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(panelControl.this,"¡ERROR!: REVISE LA MATRICULA INGRESADA" ,Toast.LENGTH_LONG).show(); //MENSAJE DE ERROR
                }
            }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // RECOGE TODOS LOS DATOS INGRESADOS Y LOS ENVIA A LA BASE DE DATOS ("variableDeBD", variableRecogida)
                if(matricula.length()>0){
                    Map<String, String> params = new HashMap<>();
                    params.put("matricula", matricula);
                    return params;
                }else{
                    Toast.makeText(panelControl.this,"¡ERROR!: REVISE LA MATRICULA INGRESADA" ,Toast.LENGTH_LONG).show(); //MENSAJE DE ERROR
                }
                return null;
            }
        };
        requestQueue.add(stringRequest); //TOMA EL HASH MAP Y LO ENVIA AL SCRIPT
    }

    //METODO PARA AGREGAR NOTICIAS (LADO DEL ADMINISTRADOR)
    public void InsertaNoticias(View view) {//RECIBE: variables String mediante EditText divididas en 3 campos || ENVIA: objeto Hash con los datos etiquetados
        progressBar.setVisibility(View.VISIBLE);
        //SE LLAMA A LOS EDITTEXT DECLARADOS EN EL ENTORNO GLOBAL PARA EXTRAER LOS DATOS INGRESADOS
        titulo = etTitulo.getText().toString();
        Mensaje = Cuerpo.getText().toString();
        destinatario = destino.getText().toString();
        //EN EL destinatario SE RECOGE A QUIEN IRA ENVIADA LA NOTICIA CON 3 POSIBILIDADES: Encuadrado, Reserva o General
        if( (destinatario.equalsIgnoreCase("General") || destinatario.equalsIgnoreCase("Encuadrado") || destinatario.equalsIgnoreCase("Reserva")) && titulo.length()>0 && Mensaje.length()>0 ){
            //LLAMADA AL METODO SQL PARA PARA OBTENER LA INFORMACION, LLEVANDO COMO PARAMETROEL TITULO, CUERPO Y DESTINATARIO
            String url = "https://"+IP+"/19590323_SMN/saveNoticias.php?titulo=" + titulo + "&cuerpo=" + Mensaje + "&destinatario=" + destinatario;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(panelControl.this, "¡NOTA!: SE HA PUBLICADO CORRECTAMENTE", Toast.LENGTH_LONG).show();
                    Cuerpo.getText().clear();
                    etTitulo.getText().clear();
                    destino.getText().clear();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(panelControl.this, "¡ADVERTENCIA!: Revise los datos, ERROR: " + error.getMessage() + ".", Toast.LENGTH_LONG).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    // RECOGE TODOS LOS DATOS INGRESADOS Y LOS ENVIA A LA BASE DE DATOS ("variableDeBD", variableRecogida)
                    Map<String, String> params = new HashMap<>();
                    params.put("titulo", titulo);
                    params.put("cuerpo", Mensaje);
                    params.put("destinatario", destinatario);
                    return params;
                }
            };
            requestQueue.add(stringRequest); //TOMA EL HASH MAP Y LO ENVIA AL SCRIPT
        }else{
            progressBar.setVisibility(View.GONE);

            Toast.makeText(panelControl.this,"¡ERROR!: LLENA TODOS LOS DATOS" ,Toast.LENGTH_LONG).show();
        }
    }
    //LIMPIA VARIABLES Y REINICIA PANTALLAS PARA LLEVAR AL USUARIO DE NUEVO AL INICIO DE SESION
    public void CierraSesionAdmin(View view){
        objL.tipoA ="";
        progressBar.setVisibility(View.GONE);
        Toast.makeText(panelControl.this, "CERRANDO SESION...\nHASTA LUEGO", Toast.LENGTH_LONG).show();
        finish();
        startActivity(getIntent());
        Intent v1 = new Intent(this, Login.class);
        startActivity(v1);
    }

    public void LimpiaPantalla(View view){
        Cuerpo.getText().clear();
        etTitulo.getText().clear();
        destino.getText().clear();
    }
    //EVITA QUE SE CIERRE LA APLICACION SIN EL METODO ADECUADO
    public void onBackPressed(){
        Toast.makeText(panelControl.this, "PARA CERRAR SESION \nTOQUE EL BOTON CORRESPONDIENTE", Toast.LENGTH_LONG).show();
    }

}