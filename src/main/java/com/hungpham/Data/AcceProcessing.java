package com.hungpham.Data;

import com.hungpham.Utils.Utils;

import java.util.ArrayList;

public class AcceProcessing implements Runnable {

    private double[] acce;

    public AcceProcessing() {
        acce = new double[3];
    }

    public void convertData() {
        if (AcceObserver.acceData.size() != 0) {
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
    }

    @Override
    public void run() {
        while (true) {
            convertData();
        }
    }
}
