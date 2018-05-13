package com.hungpham.Data;

public class AcceObserver extends DataObserver {
    private String data;

    public AcceObserver(SensorData subject) {
        this.subject = subject;
        this.subject.attach(this);
        this.name = "acce";
    }

    public String getData() {
        return data;
    }

    @Override
    public synchronized void update() {
        data = subject.getData();
        System.out.println("Accelerometer: " + data);
    }

    @Override
    public String getName() {
        return this.name;
    }
}
