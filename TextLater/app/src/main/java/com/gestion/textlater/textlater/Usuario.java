package com.gestion.textlater.textlater;

/**
 * Created by kyonru on 8/04/17.
 */

public class Usuario {
    String nombre;
    String imgUrl;


    public Usuario(String nombre, String imgUrl) {
        this.nombre = nombre;
        this.imgUrl = imgUrl;
    }

    public String getNombre() {
        return nombre;
    }

    public String getImgUrl() {
        return imgUrl;
    }

}
