package com.example.sorteosmn;

//IMPORT: ELEMENTOS DE LA PANTALLA
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
//IMPORT: ELEMENTOS PARA EL TRATAMIENTO DE DATOS EN LA APLICACIÓN CON FORMATO JSON (ARRAY DE ELEMENTOS)
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class sorteo extends AppCompatActivity {
    //LLAMADA GENERAL A SQL
    RequestQueue requestQueue;
    //VARIABLES DE ENTRONO GLOBAL
    public static Registro objR = new Registro();
    public static Boolean res = objR.Reserva;
    public static String ConsultaMatricula;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText telefono;
    public String Num_Tel;
    public Button aceptar;
    private EditText peso, altura, sangre;
    public String TipoSangre, EnviaPeso, EnviaAltura;
    int Peso;
    Double Altura;
    public Button aceptar1;
    //ELEMENTOS DE INTERFAZ PARA LA CAPTURA DE DATOS
    public static EditText Matricula, NumLiberacion, Nombre, Apellidop, Apellidom, Resultado, TipoA;
    public FloatingActionButton btGuarda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorteo);
        //LLAMADA UNIVERSAL A PHP
        requestQueue = Volley.newRequestQueue(this);
        //DEPENDIENDO EL TIPO DE BOLA (O RESULTADO) MOSTRARA INGRESOS DIFERENTES
        if (objR.EnviaBola == "Negra") {
            PopUpReserva();
        } else {
            PopUpEncuadrado();
        }
        //RECOGIDA DE DATOS DE LOS ELEMENTOS DE LA INTERFAZ EN VARIABLES
        Matricula = (EditText)findViewById(R.id.etMatricula);
        NumLiberacion = (EditText)findViewById(R.id.etLiberacion);
        Nombre = (EditText)findViewById(R.id.etNombreS);
        Apellidop = (EditText)findViewById(R.id.etApellidop);
        Apellidom = (EditText)findViewById(R.id.etApellidom);
        TipoA = (EditText)findViewById(R.id.etTipoA);
        Resultado =(EditText)findViewById(R.id.etResultado);
        btGuarda = findViewById(R.id.btSave);
        //MUESTRA LOS DATOS EN LOS CAMPOS
        readUser1(null);
        //ALMACENAR EL RESULTADO DEL SORTEO EN UNA VARIABLE
        Resultado.setText("Bola " + objR.EnviaBola);
        if (objR.EnviaBola == "Blanca") {
            TipoA.setText("Encuadrado");
        } else {
            TipoA.setText("Reserva");
        }

        //GUARDARA LA MATRICULA (COMO CONTRASENIA DE INICIO DE SESION) EN EL PORTA PAPELES
        btGuarda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("EditText", Matricula.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(sorteo.this, "TU CONTRASEÑA (MATRICULA)" + "\nFUE COPIADA AL PORTAPAPELES.", Toast.LENGTH_LONG).show();
                pasaV8(null);
            }
        });
    }

    //MUESTRA UNA PANTALLA EMERGENTE CON CAMPOS DE TEXTO CORRESPONDIENTES A LA ACTUALIZACION DE DATOS DEL RESERVA (N° TELEFONO)
    public void PopUpReserva(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.popup,null);
        telefono = (EditText) contactPopupView.findViewById(R.id.etCelular);
        aceptar = (Button) contactPopupView.findViewById(R.id.btnAceptar);
        dialogBuilder.setView(contactPopupView);
        dialog = dialogBuilder.create();
        dialog.show();
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Num_Tel = telefono.getText().toString();
                if(Num_Tel.length()>8 && Num_Tel.length()<10){updateUser(null); dialog.dismiss();}
                else{Toast.makeText(sorteo.this, "Numero de celular incorrecto", Toast.LENGTH_LONG).show();}

            }
        });
    }

    //MUESTRA UNA PANTALLA EMERGENTE CON CAMPOS DE TEXTO CORRESPONDIENTES A LA ACTUALIZACION DE DATOS DEL ENCUADRADO
    public void PopUpEncuadrado(){
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
                Peso = Integer.parseInt(peso.getText().toString());
                Altura =Double.parseDouble(altura.getText().toString());
                TipoSangre = sangre.getText().toString();

                //SE REVISARA QUE LOS DATOS INGRESADOS SEAN CORRECTOS (CASO DEL TIPO DE SANGRE
                if (TipoSangre.equalsIgnoreCase("A+") || TipoSangre.equalsIgnoreCase("A-")
                        || TipoSangre.equalsIgnoreCase("B+") || TipoSangre.equalsIgnoreCase("B-")
                        || TipoSangre.equalsIgnoreCase("O+") || TipoSangre.equalsIgnoreCase("O-")
                        || TipoSangre.equalsIgnoreCase("AB-") || TipoSangre.equalsIgnoreCase("AB+")
                        && (Altura > 1.3 && Altura < 2.1) && (Peso > 40 && Peso < 180)) {
                    EnviaAltura = String.valueOf(Altura);
                    EnviaPeso = String.valueOf(Peso);
                    updateUserE();
                    dialog.dismiss();
                } else {
                    Toast.makeText(sorteo.this, "¡ERROR!: REVISA TUS DATOS", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //SI LOS DATOS DE POPUP ENCUADRADO SON CORRECTOS, EN ESTE METODO SE MANDA A LLAMAR AL LLENADO EN LA BASE DE DATOS
    public void updateUserE(){
        //LLAMADA AL METODO SQL PARA PARA OBTENER LA INFORMACION
        String URL = "http://192.168.56.1/android/editE.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(sorteo.this, "¡EXITO!: SE HAN ACTUALIZADO LOS DATOS", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(sorteo.this, "¡ADVERTENCIA!: Revise los datos.", Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("TipoSangre", TipoSangre);
                params.put("Peso", EnviaPeso);
                params.put("Altura", EnviaAltura);
                params.put("CURP_Enc", objR.EnviaCurp);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    //EN ESTE METODO SE TOMARAN LOS DATOS INGRESADOS EN EL METODO ANTERIOR Y SE INGRESARAN A LA BASE DE DATOS
    private void updateUser(String xd) {
        //LLAMADA AL METODO SQL PARA PARA OBTENER LA INFORMACION
        String URL = "http://192.168.56.1/android/edit.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(sorteo.this, "¡EXITO!: Se ha actualizado el numero de celular", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(sorteo.this, "¡ADVERTENCIA!: Revise los datos.", Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Num_Tel", Num_Tel);
                params.put("CURP_Res", objR.EnviaCurp);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    //RECOGE LOS DATOS DE LA BASE DE DATOS PARA SU MUESTREO PARA MOSTRAR LOS RESULTADOS EN CASO DE SER ENCUADRADO O RESERVA
    public void readUser1 (String xd) {
        //LLAMADA AL METODO SQL PARA PARA OBTENER LA INFORMACION LLEVANDO COMO PARAMETRO LA CURP
        String URL = "http://192.168.56.1/android/fetchsorteo.php?curp=" + objR.EnviaCurp;
        //INICIA EL TRATAMIENTO DE LOS DATOS CON LOS OBJETOS JSON
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int x = 0; x < response.length(); x++) {
                    System.out.println("a ");
                    try {
                        jsonObject = response.getJSONObject(x);
                        Matricula.setText(jsonObject.getString("Matricula_Enc"));
                        Nombre.setText(jsonObject.getString("Nombres_Enc"));
                        Apellidop.setText(jsonObject.getString("ApellidoPat_Enc"));
                        Apellidom.setText(jsonObject.getString("ApellidoMat_Enc"));
                        ConsultaMatricula = jsonObject.getString("Matricula_Enc");
                        readUser2(ConsultaMatricula);
                    } catch (JSONException e) {
                        Toast.makeText(sorteo.this, "¡ADVERTENCIA!: Revise los datos.", Toast.LENGTH_LONG).show();
                    }
                    try {
                        jsonObject = response.getJSONObject(x);
                        Matricula.setText(jsonObject.getString("Matricula_Res"));
                        Nombre.setText(jsonObject.getString("Nombres_Res"));
                        Apellidop.setText(jsonObject.getString("ApellidoPat_Res"));
                        Apellidom.setText(jsonObject.getString("ApellidoMat_Res"));
                        ConsultaMatricula = jsonObject.getString("Matricula_Res");
                        readUser2(ConsultaMatricula);
                    } catch (JSONException e) {
                        Toast.makeText(sorteo.this, "¡ADVERTENCIA!: Revise los datos.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(sorteo.this, "¡ADVERTENCIA!: Revise los datos.", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    //METODO PARA OBTENER LA MATRICULA DEL ADSCRITO Y GUARDAR EL DATO
    public void readUser2 (String mat) {
        //LLAMADA AL METODO SQL PARA PARA OBTENER LA INFORMACION}
        String URL = "http://192.168.56.1/android/fetchsorteo2.php?matricula=" + mat;
        //INICIA EL TRATAMIENTO DE LOS DATOS CON LOS OBJETOS JSON
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int x = 0; x < response.length(); x++) {
                    try {
                        jsonObject = response.getJSONObject(x);
                        NumLiberacion.setText(jsonObject.getString("Num_Liberacion"));
                    } catch (JSONException e) {
                        Toast.makeText(sorteo.this, "¡ADVERTENCIA!: Revise los datos.", Toast.LENGTH_LONG).show();
                    }
                    try {
                        jsonObject = response.getJSONObject(x);
                        NumLiberacion.setText(jsonObject.getString("Num_Liberacion"));
                    } catch (JSONException e) {
                        Toast.makeText(sorteo.this, "¡ADVERTENCIA!: Revise los datos.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(sorteo.this, "¡ADVERTENCIA!: Revise los datos.", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue=Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    //METODOS DE PASE ENTRE INTERFACES (LOGIN)
    public void pasaV8 (String xd) {
        Intent v1 =new Intent(this,Login.class);
        startActivity(v1);
    }

}