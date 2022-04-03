package com.example.sorteosmn;

//IMPORT: ELEMENTOS DE LA PANTALLA
import  androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
//IMPORT: ELEMENTOS PARA EL TRATAMIENTO DE DATOS EN LA APLICACIÓN CON FORMATO JSON (ARRAY DE ELEMENTOS)
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity{

    //23 y 24 INICIAN EL LLAMADO A LOS METODOS DE PHP PARA EL LOGEO
    RequestQueue requestQueue;
    private static final String url = "http://localhost:3306/android.save.php";

    //LLAMADA A LOS ELEMENTOS DE LA INTERFAZ DE LOGEO EN DONDE SE INTRODUCEN LOS DATOS
    EditText correo, matricula;
    //VARIABLES QUE ALMACENAN LOS DATOS RECIBIDOS EN LOS EDITTEXT
    public static String Correo = "", Matricula = "", tipoA = "";
    public static int desvio = 0;
    //GUARDARA EL RESULTADO DE LA CONSULTA PARA OBTENER LOS DATOS VALIDOS DE LOGEO
    String z = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        correo = (EditText) findViewById(R.id.id_user);
        matricula = (EditText) findViewById(R.id.id_contra);
    }

    /* EN ESTE METODO SE REVISA SI LOS DATOS INGRESADOS EXISTEN
       DENTRO DE LA BASE DE DATOS (ES DECIR SI EL USUARIO YA SE REGISTRO)
       Y TAMBIEN DETECTA EN BASE A LO QUE SE HAYA INSERTADO EN EL USUARIO
       SI ES UN ENCUADRADO, RESERVA O INSTRUCTOR */
    public void readUser(View view) {

        //SE LLAMA A LOS EDITTEXT DECLARADOS EN EL ENTORNO GLOBAL PARA EXTRAER LOS DATOS INGRESADOS
        Correo = correo.getText().toString();
        Matricula = matricula.getText().toString();
        //BANDERA PARA DETECTAR SI ES ENCUADRADO O RESERVA (CAMBIARA SU VALOR EN CASO DE SER UNO U OTRO)
        desvio = 1;

        //SE ESTABLECE LA CONEXION CON LA BASE DE DATOS Y LLEVA LAS VARIABLES HACIA UNA CONSULTA PARA VERIFICAR SU EXISTENCIA
        String URL = "http://192.168.56.1/android/fetchlogin.php?matricula=" + Matricula + "&correo=" + Correo;

        //EN ESTE METODO SE CAPTURAN LOS RESULTADOS DE LA CONULTA ANTERIOR Y SE TRATA LA INFORMACION CON OBJETOS JSON
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int x = 0; x < response.length(); x++) {
                    try {
                        //EN EL OBJETO response SE GUARDA EL RESULTADO DE LA CONSULTA
                        jsonObject = response.getJSONObject(x);
                        //SE CONVIERTE EN UN STRING PARA PODER ANALIZARLO
                        z = response.toString();
                        //DEPENDE DEL RESULTADO DEL ANALISIS DEL STRING INICIARA SESION COMO UN DETERMINADO ROL
                        if (((z.charAt(7)) == ('D')) && (z.charAt(8)) == ('-')) {
                            pasaV3();
                            tipoA = "Encuadrado";
                        } else if (((z.charAt(7)) == ('D')) && (z.charAt(8)) != ('-')) {
                            pasaV3();
                            tipoA = "Reserva";
                        } else if (((z.charAt(7)) == ('A'))) {
                            pasaV4();
                            tipoA = "Admin";
                        }
                    } catch (JSONException e) {
                        Toast.makeText(Login.this, "¡ERROR AL INICIAR!: Revise sus datos de inicio.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this, "¡ERROR AL INICIAR!: Revise sus datos de inicio.", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    //CON ESTOS METODOS SE PASA ENTRE INTERFACES DEPENDE A QUE BOTON SE LE DE CLICK
        //VENTANA DE REGISTRO
    public void pasaV2 (View view) {
        desvio = 2;
        Intent v1 =new Intent(this,Registro.class);
        startActivity(v1);
    }
        //PERFIL PRINCIPAL DEL QUE SE ACABA DE LOGEAR
    public void pasaV3 () {
        Intent v1 =new Intent(this,MainActivity.class);
        startActivity(v1);
    }
        //INTERFAZ PRINCIPAL PARA LOS ADMINISTRADORES
    public void pasaV4 () {
        Intent v1 =new Intent(this,panelControl.class);
        startActivity(v1);
    }

}