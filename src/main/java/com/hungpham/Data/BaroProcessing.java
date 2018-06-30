package com.hungpham.Data;

import com.hungpham.Utils.Utils;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import static com.hungpham.Controller.DatabasePush.baroDBQueue;
import static com.hungpham.UI.MainScene.operatingDevicesNumber;

public class BaroProcessing implements Runnable {
    private Utils utils;
    private String rawData;
    private double baro;

    public static volatile LinkedBlockingQueue<String>[] baroQueue = new LinkedBlockingQueue[operatingDevicesNumber];

    private int conn;

    public BaroProcessing(int conn) {
        utils = new Utils();
        rawData = null;
        this.conn = conn;
        baroQueue[conn] = new LinkedBlockingQueue<>();
    }

    private void readRaw() {
        ArrayList<String> hexList = null;
        String received = null;
        try {
            received = baroQueue[conn].take();
            hexList = utils.seperate2Hex(received);

            String reversed = hexList.get(2) + hexList.get(1) + hexList.get(0);
            baro = utils.hexStringToInt(reversed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
            readRaw();
            String b = Double.toString(baro);
            //System.out.println("Barometer value: conn: " + conn + "    " + baro);
            if (baro > 120000 || baro < 90000) ;
            else {
                baroDBQueue[conn].add(b);
//                baroGraph[conn].add(b);
            }
        }
    }
}
