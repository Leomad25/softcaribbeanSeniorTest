package com.leomad25.messageStore.services;

import com.leomad25.messageStore.MessageStoreApplication;
import com.leomad25.messageStore.models.MessageModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MessageService {
    public MessageModel get(long cedula) {
        return MessageStoreApplication.bPlusTreeMessage.search(cedula);
    }
    public String add(long cedula, String message) {
        if (MessageStoreApplication.bPlusTreeMessage.search(cedula) == null) {
            MessageModel inserData = new MessageModel(cedula, new ArrayList<>());
            inserData.getMessage().add(message);
            MessageStoreApplication.bPlusTreeMessage.insert(inserData.getCedula(), inserData);
            MessageStoreApplication.localDatabase.getMessageTable().update(MessageStoreApplication.bPlusTreeMessage.search(Long.MIN_VALUE, Long.MAX_VALUE));
            return "Message inserted";
        } else {
            MessageStoreApplication.bPlusTreeMessage.search(cedula).getMessage().add(message);
            MessageStoreApplication.localDatabase.getMessageTable().update(MessageStoreApplication.bPlusTreeMessage.search(Long.MIN_VALUE, Long.MAX_VALUE));
            return "Message updated";
        }
    }

    public String delete(long cedula) {
        if (MessageStoreApplication.bPlusTreeMessage.search(cedula) != null) {
            MessageStoreApplication.bPlusTreeMessage.delete(cedula);
            MessageStoreApplication.localDatabase.getMessageTable().update(MessageStoreApplication.bPlusTreeMessage.search(Long.MIN_VALUE, Long.MAX_VALUE));
            return "Message deleted";
        } else {
            return "Message doesn't exists";
        }
    }

    public String delete(long cedula, int pos) {
        if (MessageStoreApplication.bPlusTreeMessage.search(cedula) != null) {
            ArrayList<String> list = MessageStoreApplication.bPlusTreeMessage.search(cedula).getMessage();
            if (list.size() == 1) {
                MessageStoreApplication.bPlusTreeMessage.delete(cedula);
                MessageStoreApplication.localDatabase.getMessageTable().update(MessageStoreApplication.bPlusTreeMessage.search(Long.MIN_VALUE, Long.MAX_VALUE));
            } else {
                if (pos > 0 || pos < list.size()) {
                    list.remove(pos);
                    MessageStoreApplication.bPlusTreeMessage.delete(cedula);
                    MessageStoreApplication.bPlusTreeMessage.insert(cedula, new MessageModel(cedula, list));
                    MessageStoreApplication.localDatabase.getMessageTable().update(MessageStoreApplication.bPlusTreeMessage.search(Long.MIN_VALUE, Long.MAX_VALUE));
                } else return "Message not found";
            }
            return "Message deleted";
        } else {
            return "Message doesn't exists";
        }
    }
}
