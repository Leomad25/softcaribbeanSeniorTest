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
            str += "\t";
            for (int i = 0; i < getMessage().size(); i++) {
                if (i != 0) str += "\t";
                str += getMessage().get(i);
            }
            return str;
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public MessageModel setOfDatabase(String localDBString) {
        try {
            String[] arr = localDBString.split("\t");
            ArrayList<String> messagesList = new ArrayList<>();
            long cedula = Long.MIN_VALUE;
            for (String element: arr) {
                if (cedula == Long.MIN_VALUE) {
                    try { cedula = Long.valueOf(element).longValue(); } catch (Exception ex) { return null; }
                } else messagesList.add(element);
            }
            return new MessageModel(cedula, messagesList);
        } catch (Exception ex) {
            System.err.println("Error -> caught bug (Create obj type MessageModel):\n\t" + ex.toString());
            return null;
        }

    }

    @Override
    public long getIdentifier() {
        return cedula;
    }
}
