package com.hungpham.Data;

public class DataFactory implements Runnable {
    private AcceObserver acc;
    private BaroObserver bar;
    private SensorData sensorData;

    public DataFactory() {
        sensorData = new SensorData();
        acc = new AcceObserver(sensorData);
        bar = new BaroObserver(sensorData);
    }

    @Override
    public void run() {
        while (true) {
            while (SensorData.dataQueue.size() != 0) {
                sensorData.updateData();
                sensorData.seperateData();
            }
        }
    }
}
