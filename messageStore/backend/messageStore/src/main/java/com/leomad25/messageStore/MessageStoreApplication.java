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
	}
}
