package com.hungpham.Algorithms;

import com.hungpham.Controller.DatabaseFetch;
import java.time.Instant;

import com.hungpham.database.SensorsPoint;


public class Khoatest implements Runnable{
    private DatabaseFetch fetch = new DatabaseFetch(0);
    private volatile boolean shutdown = false;
    private Long UTVtimestamp;
    KhoaFilter khoaFilter= new KhoaFilter();
    int LIMIT;  //number of most recent accelerometer value taken into consideration
    double UTV; //upper threshold value
    double LTV; //lower threshold value

    public Khoatest(double UTV) {
        this.UTV = UTV;
    }

    public void fetchAccePointAtCurrentSystemTime(){
        fetch.readAcceMostRecentfromNow(5);
    }

    public void fetchAccePointBeforeTimestamp(long timeStamp){
        //String s = Objects.toString(timeStamp, null);
        fetch.readAcceInTimeInterval(timeStamp + "s - 2s", timeStamp + "s");
    }

    public boolean findValueOverUTV(){
        for (SensorsPoint a : fetch.getValueList()) {
            if (a.getAcce() >= UTV)  {
                Instant time = a.getTime();
                UTVtimestamp = time.getEpochSecond();
                return true;
            }
        }
        return false;
    }




    public void run() {
        while (true) {
            try
            {
                fetchAccePointAtCurrentSystemTime();
                if(findValueOverUTV())
                {
                    System.out.println("************FALL-DETECTED!************");

                    Thread.sleep(4000);
                    double[] rawdata= khoaFilter.pullFromDB(UTVtimestamp);
                    double [] filteredOutput = khoaFilter.movingAverage(khoaFilter.slopeLimit(rawdata));
                    for (double a : rawdata){
                        System.out.println(a);

                    }
                    System.out.println("new");
                    for (double a : filteredOutput){
                    System.out.println(a);
                    }
                }

            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
