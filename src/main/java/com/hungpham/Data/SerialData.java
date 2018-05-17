package com.hungpham.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class is made to fetch new data from data queue and notify observers
 */

public class SerialData {
    private volatile String rawData;
    private List<SensorsObserver> observers = new ArrayList<SensorsObserver>();

    // this is the data queue
    public static volatile LinkedBlockingQueue<String> dataQueue;

    public SerialData() {
        rawData = null;
        dataQueue = new LinkedBlockingQueue<>();
    }

    public void updateData() {
        try {
            rawData = dataQueue.take();
            //System.out.println("Raw data:" + rawData);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void attach(SensorsObserver observer) {
        observers.add(observer);
    }

    public synchronized void notifySpecificObserver(String sensor) {
        for (SensorsObserver observer : observers) {
            if (observer.getName().equalsIgnoreCase(sensor)) {
                observer.update();
            }
        }
    }

    public void seperateData() {
        String accel = "04FF1A1B05000000142C";
        String bar = "04FF0E1B050000000824";
        if (rawData.indexOf(accel) == 0) {
            notifySpecificObserver("acce");
        } else if (rawData.indexOf(bar) == 0) {
            notifySpecificObserver("baro");
        }
    }

    public String getData() {
        return rawData;
    }


}
