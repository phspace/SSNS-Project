package com.hungpham.Data;

import com.hungpham.Algorithms.KalmanFilter;
import com.hungpham.Controller.SerialPortController;
import com.hungpham.Utils.Utils;
import gnu.io.SerialPort;

import java.util.ArrayList;

public class AcceProcessing implements Runnable {

    private double[] acce;
    private double[] filtered;
    private double[] offsets;
    private int samples;

    private KalmanFilter[] kalmanFilters;

    public AcceProcessing() {
        acce = new double[3];
        offsets = new double[3];
        filtered = new double[3];
        samples = 0;
        kalmanFilters = new KalmanFilter[3];
        offsets[0] = 0;
        offsets[1] = 0;
        offsets[2] = 0;
    }

    private void readRaw() {

        ArrayList<String> hexList = null;
        try {
            hexList = Utils.seperate4Hex(AcceObserver.acceData.take());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int i = 0;
        for (String s : hexList) {
            String reverse;
            reverse = s.substring(2, 4) + s.substring(0, 2);
            acce[i] = (Utils.hexStringToInt(reverse) * 1.0) - offsets[i];
            //acce[i] = (Utils.hexStringToInt(reverse) * 1.0) / (32768 / 8);
            if (acce[i] > 32768) {
                acce[i] = acce[i] - 65536;
            }
            //System.out.println("Accelerometer value: " + i + "   " + acce[i]);
            i++;
        }
    }

    public void kalmanFilter() {
        for (int i = 0; i < 3; i++) {
            filtered[i] = kalmanFilters[i].kalman_filter_update(acce[i]);
            filtered[i] = filtered[i] / (32768 / 8);
            System.out.println("Filtered Accelerometer value: " + i + "   " + filtered[i]);
        }
    }

    private void calibrateAcce() {
        double[] sums = new double[3];
        while (samples < 50) {
            readRaw();
            sums[0] += acce[0];
            sums[1] += acce[1];
            sums[2] += acce[2];
            samples++;
        }
        offsets[0] = sums[0] / 50 - 0;
        offsets[1] = sums[1] / 50 - 0;
        offsets[2] = sums[2] / 50 - 4022;
    }

    public void printData() {
        for (int i = 0; i < 3; i++) {
            acce[i] = acce[i] / (32768 / 8);
            System.out.println("Accelerometer value: " + i + "   " + acce[i]);
        }
    }

    @Override
    public void run() {
        while (true) {
            if (AcceObserver.acceData.size() > 0) {
                if (samples < 50) {
                    System.out.println("calibrating...");
                    calibrateAcce();
                }
                readRaw();
                printData();
                //kalmanFilter();
            }

        }
    }
}
