package com.example.alumnot.mapps;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.alumnot.mapps.base.Evento;
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import static com.example.alumnot.mapps.MainActivity.eventos;

public class DarAlta extends AppCompatActivity implements OnMapReadyCallback,MapboxMap.OnMapClickListener,View.OnClickListener {
    MapView mapa;
    MapboxMap mapaBox;
    MarkerOptions marcador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapboxAccountManager.start(this, "pk.eyJ1IjoiZGVjaHJ5c2xlciIsImEiOiJjanNjNW8yZ3IwNmhlNDVueWYzaXNnMzF0In0.eFw8auSp5OEjuUvPqSgoOg");
        setContentView(R.layout.activity_dar_alta);
        mapa = (MapView) findViewById(R.id.map);
        mapa.onCreate(savedInstanceState);
        mapa.getMapAsync(this);
        Button btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(this);
        Button btnSalir = (Button) findViewById(R.id.btnSalir);
        btnSalir.setOnClickListener(this);
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {

        mapaBox = mapboxMap;

        addListener();
    }

    private void addListener() {
        mapaBox.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {
        mapaBox.clear();
      marcador = new MarkerOptions()
                .position(point)
                .title("Evento");
      mapaBox.addMarker(marcador);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnGuardar:
                Toast.makeText(this,"Guardado",Toast.LENGTH_LONG).show();
                recogerDatos();
                break;
            case R.id.btnSalir:
                onBackPressed();
                break;
        }
    }
    public void recogerDatos(){
        Evento evento = new Evento();
        TextView nombre= (TextView) findViewById(R.id.etNombre);
        TextView descripcion= (TextView) findViewById(R.id.etDescripcion);
        evento.setNombre( String.valueOf(nombre.getText()));
        evento.setDescripcion(String.valueOf(descripcion.getText()));
        evento.setLatitud(marcador.getPosition().getLatitude());
        evento.setLongitud(marcador.getPosition().getLongitude());
        eventos.add(evento);
        limpiar();
    }
    public  void limpiar(){
        TextView nombre= (TextView) findViewById(R.id.etNombre);
        TextView descripcion= (TextView) findViewById(R.id.etDescripcion);
        nombre.setText("");
        descripcion.setText("");
        mapaBox.clear();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.item_alta_eventos, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemAltaEventos:
                Intent intent = new Intent(this, Preferencias.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }
}