package com.hungpham.Data;

import com.hungpham.Algorithms.KalmanFilter;
import com.hungpham.Controller.Definitions;
import com.hungpham.Utils.Utils;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import static com.hungpham.Controller.DatabasePush.acceDBQueue;

public class AcceProcessing implements Runnable {

    public static volatile LinkedBlockingQueue<String>[] acceQueue = new LinkedBlockingQueue[2];

    private double[] acce;
    private double[] offsets;
    private int samples;
    private Utils utils;

    private int conn;


    public AcceProcessing(int conn) {
        this.conn = conn;
        utils = new Utils();
        acce = new double[3];
        offsets = new double[3];
        samples = 0;
        offsets[0] = 0;
        offsets[1] = 0;
        offsets[2] = 0;
        acceQueue[conn] = new LinkedBlockingQueue<>();
    }

    private void readRaw() {
        ArrayList<String> hexList = null;

//        String received = utils.TCPReceive(9300 + conn);

        String received = null;
        try {
            received = acceQueue[conn].take();
            hexList = utils.seperate4Hex(received);
            int i = 0;
            for (String s : hexList) {
                String reverse;
                reverse = s.substring(2, 4) + s.substring(0, 2);
                acce[i] = (utils.hexStringToInt(reverse) * 1.0) - offsets[i];
                if (acce[i] > 32768) {
                    acce[i] = acce[i] - 65536;
                }
                //System.out.println("Accelerometer value: " + i + "   " + acce[i]);
                i++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void calibrateAcce() {
        double[] sums = new double[3];
        while (samples < 50) {
            readRaw();
            System.out.println("calibrating...");
            sums[0] += acce[0];
            sums[1] += acce[1];
            sums[2] += acce[2];
            samples++;
        }
        offsets[0] = sums[0] / 50 - 0;
        offsets[1] = sums[1] / 50 - 0;
        offsets[2] = sums[2] / 50 - 4096;
    }

    public void printData() {
        for (int i = 0; i < 3; i++) {
            acce[i] = acce[i] / (32768 / 8);
            //System.out.println("Accelerometer value: " + i + "   " + acce[i]);
        }
        double acc_value = Math.sqrt(acce[0] * acce[0] + acce[1] * acce[1] + acce[2] * acce[2]);
        String a = Double.toString(acc_value);
        //System.out.println("Accelerometer sqrt: conn: " + conn + "  :  " + acc_value);
        acceDBQueue[conn].add(conn + a);
        //utils.TCPSend("localhost", Definitions.GRAPH_ACCE_PORT, a);
//        utils.TCPSend("localhost", Definitions.DATABASE_ACCE_PORT + conn, conn + a);
    }

    @Override
    public void run() {
        while (true) {
            if (samples < 50) {
                System.out.println("calibrating...");
                calibrateAcce();
            }
            readRaw();
            printData();
        }


    }
}
