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

    public LinkedList<SensorsPoint> getValueList() {
        return valueList;
    }

    //controller for reading most recent item from now() from database
    public void readAcceMostRecentfromNow(int elapseTime) {
        valueList.clear();
        valueList = database.readDB("acce_value","(now() - " + elapseTime + "s)", "now()");
    }

    public LinkedList<SensorsPoint> readBaroInTimeInterval(String from, String to) {
        valueList.clear();
        valueList = database.readDB("baro_value", from, to);
        return valueList;
    }

    public LinkedList<SensorsPoint> readAcceInTimeInterval(String from, String to) {
        valueList.clear();
        valueList = database.readDB("acce_value", from, to);
        return valueList;
    }

    public LinkedList<SensorsPoint> readDataInTimeInterval(String from, String to) {
        valueList.clear();
        valueList = database.readDB("*", from, to);
        return valueList;
    }

}
