package com.leomad25.messageStore.services;

import com.leomad25.messageStore.MessageStoreApplication;
import com.leomad25.messageStore.models.UserModel;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public UserModel get(long cedula) {
        return MessageStoreApplication.bPlusTreeUser.search(cedula);
    }
    public String add(UserModel user) {
        if (MessageStoreApplication.bPlusTreeUser.search(user.getCedula()) == null) {
            MessageStoreApplication.bPlusTreeUser.insert(user.getCedula(), user);
            MessageStoreApplication.localDatabase.getUserTable().update(MessageStoreApplication.bPlusTreeUser.search(Long.MIN_VALUE, Long.MAX_VALUE));
            return "User Inserted";
        } else {
            MessageStoreApplication.bPlusTreeUser.search(user.getCedula()).setNombre(user.getNombre());
            MessageStoreApplication.bPlusTreeUser.search(user.getCedula()).setApellido(user.getApellido());
            MessageStoreApplication.localDatabase.getUserTable().update(MessageStoreApplication.bPlusTreeUser.search(Long.MIN_VALUE, Long.MAX_VALUE));
            return "User updated";
        }
    }
    public String delete(long cedula) {
        if (MessageStoreApplication.bPlusTreeUser.search(cedula) != null) {
            MessageStoreApplication.bPlusTreeUser.delete(cedula);
            MessageStoreApplication.localDatabase.getUserTable().update(MessageStoreApplication.bPlusTreeUser.search(Long.MIN_VALUE, Long.MAX_VALUE));
            return "User deleted";
        } else {
            return "User doesn't exists";
        }
    }
}
