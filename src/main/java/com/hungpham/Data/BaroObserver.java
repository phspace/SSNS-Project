package com.hungpham.Data;

import java.util.concurrent.LinkedBlockingQueue;

public class BaroObserver extends SensorsObserver {
    private String data;
    public static volatile LinkedBlockingQueue<String> baroData;

    public BaroObserver(SerialData subject) {
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
        extractValue();
        baroData.add(data);
        System.out.println("Barometer: " + data);
    }

    @Override
    public String getName() {
        return this.name;
    }

    private void extractValue() {
        String rawValue = data.substring(22, 32);
        data = rawValue;
    }
}
