package com.hungpham.Data;

import java.util.concurrent.LinkedBlockingQueue;

public class AcceObserver extends SensorsObserver {
    private String data;
    private int[] acce;
    public static volatile LinkedBlockingQueue<String> acceData;

    public AcceObserver(SerialData subject) {
        this.subject = subject;
        this.subject.attach(this);
        this.name = "acce";
        acce = new int[3];
        acceData = new LinkedBlockingQueue<String>();
    }

    public String getData() {
        return data;
    }

    @Override
    public synchronized void update() {
        data = subject.getData();
        extractValue();
        if (data.contains("000000000000")) {

        } else {acceData.add(data);

        System.out.println("Accelerometer: " + data);}
    }

    @Override
    public String getName() {
        return this.name;
    }

    private void extractValue() {
        String rawValue = data.substring(40, 46);
        data = rawValue;
    }
}
