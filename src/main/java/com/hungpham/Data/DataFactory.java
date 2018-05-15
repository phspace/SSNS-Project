package com.hungpham.Data;

public class DataFactory implements Runnable {
    private AcceObserver acc;
    private BaroObserver bar;
    private SerialData serialData;

    public DataFactory() {
        serialData = new SerialData();
        acc = new AcceObserver(serialData);
        bar = new BaroObserver(serialData);
    }

    @Override
    public void run() {
        while (true) {
            while (SerialData.dataQueue.size() != 0) {
                serialData.updateData();
                serialData.seperateData();
            }
        }
    }
}
