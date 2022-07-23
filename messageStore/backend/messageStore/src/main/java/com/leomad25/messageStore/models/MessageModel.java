package com.leomad25.messageStore.models;

import com.leomad25.messageStore.repositories.LocalDatabaseModel;

import java.util.ArrayList;

public class MessageModel implements LocalDatabaseModel {
    private long cedula;
    private ArrayList<String> message;

    public MessageModel() {}

    public MessageModel(long cedula, ArrayList<String> message) {
        this.cedula = cedula;
        this.message = message;
    }

    public long getCedula() {
        return cedula;
    }

    public void setCedula(long cedula) {
        this.cedula = cedula;
    }

    public ArrayList<String> getMessage() {
        return message;
    }

    public void setMessage(ArrayList<String> message) {
        this.message = message;
    }

    @Override
    public String getLocalDBString() {
        try {
            String str = "";
            str += getCedula();
            str += "/:/";
            for (int i = 0; i < getMessage().size(); i++) {
                if (i != 0) str += "/:/";
                str += getMessage().get(i);
            }
            return str;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public void setOfDatabase(String localDBString) {

    }
}
