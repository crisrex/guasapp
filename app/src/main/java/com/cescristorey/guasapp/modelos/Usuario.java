package com.cescristorey.guasapp.modelos;

public class Usuario {

    private String uid, nombre, numeroTelefono, imagenPerfil, token;

    public Usuario() {

    }

    public Usuario(String uid, String nombre, String numeroTelefono, String imagenPerfil) {
        this.uid = uid;
        this.nombre = nombre;
        this.numeroTelefono = numeroTelefono;
        this.imagenPerfil = imagenPerfil;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public String getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(String imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
