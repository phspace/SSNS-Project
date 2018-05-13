package com.hungpham.Data;

import java.util.concurrent.LinkedBlockingQueue;

public class BaroObserver extends DataObserver {
    private String data;
    public static volatile LinkedBlockingQueue<String> baroData;

    public BaroObserver(SensorData subject) {
        this.subject = subject;
        this.subject.attach(this);
        this.name = "baro";
        baroData = new LinkedBlockingQueue<String>();
    }

    public String getData() {
        return data;
    }

    @Override
    public synchronized void update() {
        data = subject.getData();
        baroData.add(data);
        System.out.println("Barometer: " + data);
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void extractValue() {
        //String rawValue = data.substring()
    }
}
