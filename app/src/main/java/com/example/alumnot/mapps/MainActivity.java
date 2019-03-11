package com.example.alumnot.mapps;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alumnot.mapps.base.Evento;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {

    public static List<Evento> eventos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnMapa = findViewById(R.id.btnMapa);
        btnMapa.setOnClickListener(this);
        Button btnAlta = findViewById(R.id.btnDarAlta);
        btnAlta.setOnClickListener(this);
        Button btnListar = findViewById(R.id.btnListar);
        btnListar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.btnMapa:
                intent = new Intent(this,Maps.class);
                startActivity(intent);
                break;
            case R.id.btnListar:
                intent = new Intent(this,Mapa2.class);
                startActivity(intent);
                break;
            case R.id.btnDarAlta:
            intent = new Intent(this,DarAlta.class);
            startActivity(intent);
            break;
        }
    }
    @Override
    protected void onResume() {
        // Recogemos las preferencias del sistema.
        SharedPreferences pref = PreferenceManager
                .getDefaultSharedPreferences(this);
        String nombre;
        TextView algo = findViewById(R.id.textView);
        boolean usingPreferences = pref.getBoolean("usingPreferencies", true);
        if(usingPreferences) {
            nombre =pref.getString("texto","Mapas");

            algo.setText(nombre);
            String prefColor = pref.getString("prefColor", "Azul");

            if (prefColor.equals("Cyan"))
                findViewById(R.id.layoutPrincipal).setBackgroundColor(Color.CYAN);
            else if (prefColor.equals("Azul"))
                findViewById(R.id.layoutPrincipal).setBackgroundColor(Color.BLUE);
            else if (prefColor.equals("Verde"))
                findViewById(R.id.layoutPrincipal).setBackgroundColor(Color.GREEN);
            else
                findViewById(R.id.layoutPrincipal).setBackgroundColor(Color.TRANSPARENT);

        }else {
            findViewById(R.id.layoutPrincipal).setBackgroundColor(Color.WHITE);
            algo.setText("Mapas");
        }
        super.onResume();

    }

}

