package com.example.sorteosmn;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Vector;

public class Registro extends AppCompatActivity {

    //------------------------------------------------------------------------------
    RequestQueue requestQueue;
    private static final String url = "http://localhost:3306/android.save.php";
// ------------------------------------------------------------------------------
    private Spinner spinner;
    public Spinner spEstadoc;
    public RadioGroup sexo; public RadioButton Masculino, Femenino;
    EditText Nombre,ApellidoP,ApellidoM,Curp,Edad,NumEx,NumIn,Calle,Ciudad,Profesion,Discapacidad,Correo;
    String Nombre1,ApellidoP1,ApellidoM1,Curp1,Edad1,NumEx1,NumIn1,Calle1,Colonia1,Ciudad1,EstadoCivil1,Profesion1,Sexo2,Discapacidad1,Correo1;
    int Sexo1;
// ------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        requestQueue = Volley.newRequestQueue(this);

        Spinner spinner = (Spinner) findViewById(R.id.spColonia);
        spEstadoc=(Spinner) findViewById(R.id.spEstadoc);
        sexo=(RadioGroup) findViewById(R.id.rgSexo);
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
        Discapacidad= (EditText)findViewById(R.id.etDiscapacidad);
        Correo= (EditText)findViewById(R.id.etCorreo);

        Nombre1 = Nombre.getText().toString();
        ApellidoP1=ApellidoP.getText().toString();
        ApellidoM1 = ApellidoM.getText().toString();
        Curp1 = Curp.getText().toString();
        Edad1 = Edad.getText().toString();
        NumEx1 = NumEx.getText().toString();
        NumIn1 = NumIn.getText().toString();
        Calle1 = Calle.getText().toString();
        Colonia1 = spinner.getSelectedItem().toString();
        Ciudad1 = Ciudad.getText().toString();
        EstadoCivil1 = spEstadoc.getSelectedItem().toString();
        Profesion1=Profesion.getText().toString();
        Sexo1 = sexo.getCheckedRadioButtonId();
        Masculino = findViewById(Sexo1);
        Sexo2 = Masculino.getText().toString();
        if(Sexo2 == "(M) Masculino")
            Sexo2 = "M";
        else
            Sexo2 = "F";
        Discapacidad1 = Discapacidad.getText().toString();
        Correo1 = Correo.getText().toString();

       colonia(null);
       EstadoCivil(null);

    }

public void colonia (String xd){
    requestQueue = Volley.newRequestQueue(this);


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
    //==================================================================================

    public void EstadoCivil(String xd){

        ArrayAdapter<CharSequence>adapterE=ArrayAdapter.createFromResource(this,R.array.combo_Estado, android.R.layout.simple_spinner_item);

        spEstadoc.setAdapter(adapterE);
    }

    //==================================================================================
    public void createUser(View view){

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(Registro.this, "correct", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("nombre", Nombre1);
                params.put("apellidop", ApellidoP1);
                params.put("apellidom", ApellidoM1);
                params.put("curp", Curp1);
                params.put("edad", Edad1);
                params.put("num_ext", NumEx1);
                params.put("num_int", NumIn1);
                params.put("calle", Calle1);
                params.put("colonia", Colonia1);
                params.put("ciudad", Ciudad1);
                params.put("estado_civ", EstadoCivil1);
                params.put("profesion", Profesion1);
                params.put("sexo", Sexo2);
                params.put("discapacidad", Discapacidad1);
                params.put("correo", Correo1);
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }
    // ------------------------------------------------------------------------------
    public void pasaV6 (View view) {
        Intent v1 =new Intent(this,sorteo.class);
        startActivity(v1);
    }


}