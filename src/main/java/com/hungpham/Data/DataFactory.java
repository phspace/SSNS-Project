package com.hungpham.Data;

/**
 * This class creates run instances for Observer pattern
 */
public class DataFactory implements Runnable {
    private SerialData serialData;

    public DataFactory() {
        serialData = new SerialData();
    }

    @Override
    public void run() {
        while (true) {
                serialData.updateData();
        }
    }
}
