package com.example.alumnot.mapps;

import android.app.Activity;

import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationServices;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

public class MainActivity extends AppCompatActivity {

    private MapView mapaView;
    private MapboxMap mapa;
    private FloatingActionButton btUbicacion;
    private LocationServices servicioUbicacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapboxAccountManager.start(this, "pk.eyJ1IjoiZGVjaHJ5c2xlciIsImEiOiJjanNjNW8yZ3IwNmhlNDVueWYzaXNnMzF0In0.eFw8auSp5OEjuUvPqSgoOg");
        setContentView(R.layout.activity_main);
        btUbicacion = (FloatingActionButton) findViewById(R.id.btUbicacion);
        mapaView = (MapView) findViewById(R.id.mapaView);
        mapaView.onCreate(savedInstanceState);
        mapaView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mapa = mapboxMap;
                mapboxMap.addMarker(new MarkerOptions()
                        .position(new LatLng(40.4167576, -3.7036814))
                        .title("Madrid")
                        .snippet("Esta es la ciudad de madrid"));
            }
        });
        ubicarUsuario();
    }
        private void ubicarUsuario() {

            servicioUbicacion = LocationServices.getLocationServices(this);


            btUbicacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mapa != null) {
                         Location lastLocation = servicioUbicacion.getLastLocation();
                        toask();
                        if (lastLocation != null)
                            mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation), 16));

                        // Resalta la posición del usuario en el mapa
                        mapa.setMyLocationEnabled(true);
                    }
                }
            });
    }
    public void toask(){
        Toast.makeText(this,"hola",Toast.LENGTH_SHORT).show();
    }
}


