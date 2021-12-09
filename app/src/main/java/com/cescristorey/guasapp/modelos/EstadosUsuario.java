package com.cescristorey.guasapp.modelos;

import java.util.ArrayList;

public class EstadosUsuario {
    private String nombre, imagenPerfil;
    private long ultimaActualizacion;
    private ArrayList<Estado> estados;

    public EstadosUsuario() {
    }

    public EstadosUsuario(String nombre, String imagenPerfil, long ultimaActualizacion, ArrayList<Estado> estados) {
        this.nombre = nombre;
        this.imagenPerfil = imagenPerfil;
        this.ultimaActualizacion = ultimaActualizacion;
        this.estados = estados;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(String imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

    public long getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(long lastUpdated) {
        this.ultimaActualizacion = lastUpdated;
    }

    public ArrayList<Estado> getEstados() {
        return estados;
    }

    public void setEstados(ArrayList<Estado> estados) {
        this.estados = estados;
    }
}
