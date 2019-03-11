package com.example.alumnot.mapps.base;

import java.io.Serializable;

public class Evento implements Serializable {
    private long id;
    private String nombre;
    private String descripcion;
    private double longitud;
    private double latitud;

    public Evento() {
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {

        this.id = id;
    }

    @Override
    public String toString() {
        return nombre+"\n"+descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }
}
