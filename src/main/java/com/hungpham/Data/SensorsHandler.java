package com.hungpham.Data;

import java.util.ArrayList;

public abstract class SensorsHandler  {
    protected int numberOfSensorTags = 2;
    protected ArrayList<String> connHandle;

    protected void generateSensorList() {
        connHandle = new ArrayList<>();
        for (int i = 0; i < numberOfSensorTags; i++) {
            connHandle.add("000" + i);
        }
    }

    /** receive the raw data from class Serial Data */
    public abstract void receivingData();

    /** separate sensor values from different sensortags */
    public abstract void handleSensorData();
}
