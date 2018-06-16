package com.hungpham.Controller;

import com.hungpham.Utils.Utils;
import com.hungpham.database.DatabaseConnector;

import java.util.concurrent.LinkedBlockingQueue;

public class DatabasePush implements Runnable {
    private Utils utils;
    private String data;
    private int conn;
    private DatabaseConnector databaseConnector;
    private String dest;
    private int port;

    public static volatile LinkedBlockingQueue<String>[] acceDBQueue = new LinkedBlockingQueue[2];
    public static volatile LinkedBlockingQueue<String>[] baroDBQueue = new LinkedBlockingQueue[2];

    public DatabasePush(String dest, int conn) {
        this.conn = conn;
        utils = new Utils();
        data = null;
        databaseConnector = new DatabaseConnector();
        switch (dest) {
            case "acce":
                this.dest = "acce_value";
                acceDBQueue[conn] = new LinkedBlockingQueue<>();
                break;
            case "baro":
                this.dest = "baro_value";
                baroDBQueue[conn] = new LinkedBlockingQueue<>();
                break;
            default:
                break;
        }
    }

    public void receiveData() {
        switch (dest) {
            case "acce_value":
                try {
                    data = acceDBQueue[conn].take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case "baro_value":
                try {
                    data = baroDBQueue[conn].take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
        data = data.substring(1);
    }

    public void pushToDB() {
        databaseConnector.writeDB(dest, Double.parseDouble(data), Integer.toString(conn));
    }

    @Override
    public void run() {
        while (true) {
            receiveData();
            pushToDB();
        }
    }
}
