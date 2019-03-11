package com.example.alumnot.mapps;

import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.Marker;
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

import static com.example.alumnot.mapps.MainActivity.eventos;

public class Mapa2 extends AppCompatActivity implements OnMapReadyCallback ,MapboxMap.OnMarkerClickListener,View.OnClickListener {
    MapView mapa;
    MapboxMap mapabox;
    private FloatingActionButton btUbicacion;
    private LocationServices servicioUbicacion;
    private MarkerOptions marcador;
    DirectionsRoute ruta;

    public Mapa2() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapboxAccountManager.start(this, "pk.eyJ1IjoiZGVjaHJ5c2xlciIsImEiOiJjanNjNW8yZ3IwNmhlNDVueWYzaXNnMzF0In0.eFw8auSp5OEjuUvPqSgoOg");
        setContentView(R.layout.activity_mapa2);
        mapa = (MapView) findViewById(R.id.mapa);
        mapa.onCreate(savedInstanceState);
        marcador=new MarkerOptions()
                .position(new LatLng(40.4167576, -3.7036814))
                .title("Madrid")
                .snippet("Esta es la ciudad de madrid");
        btUbicacion=(FloatingActionButton) findViewById(R.id.btUbicacion);
        btUbicacion.setOnClickListener(this);
        mapa.getMapAsync(this);
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        mapabox = mapboxMap;
        servicioUbicacion = LocationServices.getLocationServices(this);
        mapabox.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(servicioUbicacion.getLastLocation()), 14));
        mapabox.setMyLocationEnabled(true);
        mapabox.addMarker(marcador);
        if (eventos.size() > 0){
            mapboxMap.addMarker(new MarkerOptions()
                    .position(new LatLng( eventos.get(0).getLatitud(),eventos.get(0).getLongitud()))
                    .title(eventos.get(0).getNombre())
                    .snippet(eventos.get(0).getDescripcion()));
    }
        mapboxMap.setOnMarkerClickListener(this);
        btUbicacion.setVisibility(View.INVISIBLE);
        servicioUbicacion.addLocationListener(new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mapabox.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(servicioUbicacion.getLastLocation()),13));
                mapabox.setMyLocationEnabled(true);
            }
        });
    }



    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        btUbicacion.setVisibility(View.VISIBLE);
        return Boolean.parseBoolean(null);

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

             ruta= response.body().getRoutes().get(0);
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
        mapabox.addPolyline(new PolylineOptions()
                .add(puntos)
                .color(Color.parseColor("#009688"))
                .width(5));

        // Resalta la posición del usuario si no lo estaba ya
        if (!mapabox.isMyLocationEnabled())
            mapabox.setMyLocationEnabled(true);
    }

    @Override
    public void onClick(View view) {
        try {
            obtenerRuta(marcador.getPosition(),mapabox.getMyLocation());
        } catch (ServicesException e) {
            e.printStackTrace();
        }
    }
}
