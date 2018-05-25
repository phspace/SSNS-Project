package com.hungpham.Controller;

import com.hungpham.database.SensorsPoint;
import com.hungpham.database.DatabaseConnector;

import java.util.LinkedList;

public class DatabaseFetch {
    private DatabaseConnector database;
    private LinkedList<SensorsPoint> valueList;

    public DatabaseFetch() {
        database = new DatabaseConnector();
        valueList = new LinkedList<>();
    }

    public void readDataInTimeInterval(String from, String to) {
        valueList = database.readDB(from, to);
    }

    //controller for reading most recent item from database
    public void readDataMostRecent(int LIMIT) {
        valueList = database.readDB(LIMIT);
    }

    public LinkedList<SensorsPoint> getValueList() {
        return valueList;
    }

}
