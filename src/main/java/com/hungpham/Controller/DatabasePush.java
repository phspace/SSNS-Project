package com.hungpham.Controller;

import com.hungpham.Utils.Utils;
import com.hungpham.database.DatabaseConnector;

import java.util.concurrent.LinkedBlockingQueue;

public class DatabasePush implements Runnable {
    private Utils utils;
    private String data;
    private int conn;
    private static DatabaseConnector[] databaseConnector = new DatabaseConnector[2];
    private String dest;
    private int port;

    // queues for
    public static volatile LinkedBlockingQueue<String>[] acceDBQueue = new LinkedBlockingQueue[2];
    public static volatile LinkedBlockingQueue<String>[] baroDBQueue = new LinkedBlockingQueue[2];

    public DatabasePush(String dest, int conn) {
        this.conn = conn;
        utils = new Utils();
        data = null;
        databaseConnector[conn] = new DatabaseConnector(conn);
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
    }

    public void pushToDB() {
        // push all accelerometer axes to database
        if (dest.equalsIgnoreCase("acce_value")) {
            int[] loc = new int[3];
            int j = 0;
            for (int i = -1; (i = data.indexOf(" ", i + 1)) != -1; i++) {
                loc[j] = i;
                j++;
            }
            double[] acce = new double[3];
            acce[0] = Double.parseDouble(data.substring(0, loc[0]));
            acce[1] = Double.parseDouble(data.substring(loc[0] + 1, loc[1]));
            acce[2] = Double.parseDouble(data.substring(loc[1] + 1, loc[2]));
            double acceSQRT = Double.parseDouble(data.substring(loc[2] + 1));
            databaseConnector[conn].writeAcceDB(dest, acceSQRT, Integer.toString(conn), acce);
        } else
            databaseConnector[conn].writeDB(dest, Double.parseDouble(data), Integer.toString(conn));
    }

    @Override
    public void run() {
        while (true) {
            receiveData();
            pushToDB();
        }
    }
}
