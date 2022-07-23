package com.leomad25.messageStore.lib;

import com.leomad25.messageStore.models.MessageModel;
import com.leomad25.messageStore.models.UserModel;

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

    public class Table<T> {
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
                            "itemsCount:0\n" +
                            "lastId:0";
                    FileWriter write = new FileWriter(info);
                    write.write(contentInfo);
                    write.close();
                }
            } catch (IOException e) {
                System.err.println(e.toString());
                info = null;
            }
        }

        public T select(String key) {
            if (info != null && data != null) {

            }
            return null;
        }

        public ArrayList<T> selectAll() {
            if (info != null && data != null) {

            }
            return null;
        }

        public boolean insert(T item) {
            if (info != null && data != null) {
                System.out.println("insert element: " + item.toString());
            }
            return false;
        }

        public boolean update(T item) {
            if (info != null && data != null) {

            }
            return false;
        }

        public boolean delete(String key) {
            if (info != null && data != null) {

            }
            return false;
        }
    }
}
