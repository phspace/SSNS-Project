package com.hungpham.Data;

import com.hungpham.Controller.Definitions;
import com.hungpham.Utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class is made to fetch new data from data queue and notify observers
 */

public class SerialData {
    private volatile String rawData;
    private Utils utils;
    private int checkAcc = 0;

    public SerialData() {
        utils = new Utils();
        rawData = null;
    }

    public void updateData() {
        rawData = utils.TCPReceive(Definitions.RECEIVING_SENSOR_VALUE_PORT);
        seperateData();
    }

    private void seperateData() {
        String accel = "04FF1A1B05000000142C";
        String bar = "04FF0E1B050000000824";
        if (rawData.indexOf(accel) == 0) {
            //notifySpecificObserver("acce");
            String rawValue = rawData.substring(34, 46);
            if (rawValue.contains("000000000000")) {
                checkAcc = 1;
                //System.out.println("Wrong acce data");
            } else {
                utils.TCPSend("localhost", Definitions.RECEIVING_ACC_VALUE_PORT, rawValue);
            }
        } else if (checkAcc == 1) {
            String rawValue = rawData.substring(2, 14);
            checkAcc = 0;
            utils.TCPSend("localhost", Definitions.RECEIVING_ACC_VALUE_PORT, rawValue);
        } else if (rawData.indexOf("142C00000000000000") == 0) {
            String rawValue = rawData.substring(18, 30);
            if (rawValue.contains("000000000000")) {
                //System.out.println("Wrong acce data");
            } else {
                utils.TCPSend("localhost", Definitions.RECEIVING_ACC_VALUE_PORT, rawValue);
            }
        } else if (rawData.indexOf("142C00000000000000") > 0) {
            String rawValue = rawData.substring(rawData.indexOf("142C00000000000000") + 18, rawData.indexOf("142C00000000000000") + 30);
            if (rawValue.contains("000000000000")) {
                //System.out.println("Wrong acce data");
            } else {
                utils.TCPSend("localhost", Definitions.RECEIVING_ACC_VALUE_PORT, rawValue);
            }
        } else if (rawData.indexOf(bar) == 0) {
            //notifySpecificObserver("baro");
            //Utils.TCPSend("localhost", Definitions.RECEIVING_BAR_VALUE_PORT, rawData);
        }
    }

    public String getData() {
        return rawData;
    }


}
