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

    public LinkedList<SensorsPoint> getValueList() {
        return valueList;
    }

    //controller for reading most recent item from now() from database
    public void readDataMostRecentfromNow(int elapseTime) {
        valueList = database.readDB("(now() - " + elapseTime + "s)", "now()");
    }

    public void readBaroInTimeInterval(String from, String to) {
        valueList = database.readDB("baro_value", from, to);
    }

    public void readAcceInTimeInterval(String from, String to) {
        valueList = database.readDB("acce_value", from, to);
    }

}
