package com.leomad25.messageStore.repositories;

public interface LocalDatabaseModel {
    abstract public String getLocalDBString();
    abstract public void setOfDatabase(String localDBString);
}
