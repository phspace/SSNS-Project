package com.hungpham.Data;

public class BaroObserver extends DataObserver {
    private String data;

    public BaroObserver(SensorData subject) {
        this.subject = subject;
        this.subject.attach(this);
        this.name = "baro";
    }

    public String getData() {
        return data;
    }

    @Override
    public synchronized void update() {
        data = subject.getData();
        System.out.println("Barometer: " + data);
    }

    @Override
    public String getName() {
        return this.name;
    }
}
