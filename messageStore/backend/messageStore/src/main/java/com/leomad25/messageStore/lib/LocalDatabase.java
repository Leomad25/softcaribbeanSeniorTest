package com.leomad25.messageStore.lib;

import com.leomad25.messageStore.MessageStoreApplication;
import com.leomad25.messageStore.models.MessageModel;
import com.leomad25.messageStore.models.UserModel;
import com.leomad25.messageStore.repositories.LocalDatabaseModel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class LocalDatabase {
    // < - Panel_of_tables - >
    /*
     * Introduction...
     *     private Table<here_insert_the_model> indicative = new Table<>("the_store_name_folder");
     *     public Table getIndicative() { return indicative; }
     * Important...
     *  - The Model need have a personalized method toString().
     *  - The Primary key have to be first on toString() method.
     * CREATE TABLE HERE DOWN...
     */
    private Table<UserModel> userTable = new Table<>("users");
    private Table<MessageModel> messageTable = new Table<>("messages");
    // Getters Table
    public Table getUserTable() {
        return userTable;
    }
    public Table getMessageTable() {
        return messageTable;
    }
    // < - Panel_of_tables - >

    public class Table<T extends LocalDatabaseModel> {
        private final String extName = ".local-db";
        private File tableFolder, data, info;

        public Table(String name) {
            tableFolder = new File("database/" + name);
            data = new File(tableFolder.getAbsolutePath() + "/Data" + extName);
            info = new File(tableFolder.getAbsolutePath() + "/Info" + extName);
            // create folders
            if (!tableFolder.exists()) tableFolder.mkdirs();
            // table of data
            try {
                if (!(data.exists())) if (!(data.createNewFile())) data = null;
            } catch (IOException e) {
                System.err.println(e.toString());
                data = null;
            }
            // table of info
            try {
                if (!(info.exists())) if (!(info.createNewFile())) { info = null; } else {
                    String contentInfo =
                            "itemsCount:0";
                    FileWriter write = new FileWriter(info);
                    write.write(contentInfo);
                    write.close();
                }
            } catch (IOException e) {
                System.err.println(e.toString());
                info = null;
            }
        }

        public ArrayList<T> getList() {
            ArrayList<T> arr = new ArrayList<>();
            if (info != null && data != null) {

            }
            return arr;
        }

        public boolean update(ArrayList<T> items) {
            if (info != null && data != null) {
                items.forEach((e) -> {
                    System.out.println(e.getLocalDBString());
                });
            }
            return false;
        }
    }
}
