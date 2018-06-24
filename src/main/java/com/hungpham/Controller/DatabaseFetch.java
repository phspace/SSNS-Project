package com.hungpham.Controller;

import com.hungpham.database.SensorsPoint;
import com.hungpham.database.DatabaseConnector;

import java.util.LinkedList;

public class DatabaseFetch {
    private DatabaseConnector database;
    private LinkedList<SensorsPoint> valueList;
    private int conn;

    /**
     * when create new object of this class, remember to choose the database 0 or 1
     *
     * @param conn
     */
    public DatabaseFetch(int conn) {
        this.conn = conn;
        database = new DatabaseConnector(conn);
        valueList = new LinkedList<>();
    }

    public LinkedList<SensorsPoint> getValueList() {
        return valueList;
    }

    //controller for reading most recent item from now() from database
    public void readAcceMostRecentfromNow(int elapseTime) {
        valueList.clear();
        valueList = database.readDB("acce_value, acc_z, acc_y, acc_x", "(now() - " + elapseTime + "s)", "now()");
    }

    public LinkedList<SensorsPoint> readBaroInTimeInterval(long timeStamp) {
        valueList.clear();
        valueList = database.readDB("baro_value", timeStamp + "ms-6000ms", timeStamp + "ms");
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
