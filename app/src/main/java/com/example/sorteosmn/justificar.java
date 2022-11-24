package com.example.sorteosmn;

//IMPORT: ELEMENTOS DE LA PANTALLA
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.Map;
//IMPORT: ELEMENTOS PARA EL TRATAMIENTO DE DATOS EN LA APLICACIÓN CON FORMATO JSON (ARRAY DE ELEMENTOS)
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class justificar extends AppCompatActivity {
    public static sorteo objIP = new sorteo();
    public static String IP = objIP.IP;

    //LLAMADA A METODOS SQL DE LOGEO
    RequestQueue requestQueue;
    Login objL = new Login();
    String Matricula117 = objL.Matricula,link;
    int contador;
    //ELEMENTOS DE INTERFAZ PARA LA CAPTURA DE DATOS
    EditText E4;
    Button enviar;
    TextView numero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_justificar);
        //OBJETO PARA HACER UNA LLAMADA GENERAL A LOS METODOS DE PHP
        requestQueue = Volley.newRequestQueue(this);
        //SE TRAE AL METODO DE CONTEO DE FALTAS
        funa(null);
        //SE CAPTURAN LOS DATOS TRAIDOS POR LOS ELEMENTOS DE LA INTERFAZ EN VARIABLES
        E4 = (EditText) findViewById(R.id.Edoc4);

        enviar = (Button) findViewById(R.id.benviar4);


        /*CUANDO SE HACE CLICK EN EL BOTON DE JUSTIFICAR SE LLAMA A ESTE METODO QUE INSERTARA EL LINK DE LA
          JUSTIFICACION DENTRO DE LA BASE DE DATOS Y EN SU TABLA CORRESPONDIENTE*/
        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SE COMPRUEBA CON EL IF QUE NO SE TENGAN MAS DE 4 JUSTIFICACIONES POR ENCUADRADO (YA QUE ES EL LIMITE)
                if (contador < 5) {
                    //SI SE CUMPLE LA CONDICION ENTONCES SE LLAMA AL METODO PARA INSERTAR LA JUSTIFICACION Y SE REGRESA A LA VENTANA PRINCIPAL
                    link = E4.getText().toString();
                    InsertaJ(null);
                    pasaMain(null);
                } else {
                    //SE MOSTRARA UNA ADVERTENCIA EN CASO DE QUE YA TENGA +4 FALTAS O JUSTIFICACIONES
                    Toast.makeText(justificar.this, "¡ADVERTENCIA!: Se han agotado" + "\n las oportunidades de justificacion.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //CUENTA LA CANTIDAD DE JUSTIFICACIONES QUE HA SUBIDO EL ENCUADRADO
    public void funa(String xd) {
        //SE LLAMA AL METODO SQL QUE REALIZARA LA CONSULTA EN LA TABLA CORRESPONDIENTE LLEVANDO COMO PARAMETRO LA MATRICULA
        String URL = "https://"+IP+"/19590323_SMN/cuentaJ.php?Matricula_Enc=" + Matricula117;
        //INICIA EL TRATAMIENTO DE LOS DATOS MEDIANTE LOS OBJETOS JSON
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int x = 0; x < response.length(); x++) {
                    try {
                        jsonObject = response.getJSONObject(x);
                        contador = Integer.parseInt(jsonObject.getString("Encuadrado"));
                        Toast.makeText(justificar.this, "¡CUIDADO!: Usted cuenta con " + contador + " de 4 justificaciones", Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        Toast.makeText(justificar.this, "¡ADVERTENCIA!: Revise los datos, ERROR: "+e+".", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(justificar.this, "¡ADVERTENCIA!: Revise los datos, ERROR: "+error.getMessage()+".", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    //SI AUN CUMPLE CON LOS CRITERIOS PARA JUSTIFICAR, ENTONCES ESTE METODO HARA LA SUBIDA DEL JUSTIFICANTE
    public void InsertaJ(String xd) {
        //SE HACE EL LLAMADO A SQL PARA INSERTAR LOS DATOS CORRESPONDIENTES EN LA TABLA INDICADA
        System.out.println("Prueba justifica"+link);
        String url = "https://"+IP+"/19590323_SMN/saveJustifica.php?Matricula_Enc="+Matricula117+"&L_DocJust="+link;
        //HACE LA INSERCION A LA TABLA DE JUSTIFICACIONES UNA VEZ REALIZADO EL LLAMADO A SQL
        StringRequest stringRequest = new StringRequest(
            Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(justificar.this, "¡EXITO!: Se ha mandado su justificación", Toast.LENGTH_LONG).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(justificar.this, "¡ADVERTENCIA!: Revise los datos, ERROR: "+error.getMessage()+".", Toast.LENGTH_LONG).show();
                }
            }
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Matricula_Enc", Matricula117);
                params.put("L_DocJust", link);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void PopUpInfo(View view) {
        AlertDialog.Builder alerta = new AlertDialog.Builder(justificar.this);
        alerta.setMessage("INSTRUCCIONES: "+"\n\n-Escanea en formato pdf el documento justificatorio\n-Sube dicho documento a Google Drive con formato de nombre 'Matricula_Nombre'\n-Obten un enlace publico del archivo\nPega dicho enlace en el espacio correspondiente y envialo\n\nSolo se dispone de 4 intentos totales").setCancelable(false)
                .setNeutralButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
        AlertDialog titulo = alerta.create();
        titulo.setTitle("JUSTIFICACION DE FALTAS");
        titulo.show();
    }

    //METODOS PARA PASAR DE INTERFAZ (PERFIL)
    public void pasaMain (String xd) {
        Intent v1 =new Intent(this,MainActivity.class);
        startActivity(v1);
    }
    public void pasaMain1 (String xd) {
        Intent v1 =new Intent(this,MainActivity.class);
        startActivity(v1);
    }

}