package com.leomad25.messageStore.models;

import com.leomad25.messageStore.repositories.LocalDatabaseModel;

public class UserModel implements LocalDatabaseModel {
    private long cedula;
    private String nombre, apellido;

    public UserModel() {
    }

    public UserModel(long cedula, String nombre, String apellido) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public long getCedula() {
        return cedula;
    }

    public void setCedula(long cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    @Override
    public String getLocalDBString() {
        try {
            String str = "";
            str += getCedula();
            str += "/:/";
            str += getNombre();
            str += "/:/";
            str += getApellido();
            return str;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public void setOfDatabase(String localDBString) {

    }
}
