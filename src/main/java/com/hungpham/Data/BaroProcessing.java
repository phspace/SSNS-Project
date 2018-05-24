package com.hungpham.Data;

import com.hungpham.Controller.Definitions;
import com.hungpham.Utils.Utils;

import java.util.ArrayList;

public class BaroProcessing implements Runnable {
    private Utils utils;
    private String rawData;
    private double baro;
    private double[] filtered;

    public BaroProcessing() {
        utils = new Utils();
        rawData = null;
    }

    private void readRaw() {
        ArrayList<String> hexList = null;
        String received = utils.TCPReceive(Definitions.RECEIVING_BAR_VALUE_PORT);

        hexList = utils.seperate2Hex(received);

        String reversed = hexList.get(2) + hexList.get(1) + hexList.get(0);
        baro = utils.hexStringToInt(reversed) / 100.0;
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
            readRaw();
            System.out.println("Barometer value:  " + baro);
            utils.TCPSend("localhost", Definitions.GRAPH_BARO_PORT, Double.toString(baro));
        }
    }
}
