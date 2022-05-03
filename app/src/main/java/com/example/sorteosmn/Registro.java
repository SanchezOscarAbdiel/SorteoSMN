package com.example.sorteosmn;

//IMPORT: ELEMENTOS DE LA PANTALLA
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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
//IMPORT: ELEMENTOS PARA EL TRATAMIENTO DE DATOS EN LA APLICACIÓN CON FORMATO JSON (ARRAY DE ELEMENTOS)
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Registro extends AppCompatActivity {

    public static sorteo objIP = new sorteo();
    public static String IP = objIP.IP;

    //LLAMADA GENERAL A SQL
    RequestQueue requestQueue;
    public static final String url = "http://"+IP+"/android/save.php";
    //ELEMENTOS DE INTERFAZ PARA LA CAPTURA DE DATOS
    public Spinner spinner;
    public Spinner spEstadoc;
    public Spinner Discapacidad;
    public RadioGroup Sexo;
    public RadioButton Masculino, Femenino;
    public static EditText Nombre, ApellidoP, ApellidoM, Curp, Edad, NumEx, NumIn, Calle, Ciudad, Profesion, Correo, Resultado;
    public static String edad, nombre, apellidop, apellidom, curp, num_ext, num_int, calle, colonia, ciudad, estado_civ, profesion, sexo, discapacidad, correo;
    //VARIABLES DE ENTRONO GLOBAL
    public static int Sexo1;
    public static String EnviaCurp, EnviaMatricula, EnviaBola;
    Boolean Reserva = true;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        //LLAMADA UNIVERSAL A PHP
        requestQueue = Volley.newRequestQueue(this);
        //RECOGIDA DE DATOS DE LOS ELEMENTOS DE LA INTERFAZ EN VARIABLES
        spEstadoc=(Spinner) findViewById(R.id.spEstadoc);
        Sexo=(RadioGroup) findViewById(R.id.rgSexo);
        Nombre = (EditText)findViewById(R.id.etNombre);
        ApellidoP = (EditText)findViewById(R.id.etApellidoP);
        ApellidoM = (EditText)findViewById(R.id.etApellidoM);
        Curp = (EditText)findViewById(R.id.etCurp);
        Edad = (EditText)findViewById(R.id.etEdad);
        NumEx = (EditText)findViewById(R.id.etNumExt);
        NumIn = (EditText)findViewById(R.id.etNumInt);
        Calle = (EditText)findViewById(R.id.etCalle);
        Ciudad = (EditText)findViewById(R.id.etCiudad);
        Profesion= (EditText)findViewById(R.id.etProfesion);
        Discapacidad= (Spinner) findViewById(R.id.spdiscapacidad);
        Correo= (EditText)findViewById(R.id.etCorreo);
        //LLAMA LLENADO DE SPINNERS
        colonia(null);
        EstadoCivil(null);
        Discapacidad(null);
    }

    //METODOS PARA INGRESO DE DATOS A SPINNERS (MENUS DESPLEGABLES) EN BASE A TXT
    public void colonia (String xd){  //RECIBE: Valores estaticos guardados en un documento .txt || ENVIA: Listado de datos en forma de Spinner
        //SE LLAMA A LOS EDITTEXT DECLARADOS EN EL ENTORNO GLOBAL PARA EXTRAER LOS DATOS INGRESADOS
        requestQueue = Volley.newRequestQueue(this);
        spinner = (Spinner) findViewById(R.id.spColonia);
        //VARIABLES LOCALES AL METODO DONDE SE GUARDARA LA INFORMACION DEL TXT
        List<String> spin = new ArrayList<String>();
        String linea = null;

        //INICIA LA RECOGIDA DE DATOS DESDE EL TXT Y LOS INGRESA AL SPINNER
        InputStream is = this.getResources().openRawResource(R.raw.colonias);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        if (is != null) { //Mientras el renglon no esté vacio se recogerá el texto y se guardará (Linea 98)
            while (true) {
                try {
                    if (!((linea = reader.readLine()) != null)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                spin.add(linea.split(",")[0]); //Se agrega un nuevo renglon en el Spinner cada vez que se encuentre una coma
            }
        }
        try {
            is.close(); //Cierra el documento y el metodo
        } catch (IOException e) {
            e.printStackTrace();
        }
        String datos[] = spin.toArray(new String[spin.size()]); //Forma un Array de datos recogiendo el tamaño del Spinner para su correcto llenado
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, datos); //Forma y prepara un objeto compatible con un Spinner
        spinner.setAdapter(adapter); //Toma el objeto de la linea pasada ya lleno de datos y los asigna al Spinner para su muestreo

    }

    //LLENADO DE LOS SPINNERS PARA ESTADOCIVIL Y DISCAPACIDAD A PARTIR DE ARCHIVOS XML
    public void EstadoCivil(String xd) { //Recibe: caracteres almacenados en archivos XML || Envia:  Listado de datos en forma de Spinner
        ArrayAdapter<CharSequence> adapterE = ArrayAdapter.createFromResource(this, R.array.combo_Estado, android.R.layout.simple_spinner_item); /*TOMA los renglones
        almacenados en la clase mencionada en "R.array" y los guarda en un objeto Array*/
        spEstadoc.setAdapter(adapterE); //ASIGNA el objeto array al spinner correspondiente
    }
    public void Discapacidad(String xd) { ////Recibe: caracteres almacenados en archivos XML || Envia:  Listado de datos en forma de Spinner
        ArrayAdapter<CharSequence> adapterE = ArrayAdapter.createFromResource(this, R.array.combo_discapacidad, android.R.layout.simple_spinner_item);
        Discapacidad.setAdapter(adapterE);
    }

    //INSERCION EN BASE DE DATOS
    public void createUser(View view){ //RECIBE: caracteres String almacenados en campos EditText y Spinners || ENVIA: Objeto tipo HashMap con datos etiquetados hacia la BD
        //SE LLAMA A LOS EDITTEXT DECLARADOS EN EL ENTORNO GLOBAL PARA EXTRAER LOS DATOS INGRESADOS
        nombre = Nombre.getText().toString();
        apellidop=ApellidoP.getText().toString();
        apellidom = ApellidoM.getText().toString();
        curp = Curp.getText().toString();
        edad = Edad.getText().toString();
        num_ext = NumEx.getText().toString();
        num_int = NumIn.getText().toString();
        calle = Calle.getText().toString();
        colonia = spinner.getSelectedItem().toString();
        ciudad = Ciudad.getText().toString();
        estado_civ = spEstadoc.getSelectedItem().toString();
        profesion=Profesion.getText().toString();
        Sexo1 = Sexo.getCheckedRadioButtonId();
        Masculino = findViewById(Sexo1);
        sexo = Masculino.getText().toString();
        if (sexo == "(M) Masculino") { //TOMA EL TEXTO DEL RADIO BUTTON Y LO REDEFINE
            sexo = "F";
        } else {
            sexo = "M";
        }
        discapacidad = Discapacidad.getSelectedItem().toString();
        correo = Correo.getText().toString();

        //INDICA QUE EL REGISTRO HAYA SIDO CORRECTO O SI EXISTE ALGUN ERROR
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Registro.this, "¡EXITO AL REALIZAR EL REGISTRO!, FELICIDADES", Toast.LENGTH_LONG).show();
                readUser(null);//MANDA LLAMAR EL METODO ENCARGADO DE MUESTREO DE DATOS PARA EL USUARIO
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // Toast.makeText(Registro.this, "¡ERROR AL REALIZAR EL REGISTRO!, PRUEBA DE NUEVO", Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>(); // RECOGE TODOS LOS DATOS INGRESADOS Y LOS ENVIA A LA BASE DE DATOS ("variableDeBD", variableRecogida)
                params.put("nombre", nombre);
                params.put("apellidop", apellidop);
                params.put("apellidom", apellidom);
                params.put("curp", curp);
                params.put("edad", edad);
                params.put("num_ext", num_ext);
                params.put("num_int", num_int);
                params.put("calle", calle);
                params.put("colonia", colonia);
                params.put("ciudad", ciudad);
                params.put("estado_civ", estado_civ);
                params.put("profesion", profesion);
                params.put("sexo", sexo);
                params.put("discapacidad", discapacidad);
                params.put("correo", correo);
                return params;
            }
        };
        requestQueue.add(stringRequest); //MANDA LLAMAR A LA BD
    }

    //METODO PARA MOSTRAR EL RESULTADO DEL SORTEO (BOLA BLANCA O BOLA NEGRA)
    public void PopUp(String bola) {
        AlertDialog.Builder alerta = new AlertDialog.Builder(Registro.this);
        alerta.setMessage("¡FELICIDADES!, El color resultante del sorteo es: " + bola + "\n¡CUIDADO!: Serás dirigido a tu hoja de datos.").setCancelable(false)
                .setNeutralButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        EnviaBola = bola;
                        pasaV6(null);
                    }
                });
        AlertDialog titulo = alerta.create();
        titulo.setTitle("SORTEO");
        titulo.show();
    }

    //METODO PARA MOSTRAR LOS DATOS NECESARIOS EN LA HOJA DEL ASPIRANTE A MODO DE RESUMEN
    private void readUser(String xd) { //RECIBE: curp del Aspirante mediante un String recogido de la clase sorteo; objeto JSON con datos recibidos de MYSQL
                                      // ENVIA: objeto HASH hacia la BD
        //SE ESTABLECE LA CONEXION CON LA BASE DE DATOS PARA LA RECOGIDA DE LOS DATOS Y SE PASA COMO PARAMETRO LA CURP DEL ASPIRANTE
        String URL = "http://"+IP+"/android/fetch1.php?curp=" + curp;
        //EN ESTE METODO SE CAPTURAN LOS RESULTADOS DE LA CONULTA ANTERIOR Y SE TRATA LA INFORMACION CON OBJETOS JSON
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int x = 0; x < response.length(); x++) { //recorre el array para su completa impresion
                    try {//para encuadrado
                        jsonObject = response.getJSONObject(x); //toma el renglon del array y se guarda en variables string
                        EnviaMatricula = jsonObject.getString("Matricula_Enc");
                        EnviaCurp = jsonObject.getString("CURP_Enc");
                        Reserva = false;
                        PopUp("Blanca"); //manda a llamar un metodo para su muestreo en pantalla
                    } catch (JSONException e) {
                    }
                    try {//para reserva
                        jsonObject = response.getJSONObject(x);//toma el renglon del array y se guarda en variables string
                        EnviaMatricula = jsonObject.getString("Matricula_Res");
                        EnviaCurp = jsonObject.getString("CURP_Res");
                        PopUp("Negra");
                    } catch (JSONException e) {//manda a llamar un metodo para su muestreo en pantalla
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // Toast.makeText(Registro.this, "¡ERROR AL INICIAR!: Revise sus datos de inicio.", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    //METODO PARA EL PASE ENTRE INTERFACES
    public void pasaV6(String xd) {
        Intent v1 = new Intent(this, sorteo.class);
        startActivity(v1);
    }

}