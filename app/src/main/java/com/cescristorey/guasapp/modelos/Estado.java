package com.cescristorey.guasapp.modelos;

public class Estado {
    private String urlImagen;
    private long timeStamp;

    public Estado() {
    }

    public Estado(String urlImagen, long timeStamp) {
        this.urlImagen = urlImagen;
        this.timeStamp = timeStamp;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
