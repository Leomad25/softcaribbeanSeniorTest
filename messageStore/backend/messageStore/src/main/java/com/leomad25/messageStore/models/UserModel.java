package com.leomad25.messageStore.models;

import com.leomad25.messageStore.repositories.LocalDatabaseModel;

public class UserModel implements LocalDatabaseModel<UserModel> {
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
            str += "\t";
            str += getNombre();
            str += "\t";
            str += getApellido();
            return str;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public UserModel setOfDatabase(String localDBString) {
        try {
            String[] arr = localDBString.split("\t");
            return new UserModel(Long.valueOf(arr[0]).longValue(), arr[1], arr[2]);
        } catch (Exception ex) {
            System.err.println("Error -> caught bug (Create obj type UserModel):\n\t" + ex.toString());
            return null;
        }
    }

    @Override
    public long getIdentifier() {
        return cedula;
    }
}
