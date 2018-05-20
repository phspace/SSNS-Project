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

    public SerialData() {
        utils = new Utils();
        rawData = null;
    }

    public void updateData() {
        rawData = utils.TCPReceive(Definitions.RECEIVING_SENSOR_VALUE_PORT);
        seperateData();
    }

    private void seperateData() {
        String accel = "142C00000000000000";
        String bar = "082400";

        if (rawData.contains(accel) && rawData.contains(bar)) {
            String rawAccValue = rawData.substring(rawData.indexOf(accel) + 18, rawData.indexOf(accel) + 30);
            String rawBaroValue = rawData.substring(rawData.indexOf(bar) + 6, rawData.indexOf(bar) + 18);
            if (rawAccValue.contains("000000000000")) {
                //System.out.println("Wrong acce data");
            } else {
                utils.TCPSend("localhost", Definitions.RECEIVING_ACC_VALUE_PORT, rawAccValue);
            }

            if (rawBaroValue.contains("000000000000")) {
                //System.out.println("Wrong acce data");
            } else {
                utils.TCPSend("localhost", Definitions.RECEIVING_BAR_VALUE_PORT, rawBaroValue);
            }
        } else if (rawData.contains(accel) || rawData.contains(bar)) {
            if (rawData.contains(accel)) {
                String rawAccValue = rawData.substring(rawData.indexOf(accel) + 18, rawData.indexOf(accel) + 30);
                if (rawAccValue.contains("000000000000")) {
                    //System.out.println("Wrong acce data");
                } else {
                    utils.TCPSend("localhost", Definitions.RECEIVING_ACC_VALUE_PORT, rawAccValue);
                }
            } else if (rawData.contains(bar)) {
                String rawBaroValue = rawData.substring(rawData.indexOf(bar) + 6, rawData.indexOf(bar) + 18);
                if (rawBaroValue.contains("000000000000")) {
                    //System.out.println("Wrong acce data");
                } else {
                    utils.TCPSend("localhost", Definitions.RECEIVING_BAR_VALUE_PORT, rawBaroValue);
                }
            }
        }

    }

    public String getData() {
        return rawData;
    }


}
