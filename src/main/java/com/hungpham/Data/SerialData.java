package com.hungpham.Data;

import com.hungpham.Controller.Definitions;
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

    public static volatile LinkedBlockingQueue<String> dataPackage = new LinkedBlockingQueue<>();

    private String firstPackage;
    private String secondPackage;

    private Thread seperateData;

    public SerialData() {
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
            try {
                packages = dataPackage.take();
                String accePackage = "";
                String baroPackage = "";
                String accePackage1 = "";
                String baroPackage1 = "";
                switch (packages.length()) {
                    /** if the package has 2 accelerometer values */
                    case 116:
                        accePackage = packages.substring(0, 58).replaceAll(ACCE_NOTIFY, "");
                        accePackage1 = packages.substring(58, 116).replaceAll(ACCE_NOTIFY, "");
//                    System.out.println("Acce 1: " + accePackage);
//                    System.out.println("Acce 2: " + accePackage1);
                        break;

                    /** if the package contains accelerometer and barometer */
                    case 92:
                        if (packages.indexOf(ACCE_NOTIFY) < 2) {
                            accePackage = packages.substring(0, 58).replaceAll(ACCE_NOTIFY, "");
                            baroPackage = packages.substring(58, 92).replaceAll(BARO_NOTIFY, "");
//                        System.out.println("Acce: " + accePackage);
//                        System.out.println("Baro: " + baroPackage);
                        } else if (packages.indexOf(BARO_NOTIFY) < 2) {
                            accePackage = packages.substring(34, 92).replaceAll(ACCE_NOTIFY, "");
                            baroPackage = packages.substring(0, 34).replaceAll(BARO_NOTIFY, "");
//                        System.out.println("Acce: " + accePackage);
//                        System.out.println("Baro: " + baroPackage);
                        }
                        break;

                    /** 2 baro */
                    case 68:
                        baroPackage = packages.substring(0, 34).replaceAll(BARO_NOTIFY, "");
                        baroPackage1 = packages.substring(34, 68).replaceAll(BARO_NOTIFY, "");
//                    System.out.println("Baro 1: " + baroPackage);
//                    System.out.println("Baro 2: " + baroPackage1);
                        break;

                    /** 1 single acce */
                    case 58:
                        accePackage = packages.replaceAll(ACCE_NOTIFY, "");
//                    System.out.println("Acce: " + accePackage);
                        break;

                    /** 1 single baro */
                    case 34:
                        baroPackage = packages.replaceAll(BARO_NOTIFY, "");
//                    System.out.println("Baro: " + baroPackage);
                        break;
                    case 0:
//                    System.out.println("Package reset and not processed??????????");
                        break;
                    default:
                        System.out.println("Something wrong with complete package: " + packages);
                        break;
                }

                if (accePackage.length() > 0) {
                    String conn = accePackage.substring(0, 4);
                    String accData = accePackage.substring(24, 36);
                    //System.out.println(conn + "   acce  " + accData);
                    int i = Integer.parseInt(conn);
                    AcceProcessing.acceQueue[i].add(accData);
//                    utils.TCPSend("localhost", RECEIVING_ACC_VALUE_PORT + i, accData);
                }
                if (accePackage1.length() > 0) {
                    String conn = accePackage1.substring(0, 4);
                    String accData = accePackage1.substring(24, 36);
                    //System.out.println(conn + "   acce  " + accData);
                    int i = Integer.parseInt(conn);
                    AcceProcessing.acceQueue[i].add(accData);
//                    utils.TCPSend("localhost", RECEIVING_ACC_VALUE_PORT + i, accData);
                }
                if (baroPackage.length() > 0) {
                    String conn = baroPackage.substring(0, 4);
                    String baroData = baroPackage.substring(18, 24);
                    //System.out.println(conn + "  baro   " + baroData);
                    int i = Integer.parseInt(conn);
                    BaroProcessing.baroQueue[i].add(baroData);
//                    utils.TCPSend("localhost", RECEIVING_BAR_VALUE_PORT + i, baroData);
                }
                if (baroPackage1.length() > 0) {
                    String conn = baroPackage1.substring(0, 4);
                    String baroData = baroPackage1.substring(18, 24);
                    //System.out.println(conn + "  baro   " + baroData);
                    int i = Integer.parseInt(conn);
                    BaroProcessing.baroQueue[i].add(baroData);
//                    utils.TCPSend("localhost", RECEIVING_BAR_VALUE_PORT + i, baroData);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
