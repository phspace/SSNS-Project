package com.hungpham.Data;

import java.util.concurrent.LinkedBlockingQueue;

public class AcceObserver extends DataObserver {
    private String data;
    public static volatile LinkedBlockingQueue<String> acceData;

    public AcceObserver(SensorData subject) {
        this.subject = subject;
        this.subject.attach(this);
        this.name = "acce";
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
        String rawValue = data.substring(34, 46);
        data = rawValue;
    }
}
