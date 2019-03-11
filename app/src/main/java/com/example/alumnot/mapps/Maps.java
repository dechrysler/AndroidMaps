package com.example.alumnot.mapps;


import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationListener;
import com.mapbox.mapboxsdk.location.LocationServices;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.Constants;
import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.directions.v5.DirectionsCriteria;
import com.mapbox.services.directions.v5.MapboxDirections;
import com.mapbox.services.directions.v5.models.DirectionsResponse;
import com.mapbox.services.directions.v5.models.DirectionsRoute;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Maps extends AppCompatActivity implements MapboxMap.OnMapClickListener {
    private MapView mapaView;
    private MapboxMap mapa;
    private FloatingActionButton btUbicacion;
    private LocationServices servicioUbicacion;
    private DirectionsRoute ruta;
    private LatLng  marquet=new LatLng(40.4167576, -3.7036814);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapboxAccountManager.start(this, "pk.eyJ1IjoiZGVjaHJ5c2xlciIsImEiOiJjanNjNW8yZ3IwNmhlNDVueWYzaXNnMzF0In0.eFw8auSp5OEjuUvPqSgoOg");
        setContentView(R.layout.activity_maps);
        btUbicacion = (FloatingActionButton) findViewById(R.id.btUbicacion);
        mapaView = (MapView) findViewById(R.id.mapaView);

        mapaView.onCreate(savedInstanceState);
        mapaView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mapa = mapboxMap;
                mapboxMap.addMarker(new MarkerOptions()
                        .position(marquet)
                        .title("Madrid")
                        .snippet("Esta es la ciudad de madrid"));
                anadirListener();
            }

        });
        ubicarUsuario();

    }
    private void ubicarUsuario() {

        servicioUbicacion = LocationServices.getLocationServices(this);
        servicioUbicacion.addLocationListener(new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (mapa != null) {
                    Location lastLocation = servicioUbicacion.getLastLocation();
                    if (lastLocation != null)
                        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation), 16));

                    // Resalta la posición del usuario en el mapa
                    mapa.setMyLocationEnabled(true);

                }
            }
        });
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
    private void anadirListener(){
        mapa.setOnMapClickListener(this);
    }
    public void toask(){
        Toast.makeText(this,"hola",Toast.LENGTH_SHORT).show();
    }
    public void toaskw( DirectionsRoute ruta){
        Toast.makeText(this,"Distancia:"+ruta.getDistance(),Toast.LENGTH_SHORT).show();
    }

    private void obtenerRuta(LatLng markerLocation, Location userLocation) throws ServicesException {

        Position posicionMarker = Position.fromCoordinates(markerLocation.getLongitude(), markerLocation.getLatitude());
        Position posicionUsuario = Position.fromCoordinates(userLocation.getLongitude(), userLocation.getLatitude());

        // Obtiene la dirección entre los dos puntos
        MapboxDirections direccion = new MapboxDirections.Builder()
                .setOrigin(posicionMarker)
                .setDestination(posicionUsuario)
                .setProfile(DirectionsCriteria.PROFILE_CYCLING)
                .setAccessToken(MapboxAccountManager.getInstance().getAccessToken())
                .build();

        direccion.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {

                 ruta = response.body().getRoutes().get(0);
                toaskw(ruta);

                pintarRuta(ruta);
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                // Qué hacer en caso de que falle el cálculo de la ruta
            }
        });
    }
    private void pintarRuta(DirectionsRoute ruta) {

        // Recoge los puntos de la ruta
        LineString lineString = LineString.fromPolyline(ruta.getGeometry(), Constants.OSRM_PRECISION_V5);
        List<Position> coordenadas = lineString.getCoordinates();
        LatLng[] puntos = new LatLng[coordenadas.size()];
        for (int i = 0; i < coordenadas.size(); i++) {
            puntos[i] = new LatLng(coordenadas.get(i).getLatitude(), coordenadas.get(i).getLongitude());
        }

        // Pinta los puntos en el mapa
        mapa.addPolyline(new PolylineOptions()
                .add(puntos)
                .color(Color.parseColor("#009688"))
                .width(5));

        // Resalta la posición del usuario si no lo estaba ya
        if (!mapa.isMyLocationEnabled())
            mapa.setMyLocationEnabled(true);
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {
        try {

            obtenerRuta(mapa.getMarkers().get(0).getPosition(),mapa.getMyLocation());
            mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(marquet, 10));
        } catch (ServicesException e) {
            e.printStackTrace();
        }
    }
}


