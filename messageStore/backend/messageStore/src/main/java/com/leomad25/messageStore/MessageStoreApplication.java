package com.leomad25.messageStore;

import com.leomad25.messageStore.lib.BPlusTree.MessageBPlusTree;
import com.leomad25.messageStore.lib.BPlusTree.UserBPlusTree;
import com.leomad25.messageStore.lib.LocalDatabase;
import com.leomad25.messageStore.models.MessageModel;
import com.leomad25.messageStore.models.UserModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;

@SpringBootApplication
public class MessageStoreApplication {
	public static UserBPlusTree bPlusTreeUser;
	public static MessageBPlusTree bPlusTreeMessage;
	public static LocalDatabase localDatabase;

	public static void main(String[] args) {
		SpringApplication.run(MessageStoreApplication.class, args);
		// BPlusTree
		MessageStoreApplication.bPlusTreeUser = new UserBPlusTree(100);
		MessageStoreApplication.bPlusTreeMessage = new MessageBPlusTree(100);
		// Local database
		MessageStoreApplication.localDatabase = new LocalDatabase();
		// Load from local database
		ArrayList<UserModel> listUsers = MessageStoreApplication.localDatabase.getUserTable().getList(new UserModel());
		ArrayList<MessageModel> listMessages = MessageStoreApplication.localDatabase.getMessageTable().getList(new MessageModel());
		if (listUsers.size() > 0) listUsers.forEach((e) -> { MessageStoreApplication.bPlusTreeUser.insert(e.getCedula(), e); });
		if (listMessages.size() > 0) listMessages.forEach((e) -> { MessageStoreApplication.bPlusTreeMessage.insert(e.getCedula(), e); });

		/**
		 * =================
		 *  DATOS DE PRUEBA
		 * =================
		 */
		/*
		//Generate Ramdoms UsersModels
		ArrayList<Long> cedulas = new ArrayList<>();
		long topNum = 0;
		for (int i = 0; i < 6000; i++) {
			long cc = 1L;
			for (int j = 0 ; j < 60; j++) {
				int multi = Math.abs((int)Math.floor(Math.random() * 10));
				if (multi != 0) cc *= multi;
			}
			cc = Math.abs(cc);
			if (topNum < cc) topNum = cc;
			if (cedulas.size() == 0) {
				cedulas.add(Long.valueOf(cc));
			} else {
				boolean isEqual = false;
				for (int j = 0; j < cedulas.size(); j++) if (cedulas.get(j).longValue() == cc) {
					isEqual = true;
					break;
				}
				if (!isEqual) {
					cedulas.add(Long.valueOf(cc));
				} else {
					i--;
				}
			}
		}
		*/
		/*
		// insert to B Tree
		//System.out.println("Number Top => " + topNum);
		for (int i = 0; i < cedulas.size(); i++) {
			UserModel user = new UserModel(cedulas.get(i).longValue(), "Nombre " + i, "Apellido " + i);
			MessageStoreApplication.bPlusTreeUser.insert(user.getCedula(), user);
		}
		*/
		/*
		// Print of B tree
		MessageStoreApplication.bPlusTreeUser.search(Long.MIN_VALUE, Long.MAX_VALUE).forEach((e) -> {
			System.out.println(e.getCedula());
		});
		*/
		/*
		// update to local database;
		MessageStoreApplication.localDatabase.getUserTable().update(MessageStoreApplication.bPlusTreeUser.search(Long.MIN_VALUE, Long.MAX_VALUE));
		ArrayList<UserModel> listLocalDatabase = MessageStoreApplication.localDatabase.getUserTable().getList(new UserModel());
		listLocalDatabase.forEach((e) -> { System.out.println(e.getCedula() + ", " + e.getNombre() + " " + e.getApellido()); });
		*/
	}
}
