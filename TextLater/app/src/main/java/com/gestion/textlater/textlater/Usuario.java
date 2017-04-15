package com.gestion.textlater.textlater;

import android.util.Log;

/**
 * Created by kyonru on 8/04/17.
 */

public class Usuario {
    static String nombre;
    static String imgUrl;
    UserPublicInformation info;

    public Usuario(String nombre, String imgUrl) {
        this.nombre = nombre;
        this.imgUrl = imgUrl;
        info = new UserPublicInformation();
    }

    public void setInformation(){
        info.init();
        nombre = info.getNombreCmpleto();
        imgUrl = info.getUrlImage();
        Log.e("hola", nombre+imgUrl);
    }



    public String getNombre() {
        return nombre;
    }

    public String getImgUrl() {
        return imgUrl;
    }

}
