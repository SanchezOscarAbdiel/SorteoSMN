package com.example.sorteosmn;

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
import android.widget.TextView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class Registro extends AppCompatActivity {
    //------------------------------------------------------------------------------
    RequestQueue requestQueue;
    public static final String url = "http://192.168.56.1/android/save.php"; // hay que cambiar la ip
// ------------------------------------------------------------------------------
    public Spinner spinner;
    public Spinner spEstadoc;
    public Spinner Discapacidad;

    public RadioGroup Sexo; public RadioButton Masculino, Femenino;
   public static EditText Nombre,ApellidoP,ApellidoM,Curp,Edad,NumEx,NumIn,Calle,Ciudad,Profesion,Correo,Resultado;
    public static String edad,nombre,apellidop,apellidom,curp,num_ext,num_int,calle,colonia,ciudad,estado_civ,profesion,sexo,discapacidad,correo;
  public static  int Sexo1;
    public static String EnviaCurp,EnviaMatricula,EnviaBola; //variables consultas
    Boolean Reserva = true;
// ------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        requestQueue = Volley.newRequestQueue(this);

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
        Resultado =(EditText)findViewById(R.id.edResultado);
        //--------------------LLAMA LLENADO DE SPINNERS
       colonia(null);
       EstadoCivil(null);
       Discapacidad(null);
    }

    //======================INGRESO DATOS A SPINNERS==========================

    public void colonia (String xd){ //rellena un spinner en base al archivo txt

        requestQueue = Volley.newRequestQueue(this);
        spinner = (Spinner) findViewById(R.id.spColonia);

        List<String> spin = new ArrayList<String>();
        String linea = null;

        InputStream is =this.getResources().openRawResource(R.raw.colonias);
        BufferedReader reader =new BufferedReader(new InputStreamReader(is));
        if (is!=null){
            while (true){
                try {
                    if (!((linea=reader.readLine())!=null)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                spin.add(linea.split(",")[0]);
            }
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String datos[] = spin.toArray(new String[spin.size()]);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, datos);
        spinner.setAdapter(adapter);

    }

    public void EstadoCivil(String xd){ //rellena spinner
        ArrayAdapter<CharSequence>adapterE=ArrayAdapter.createFromResource(this,R.array.combo_Estado, android.R.layout.simple_spinner_item);
        spEstadoc.setAdapter(adapterE);
    }

    public void Discapacidad(String xd){ //rellena spinner
        ArrayAdapter<CharSequence>adapterE=ArrayAdapter.createFromResource(this,R.array.combo_discapacidad, android.R.layout.simple_spinner_item);
        Discapacidad.setAdapter(adapterE);
    }

    //===========================INSERCIO EN BASE DE DATOS=====================================
    public void createUser(View view){

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
        if(sexo == "(M) Masculino")
            sexo = "F";
        else
            sexo = "M";
        discapacidad = Discapacidad.getSelectedItem().toString();
        correo = Correo.getText().toString();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Registro.this, "correct", Toast.LENGTH_LONG).show();
                        System.out.println("response "+response);

                        readUser(null);


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Registro.this, "Revise sus datos.", Toast.LENGTH_LONG).show();
                        System.out.println("error "+error);
                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                params.put("nombre", nombre);
                params.put("apellidop", apellidom);
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
                System.out.println("hola "+nombre+" "+apellidop+" "+apellidom+" "+curp+" "+edad+" "+num_ext+" "+num_int+" "+calle+" "+colonia+" "+ciudad+" "+estado_civ+" "+profesion+" "+sexo+" "+discapacidad+" "+correo);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void PopUp(String bola){ //muestra la bola (blanca,negra) resultante
        AlertDialog.Builder alerta = new AlertDialog.Builder(Registro.this);
        alerta.setMessage("Felicidades, usted es bola: "+bola+"\nSerá dirigido a su hoja de datos.").setCancelable(false)
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

private void readUser (String xd){ //guarda los datos del aspirante necesarios para su muestreo en el resumen
    String URL ="http://192.168.56.1/android/fetch1.php?curp="+curp;
      JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
          @Override
          public void onResponse(JSONArray response) {
              JSONObject jsonObject = null;

              for (int x = 0; x < response.length(); x++) {
                  try {
                      jsonObject = response.getJSONObject(x);
                          Resultado.setText(jsonObject.getString("Matricula_Enc") + jsonObject.getString("CURP_Enc"));
                          EnviaMatricula = jsonObject.getString("Matricula_Enc"); EnviaCurp = jsonObject.getString("CURP_Enc");
                          System.out.println("Curp: "+EnviaCurp+" MATRICULA "+EnviaMatricula);
                        Reserva = false;
                      PopUp("Blanca");

                  } catch (JSONException e) {
                      System.out.println("Error1 " + e.getMessage());
                  } try{
                      jsonObject = response.getJSONObject(x);
                      Resultado.setText(jsonObject.getString("Matricula_Res") + jsonObject.getString("CURP_Res"));
                      EnviaMatricula = jsonObject.getString("Matricula_Res"); EnviaCurp = jsonObject.getString("CURP_Res");
                      System.out.println("Curp: "+EnviaCurp+" MATRICULA "+EnviaMatricula);
                      //Reserva = true;
                      PopUp("Negra");
                  } catch(JSONException e) {
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

    private void ReadUser(String xd){ //metodo no usado
        String URL ="http://192.168.56.1/android/fetch.php?curp=jijija";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String CURP_Enc,Matricula_Enc;
                        try {
                            CURP_Enc =  response.getString("CURP_Enc");
                            Matricula_Enc = response.getString("Matricula_Enc");

                            System.out.println(Matricula_Enc);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("errorss "+error);
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    } //codigo no servible
    // ------------------------------------------------------------------------------
    public void pasaV6 (String xd) {
       Intent v1 =new Intent(this,sorteo.class);
        startActivity(v1);
    }

}