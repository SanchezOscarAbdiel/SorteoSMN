package com.example.sorteosmn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class stub extends AppCompatActivity {

    TextView tvStatus;
    Button regresar;

    Login objLogin = new Login();
    Registro objRegistro = new Registro();
    panelControl objPanel = new panelControl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stub);

        //tvStatus = (TextView) findViewById(R.id.tvStatus);
       // regresar = (Button) findViewById(R.id.btnRegresaStub);

       // tvStatus.setText(objPanel.Stub);
    }
}