package com.hungpham.Controller;

import com.hungpham.database.AccelerometerPoint;
import com.hungpham.database.DatabaseConnector;
import org.influxdb.InfluxDB;

import java.util.ArrayList;
import java.util.LinkedList;

public class DatabaseFetch {
    private DatabaseConnector database;
    private LinkedList<AccelerometerPoint> valueList;

    public DatabaseFetch() {
        database = new DatabaseConnector();
        valueList = new LinkedList<>();
    }

    public void readDataInTimeInterval(String from, String to) {
        valueList = database.readDB(from, to);
    }

    public void readDataInTimeInterval1(String from, String to) {
        valueList = database.readDB1(from, to);
    }

    //controller for reading most recent item from database
    public void readDataMostRecent(int LIMIT) {
        valueList = database.readDB(LIMIT);
    }

    //controller for reading most recent item from now() from database
    public void readDataMostRecentfromNow(String elapseTime) {
        valueList = database.readDB(elapseTime);
    }

    public LinkedList<AccelerometerPoint> getValueList() {
        return valueList;
    }

}
