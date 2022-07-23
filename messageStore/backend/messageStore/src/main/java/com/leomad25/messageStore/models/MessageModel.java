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
        String[] arr = localDBString.split("\t\t");
        ArrayList<String> messagesList = new ArrayList<>();
        if (arr.length >= 2)  {
            boolean isFirst = true;
            for (String msg: arr) {
                if (!isFirst) {
                    message.add(msg);
                } else isFirst = false;
            }
        }
        return new MessageModel(Long.valueOf(arr[0]).longValue(), messagesList);
    }

    @Override
    public long getIdentifier() {
        return cedula;
    }
}
