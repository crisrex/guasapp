package com.cescristorey.guasapp.modelos;

public class Mensaje {
    private String mensajeId, textoMensaje, emisorId, urlImagen;
    private long timestamp;
    private int animo;

    public Mensaje() {
    }

    public Mensaje(String message, String emisorId, long timestamp) {
        this.textoMensaje = message;
        this.emisorId = emisorId;
        this.timestamp = timestamp;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public String getMensajeId() {
        return mensajeId;
    }

    public void setMensajeId(String mensajeId) {
        this.mensajeId = mensajeId;
    }

    public String getTextoMensaje() {
        return textoMensaje;
    }

    public void setTextoMensaje(String textoMensaje) {
        this.textoMensaje = textoMensaje;
    }

    public String getEmisorId() {
        return emisorId;
    }

    public void setEmisorId(String emisorId) {
        this.emisorId = emisorId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getAnimo() {
        return animo;
    }

    public void setAnimo(int animo) {
        this.animo = animo;
    }


}
