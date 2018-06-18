package com.hungpham.Data;

import com.hungpham.Controller.Definitions;
import com.hungpham.MainApplication;
import com.hungpham.Utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import static com.hungpham.Controller.Definitions.*;

/**
 * This class is made to fetch new data from data queue and notify observers
 */

public class SerialData implements Runnable {
    private volatile String rawData;
    private String completePackage;
    private Utils utils;

    public static volatile LinkedBlockingQueue<String>[] dataPackage = new LinkedBlockingQueue[2];

    private String firstPackage;
    private String secondPackage;

    private Thread seperateData;

    private int conn;

    public SerialData(int conn) {
        this.conn = conn;
        dataPackage[conn] = new LinkedBlockingQueue<>();
        utils = new Utils();
        rawData = null;
        completePackage = null;
        firstPackage = "";
        secondPackage = "";
        seperateData = new Thread(this);
        seperateData.start();
    }

    public String getData() {
        return rawData;
    }


    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        while (true) {
            String packages;
            String accePackage;
            String baroPackage;
            try {
                packages = dataPackage[conn].take();
//                System.out.println(packages);
                for (int i = -1; (i = packages.indexOf(ACCE_NOTIFY, i + 1)) != -1; i++) {
                    try {
                        accePackage = packages.substring(i + 10, i + 48);
//                    String conn = accePackage.substring(0, 4);
                        String acceData = accePackage.substring(24, 36);
//                    int c = Integer.parseInt(conn);
                        AcceProcessing.acceQueue[conn].add(acceData);
//                    System.out.println("Acce:   conn :  " + c + "   :::  " + acceData);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                for (int i = -1; (i = packages.indexOf(BARO_NOTIFY, i + 1)) != -1; i++) {
                    try {
                        baroPackage = packages.substring(i + 10, i + 34);
//                    String conn = baroPackage.substring(0, 4);
                        String baroData = baroPackage.substring(18, 24);
//                    int c = Integer.parseInt(conn);
                        BaroProcessing.baroQueue[conn].add(baroData);
//                    System.out.println("Baro:   conn :  " + c + "   :::  " + baroData);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
