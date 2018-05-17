package com.hungpham.Data;

import com.hungpham.Utils.Utils;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class AcceObserver extends SensorsObserver {
    private String data;
    private double[] acce;
    public static volatile LinkedBlockingQueue<String> acceData;

    public AcceObserver(SerialData subject) {
        this.subject = subject;
        this.subject.attach(this);
        this.name = "acce";
        acce = new double[3];
        acceData = new LinkedBlockingQueue<String>();
        data = null;
    }

    public void convertData() {
        if (!(data == null)) {
            ArrayList<String> hexList = Utils.seperate4Hex(data);
            for (String s : hexList) {
                int i = 0;
                String reverse;
                reverse = s.substring(2,4) + s.substring(0,2);
                acce[i] = (Utils.hexStringToInt(reverse) * 1.0) / (32768/8);
                System.out.println("Accelerometer value: " + acce[i]);
                i++;
            }
        }
    }

    public String getData() {
        return data;
    }

    @Override
    public synchronized void update() {
        data = subject.getData();
        extractValue();
        if (data.contains("000000000000")) {

        } else {
            acceData.add(data);
            //System.out.println("Accelerometer: " + data);
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    private void extractValue() {
        String rawValue = data.substring(34, 46);
        data = rawValue;
    }
}
