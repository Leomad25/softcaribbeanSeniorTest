package com.leomad25.messageStore.lib;

import com.leomad25.messageStore.MessageStoreApplication;
import com.leomad25.messageStore.models.MessageModel;
import com.leomad25.messageStore.models.UserModel;
import com.leomad25.messageStore.repositories.LocalDatabaseModel;

import java.io.*;
import java.util.ArrayList;
import java.util.stream.Stream;

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
                            "itemsCount:0" + "\n" +
                            "minItemId:0" + "\n" +
                            "maxItemId:0";
                    FileWriter write = new FileWriter(info);
                    write.write(contentInfo);
                    write.close();
                }
            } catch (IOException e) {
                System.err.println(e.toString());
                info = null;
            }
        }

        public ArrayList<T> getList(T newModel) {
            ArrayList<T> arr = new ArrayList<>();
            if (info != null && data != null) {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(data));
                    String line;
                    while ((line = bufferedReader.readLine ()) != null) {
                        if (line.length() == 0) continue;
                        newModel = (T) newModel.setOfDatabase(line);
                        arr.add(newModel);
                    }
                    bufferedReader.close();
                } catch (Exception ex) {
                    System.err.println("Error (Read into local database):\n" + ex);
                }

            }
            return arr;
        }

        public void update(ArrayList<T> items) {
            if (info != null && data != null) {
                try {
                    // Write on Data File
                    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(data));
                    int itemsCount = 0;
                    long minValue = 0, maxValue = 0;
                    for (T item: items) {
                        bufferedWriter.write(item.getLocalDBString());
                        bufferedWriter.newLine();
                        itemsCount++;
                        if (minValue > item.getIdentifier()) minValue = item.getIdentifier();
                        if (maxValue < item.getIdentifier()) maxValue = item.getIdentifier();
                    }
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    // Read Info File
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(info));
                    String line;
                    ArrayList<String[]> listSplit = new ArrayList<>();
                    while ((line = bufferedReader.readLine ()) != null) {
                        listSplit.add(line.split(":"));
                    }
                    bufferedReader.close();
                    // Write on Info File
                    bufferedWriter = new BufferedWriter(new FileWriter(info));
                    for (int i = 0; i < listSplit.size(); i++) {
                        if (i != 0) bufferedWriter.newLine();
                        if (listSplit.get(i)[0].equals("itemsCount")) bufferedWriter.write("itemsCount:" + itemsCount);
                        if (listSplit.get(i)[0].equals("minItemId")) bufferedWriter.write("minItemId:" + minValue);
                        if (listSplit.get(i)[0].equals("maxItemId")) bufferedWriter.write("maxItemId:" + maxValue);
                    }
                    bufferedWriter.flush();
                    bufferedWriter.close();
                } catch (IOException ex) {
                    System.err.println("Error (Writing into local database):\n" + ex);
                }
            }
        }
    }
}
