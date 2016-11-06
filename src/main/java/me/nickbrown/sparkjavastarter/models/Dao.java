package me.nickbrown.sparkjavastarter.models;

/**
 * Created by ncbrown on 11/6/16.
 */
public interface Dao {
    void createTable();
    /**
     * close with no args is used to close the connection
     */
    void close();
}
