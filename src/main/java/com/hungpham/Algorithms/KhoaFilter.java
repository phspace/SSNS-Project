package com.hungpham.Algorithms;

import com.hungpham.Controller.DatabaseFetch;
import com.hungpham.database.SensorsPoint;

import java.util.LinkedList;

public class KhoaFilter {
    private DatabaseFetch fetch;
    private LinkedList<SensorsPoint> baro_values;

    public KhoaFilter() {
        baro_values = new LinkedList<>();
        fetch = new DatabaseFetch();
    }

    public double[] pullFromDB(long UTVtimestamp) {
        // get barometer values in the most recent 2 second
        baro_values = fetch.readBaroInTimeInterval(UTVtimestamp);

        double[] rawdata = new double[baro_values.size()];
        int i=0;
        for (SensorsPoint s : baro_values) {
            rawdata[i] = s.getBaro();
            i++;
        }
        return rawdata;
    }
    public double[] slopeLimit(double[] data) {
        double slopeLimit = 20; //hPa
        double[] slopeFilteredOutput = new double[data.length];
        for (int j = 0; j < data.length; j++) slopeFilteredOutput[j] = 0;
        for (int i = 0; i < data.length; i++) {
            if (i == 0) {slopeFilteredOutput[i] = data[i];}
            else {
                if (data[i] > (data[i - 1] + slopeLimit)) {
                    slopeFilteredOutput[i] = data[i - 1] - slopeLimit;
                } else if (data[i] < (data[i - 1] - slopeLimit))
                {slopeFilteredOutput[i] = data[i - 1] - slopeLimit;}
                     else {slopeFilteredOutput[i] = data[i];}

            }
        }
        return slopeFilteredOutput;
    }
    public double[] movingAverage(double[] data){
        double[] Output = new double[data.length];
        for (int j=0; j < data.length; j++) Output[j] = 0;
        for (int i=0; i < data.length-6; i++) {
            for (int k=0; k< 7; k++)
                Output[i] = Output[i]+1/7*(data[i+k]);
        }
        for (int a=0; a < 6; a++) Output[data.length-6] = Output[data.length-6] + 1 / 6* (data[a+data.length-6]);
        for (int b=0; b < 5; b++) Output[data.length-5] = Output[data.length-5] + 1 / 5* (data[b+data.length-5]);
        for (int c=0; c < 4; c++) Output[data.length-4] = Output[data.length-4] + 1 / 4* (data[c+data.length-4]);
        for (int d=0; d < 3; d++) Output[data.length-3] = Output[data.length-3] + 1 / 3* (data[d+data.length-3]);
        for (int e=0; e < 2; e++) Output[data.length-2] = Output[data.length-2] + 1 / 2* (data[e+data.length-2]);
        Output[data.length-1]=data[data.length-1];
        return Output;
    }
}
