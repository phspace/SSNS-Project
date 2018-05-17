package com.hungpham.Data;

import com.hungpham.Controller.SerialPortController;
import com.hungpham.Utils.Utils;
import gnu.io.SerialPort;

import java.util.ArrayList;

public class AcceProcessing implements Runnable {

    private double[] acce;
    private double[] offsets;
    private int samples;

    public AcceProcessing() {
        acce = new double[3];
        offsets = new double[3];
        samples = 0;
    }

    private void convertData() {

            ArrayList<String> hexList = null;
            try {
                hexList = Utils.seperate4Hex(AcceObserver.acceData.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (String s : hexList) {
                int i = 0;
                String reverse;
                reverse = s.substring(2, 4) + s.substring(0, 2);
                acce[i] = (Utils.hexStringToInt(reverse) * 1.0) / (32768 / 8);
                System.out.println("Accelerometer value: " + acce[i]);
                i++;
            }

    }

    private void calibrateAcce() {
        double[] sums = new double[3];
        while (samples < 100) {
            convertData();
            sums[0] += acce[0];
            sums[1] += acce[1];
            sums[2] += acce[2];
            samples++;
        }
        offsets[0] = sums[0]/100 - 0;
        offsets[1] = sums[1]/100 - 0;
        offsets[2] = sums[2]/100 - 1;
    }

    public void printData() {
        for (int i = 0; i < 3; i++) {
            acce[i] = acce[i] - offsets[i];
            System.out.println("Accelerometer value: " + acce[i]);
        }
    }

    @Override
    public void run() {
        while (true) {
            convertData();
        }
    }
}
