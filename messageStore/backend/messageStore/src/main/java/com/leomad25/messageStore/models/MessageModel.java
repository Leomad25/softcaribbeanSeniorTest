package com.leomad25.messageStore.models;

import java.util.ArrayList;

public class MessageModel {
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
}
