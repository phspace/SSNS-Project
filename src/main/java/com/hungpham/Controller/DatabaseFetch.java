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
        valueList = database.readDB("acce_value","(now() - " + elapseTime + "s)", "now()");
    }

    public LinkedList<SensorsPoint> readBaroInTimeInterval(long UTVtimestamp) {
        valueList.clear();
        valueList = database.readDB("baro_value", UTVtimestamp+"-3s",UTVtimestamp+"+3s" );
        return valueList;
    }
    public LinkedList<SensorsPoint> readBaroInFirst2s(long UTVtimestamp ) {
        valueList.clear();
        valueList = database.readDB("baro_value", UTVtimestamp+"-3s",UTVtimestamp+"-1s" );
        return valueList;
    }
    public LinkedList<SensorsPoint> readBaroInNext2s(long UTVtimestamp ) {
        valueList.clear();
        valueList = database.readDB("baro_value", UTVtimestamp+"-1s",UTVtimestamp+"+1s" );
        return valueList;
    }
    public LinkedList<SensorsPoint> readBaroInLast2s(long UTVtimestamp ) {
        valueList.clear();
        valueList = database.readDB("baro_value", UTVtimestamp+"+1s",UTVtimestamp+"+3s" );
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
