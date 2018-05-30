package com.hungpham.Data;

import com.hungpham.Controller.Definitions;
import com.hungpham.Utils.Utils;

public class BaroProcessing implements Runnable {
    private Utils utils;
    private String rawData;

    public BaroProcessing() {
        utils = new Utils();
        rawData = null;
    }

    public void readData() {
        rawData = utils.TCPReceive(Definitions.RECEIVING_BAR_VALUE_PORT);
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        while (true) {
            readData();
            //System.out.println("Barometer value:  " + rawData);
        }
    }
}
