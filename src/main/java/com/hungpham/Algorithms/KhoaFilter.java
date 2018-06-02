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

    public void pullFromDB() {
        // get barometer values in the most recent 2 seconds
        baro_values = fetch.readBaroInTimeInterval("(now() - " + 2 + "s)", "now()");

        double[] baro = new double[baro_values.size()];
        int i = 0;
        for (SensorsPoint s : baro_values) {
            baro[i] = s.getBaro();
        }
    }
}
