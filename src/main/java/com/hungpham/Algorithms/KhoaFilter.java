package com.hungpham.Algorithms;

import com.hungpham.Controller.DatabaseFetch;
import com.hungpham.database.SensorsPoint;

import java.util.LinkedList;

public class KhoaFilter {
    private DatabaseFetch fetch;
    private LinkedList<SensorsPoint> baro_values;

    public KhoaFilter() {
        baro_values = new LinkedList<>();
        fetch = new DatabaseFetch(0);
    }

    public double[] pullFromDB() {
        // get barometer values in the most recent 2 seconds
        //baro_values = fetch.readBaroInTimeInterval("(now() - " + 2 + "s)", "now()");

        double[] rawdata = new double[baro_values.size()];
        int i = 0;
        for (SensorsPoint s : baro_values) {
            rawdata[i] = s.getBaro();

        }
        return rawdata;
    }
    public double[] slopeLimit(double[] data) {
        double slopeLimit = 0.07; //hPa
        double[] slopeFilteredOutput = new double[data.length];
        for (int i=0; i < data.length; i++) slopeFilteredOutput[i] = 0;
        for (int i=0; i < data.length; i++)
            if  (data[i+1] > data[i]+ slopeLimit)
                  slopeFilteredOutput [i+1] = data[i] - slopeLimit;
            else if (slopeFilteredOutput [i+1] < data[i] - slopeLimit)
                  slopeFilteredOutput[i+1] = data[i] - slopeLimit;
            else  slopeFilteredOutput[i+1] = data[i];
            return slopeFilteredOutput;
    }
    public double[] movingAverage(double[] data){
        double[] Output = new double[data.length];
        for (int i=0; i < data.length; i++) Output[i] = 0;
        for (int i=0; i < data.length-6; i++) {
            for (int j=0; j < 7; j++) Output[i] = Output[i]+1/7*(data[i+j]);
        }
        for (int i=0; i < 6; i++) Output[data.length-6] = Output[data.length-6] + 1 / 6* (data[i+data.length-6]);
        for (int i=0; i < 5; i++) Output[data.length-5] = Output[data.length-5] + 1 / 5* (data[i+data.length-5]);
        for (int i=0; i < 4; i++) Output[data.length-4] = Output[data.length-4] + 1 / 4* (data[i+data.length-4]);
        for (int i=0; i < 3; i++) Output[data.length-3] = Output[data.length-3] + 1 / 3* (data[i+data.length-3]);
        for (int i=0; i < 2; i++) Output[data.length-2] = Output[data.length-2] + 1 / 2* (data[i+data.length-2]);
        Output[data.length-1]=data[data.length-1];
        return Output;
    }
}
