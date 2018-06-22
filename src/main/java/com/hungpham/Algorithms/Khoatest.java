
package com.hungpham.Algorithms;

import com.hungpham.Controller.DatabaseFetch;

import java.time.Instant;

import com.hungpham.Controller.SerialPortController;
import com.hungpham.database.SensorsPoint;
import static java.lang.Math.abs;

public class Khoatest implements Runnable {
    private DatabaseFetch fetch = new DatabaseFetch(0);
    private volatile boolean shutdown = false;
    private Long UTVtimestamp;
    KhoaFilter khoaFilter = new KhoaFilter();
    FeatureExtraction ex = new FeatureExtraction();
    int LIMIT;  //number of most recent accelerometer value taken into consideration
    double UTV; //upper threshold value
    double LTV; //lower threshold value
    double slope, slope1;
    double pressureShift;

    public Khoatest(double UTV, double LTV) {
        this.UTV = UTV;
        this.LTV = LTV;
    }

    public void fetchAccePointAtCurrentSystemTime() {
        fetch.readAcceMostRecentfromNow(2);
    }

    public void fetchAccePointBeforeTimestamp(long timeStamp) {
        //String s = Objects.toString(timeStamp, null);
        fetch.readAcceInTimeInterval(timeStamp + "s - 2s", timeStamp + "s");
    }

    public boolean findValueOverUTV() {
        for (SensorsPoint a : fetch.getValueList()) {
            if (a.getAcce() >= UTV) {
                Instant time = a.getTime();
                UTVtimestamp = time.getEpochSecond();
                return true;
            }
        }
        return false;
    }

    public boolean findValueUnderLTV() {
        for (SensorsPoint a : fetch.getValueList()) {
            if (a.getAcce() <= LTV) {
                return true;
            }
        }
        return false;
    }

    public void run() {
        while (true) {
            try {
                fetchAccePointAtCurrentSystemTime();
                if (findValueOverUTV()) {
                    System.out.println("UTV");
                    fetchAccePointBeforeTimestamp(UTVtimestamp);
                    if (findValueUnderLTV()) {
                        Thread.sleep(4000);
                        double[] rawdata = khoaFilter.pullFromDB(UTVtimestamp);
                        double[] filteredOutput = khoaFilter.movingAverage(khoaFilter.slopeLimit(rawdata));
                        for (double a : rawdata) {
                            System.out.println(a);
                        }
                        System.out.println("=====================================");
                        for (double a : filteredOutput) {
                            System.out.println(a);
                        }
                        System.out.println("=====================================");
                        double[] first2s = new double[(int) rawdata.length / 3];
                        for (int i = 0; i < (int) rawdata.length / 3; i++) {
                            first2s[i] = filteredOutput[i];
                            System.out.println(first2s[i]);
                        }
                        System.out.println("=====================================");

                        double[] middle2s = new double[(int) rawdata.length / 9];
                        for (int j = 0; j < (int) rawdata.length / 9; j++) {
                            middle2s[j] = filteredOutput[45 + j];
                            System.out.println(middle2s[j]);
                        }
                        double[] x = new double[(int) rawdata.length / 9];
                        for (int j = 0; j < (int) rawdata.length / 9; j++) {
                            x[j] = j + 1;
                        }
                        System.out.println("=====================================");

                        double[] last2s = new double[(int) rawdata.length / 3];
                        for (int j = 0; j < rawdata.length / 3; j++) {
                            last2s[j] = filteredOutput[rawdata.length - 1 - j];
                            System.out.println(last2s[j]);
                        }
                        double[] l = new double[(int) rawdata.length / 3];
                        for (int j = 0; j < (int) rawdata.length / 9; j++) {
                            l[j] = j + 1;
                        }
                        System.out.println("=====================================");
                        ex.LinearRegression(x, middle2s);
                        slope = ex.slope();
                        System.out.println(slope);
                        ex.LinearRegression(l, last2s);
                        slope1 = ex.slope();
                        System.out.println(slope1);
                        pressureShift = ex.pressureShift(last2s, first2s);
                        System.out.println(pressureShift);
                        if ((slope>=0.7)&&(abs(slope1)<=0.2)&&(pressureShift>=9)){
                            System.out.println("************FALL-DETECTED!************");
                        }
                        else if (pressureShift<8){
                            System.out.println("not enough change in pressure");
                        }
                             else if(abs(slope)<0.7) {
                            System.out.println("not steep middle slope");
                        }
                             else  System.out.println("not stable post-fall slope ");
                        SerialPortController.mode = 0;
                        return;
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}




                     /*   System.out.println("fall detect");
                        Thread.sleep(4000);
                        double[] rawdata = khoaFilter.pullFromDB(UTVtimestamp);
                        double[] filteredOutput = khoaFilter.movingAverage(khoaFilter.slopeLimit(rawdata));
                        for (double a : rawdata) {
                            System.out.println(a);
                        }
                        System.out.println("=====================================");
                        for (double a : filteredOutput) {
                            System.out.println(a);
                        }*/

