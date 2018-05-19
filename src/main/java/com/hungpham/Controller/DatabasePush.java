package com.hungpham.Controller;

import com.hungpham.Utils.Utils;
import com.hungpham.database.DatabaseConnector;

import javax.xml.crypto.Data;

public class DatabasePush implements Runnable {
    private Utils utils;
    private String acclerometer;
    private DatabaseConnector databaseConnector;

    public DatabasePush() {
        utils = new Utils();
        acclerometer = null;
        databaseConnector = new DatabaseConnector();
    }

    public void receiveData() {
        acclerometer = utils.TCPReceive(Definitions.DATABASE_PORT);
    }

    public void pushToDB() {
        databaseConnector.writeDB(Double.parseDouble(acclerometer));
    }

    @Override
    public void run() {
        while (true) {
            receiveData();
            pushToDB();
        }
    }
}
