package com.hungpham.Controller;

import com.hungpham.Utils.Utils;
import com.hungpham.database.DatabaseConnector;

public class DatabasePush implements Runnable {
    private Utils utils;
    private String data;
    private DatabaseConnector databaseConnector;
    private String dest;
    private int port;

    public DatabasePush(String dest) {
        utils = new Utils();
        data = null;
        databaseConnector = new DatabaseConnector();
        switch (dest) {
            case "acce":
                this.dest = "acce_value";
                port = Definitions.DATABASE_ACCE_PORT;
                break;
            case "baro":
                this.dest = "baro_value";
                port = Definitions.DATABASE_BARO_PORT;
                break;
            default:
                break;
        }
    }

    public void receiveData() {
        data = utils.TCPReceive(port);
    }

    public void pushToDB() {
        databaseConnector.writeDB(dest, Double.parseDouble(data));
    }

    @Override
    public void run() {
        while (true) {
            receiveData();
            pushToDB();
        }
    }
}
