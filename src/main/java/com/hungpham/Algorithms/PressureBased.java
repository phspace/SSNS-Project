package com.hungpham.Algorithms;

import com.hungpham.Controller.DatabaseFetch;

import static com.hungpham.Controller.AlgorithmsController.voting;
import static java.lang.Math.abs;

public class PressureBased implements Runnable {
    private BaroFilters baroFilters;
    private FeatureExtraction ex;
    private double slope, slope1;
    private double pressureShift;

    private long updateTime;

    private int conn;

    private DatabaseFetch fetch;

    public PressureBased(int conn) {
        this.conn = conn;
        fetch = new DatabaseFetch(conn);
        baroFilters = new BaroFilters(conn, fetch);
        ex = new FeatureExtraction();
    }

    public void run() {
        try {
            updateTime = System.currentTimeMillis();
            double[] rawdata = baroFilters.pullFromDB(updateTime);
            double[] filteredOutput = baroFilters.movingAverage(baroFilters.slopeLimit(rawdata));

//                System.out.println("=====================================");
//                for (double a : filteredOutput) {
//                    System.out.println(a);
//                }
//                System.out.println("=====================================");
            double[] first2s = new double[(int) rawdata.length / 3];
            for (int i = 0; i < (int) rawdata.length / 3; i++) {
                first2s[i] = filteredOutput[i];
//                    System.out.println(first2s[i]);
            }
//                System.out.println("=====================================");

            double[] middle2s = new double[(int) rawdata.length / 7];
            for (int j = 0; j < (int) rawdata.length / 7; j++) {
                middle2s[j] = filteredOutput[40 + j];
//                    System.out.println(middle2s[j]);
            }
            double[] x = new double[(int) rawdata.length / 7];
            for (int j = 0; j < (int) rawdata.length / 7; j++) {
                x[j] = j + 1;
            }
//                System.out.println("=====================================");

            double[] last2s = new double[(int) rawdata.length / 3];
            for (int j = 0; j < rawdata.length / 3; j++) {
                last2s[j] = filteredOutput[rawdata.length - 1 - j];
//                    System.out.println(last2s[j]);
            }
            double[] l = new double[(int) rawdata.length / 3];
            for (int j = 0; j < (int) rawdata.length / 3; j++) {
                l[j] = j + 1;
            }
//                System.out.println("=====================================");
            ex.LinearRegression(x, middle2s);
            slope = ex.slope();
//            System.out.println(slope);
            ex.LinearRegression(l, last2s);
            slope1 = ex.slope();
//            System.out.println(slope1);
            pressureShift = ex.pressureShift(last2s, first2s);
//            System.out.println(pressureShift);
            if ((slope >= 0.5) && (abs(slope1) <= 0.2) && (pressureShift >= 6)) {
                System.out.println(slope);
                System.out.println(slope1);
                System.out.println(pressureShift);
                System.out.println("************FALL-DETECTED from BARO!************");
                if (voting[conn] == 1) {
                    voting[conn] = 2;
                }
            } else if (pressureShift < 7.5) {
//                System.out.println("not enough change in pressure");
            } else if (abs(slope) < 0.5) {
//                System.out.println("not steep middle slope");
            }
//            else System.out.println("not stable post-fall slope ");

        } catch (Exception e) {
//            e.printStackTrace();
        }
    }
}