package com.hungpham.Algorithms;

import com.hungpham.Controller.DatabaseFetch;
import com.hungpham.database.SensorsPoint;

import java.util.LinkedList;

import static com.hungpham.UI.graphs.BaroGraph.baroGraph;

public class BaroFilters {
    private DatabaseFetch fetch;
    private LinkedList<SensorsPoint> baro_values;

    private int conn;

    public BaroFilters(int conn, DatabaseFetch databaseFetch) {
        baro_values = new LinkedList<>();
        fetch = databaseFetch;
        this.conn = conn;
    }

    public double[] pullFromDB(long timeStamp) {
        // get barometer values in the most recent 2 seconds

        baro_values = fetch.readBaroInTimeInterval(timeStamp);

        double[] rawdata = new double[baro_values.size()];
        int i = 0;
        for (SensorsPoint s : baro_values) {
            rawdata[i] = s.getBaro();
            i++;
        }
        return rawdata;
    }

    public double[] slopeLimit(double[] data) {
        double slopeLimit = 15; //hPa
        double[] slopeFilteredOutput = new double[data.length];
        for (int j = 0; j < data.length; j++) slopeFilteredOutput[j] = 0;
        for (int i = 0; i < data.length; i++)
            if (i == 0) {
                slopeFilteredOutput[i] = data[i];
            } else {
                if (data[i] > data[i - 1] + slopeLimit)
                    slopeFilteredOutput[i] = data[i - 1] - slopeLimit;
                else if (data[i] < data[i - 1] - slopeLimit)
                    slopeFilteredOutput[i] = data[i - 1] - slopeLimit;
                else slopeFilteredOutput[i] = data[i];
            }
        return slopeFilteredOutput;
    }

    public double[] movingAverage(double[] data) {
        double[] Output = new double[data.length];
        int k = 0;
        for (int i = 0; i < data.length; i++) {
            Output[i] = 0;
        }
        for (int j = 0; j < data.length - 6; j++) {
            for (k = 0; k < 7; k++) {
                Output[j] = Output[j] + (data[j + k]) / 7;
            }
        }
        for (int i = 0; i < 6; i++) {
            Output[data.length - 6] = Output[data.length - 6] + (data[i + data.length - 6]) / 6;
        }
        for (int i = 0; i < 5; i++) {
            Output[data.length - 5] = Output[data.length - 5] + (data[i + data.length - 5]) / 5;
        }
        for (int i = 0; i < 4; i++) {
            Output[data.length - 4] = Output[data.length - 4] + (data[i + data.length - 4]) / 4;
        }
        for (int i = 0; i < 3; i++) {
            Output[data.length - 3] = Output[data.length - 3] + (data[i + data.length - 3]) / 3;
        }
        for (int i = 0; i < 2; i++) {
            Output[data.length - 2] = Output[data.length - 2] + (data[i + data.length - 2]) / 2;
        }

        Output[data.length - 1] = data[data.length - 1];
        baroGraph[conn].add(String.valueOf(Output[data.length - 1]));
        return Output;
    }
}
