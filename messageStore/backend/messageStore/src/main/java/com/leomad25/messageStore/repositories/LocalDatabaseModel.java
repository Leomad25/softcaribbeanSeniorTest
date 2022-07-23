package com.leomad25.messageStore.repositories;

public interface LocalDatabaseModel<T> {
    abstract public String getLocalDBString();
    abstract public T setOfDatabase(String localDBString);
    abstract public long getIdentifier();
}
