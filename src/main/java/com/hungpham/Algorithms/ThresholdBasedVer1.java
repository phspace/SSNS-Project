
package com.hungpham.Algorithms;

import com.hungpham.Controller.DatabaseFetch;
import java.time.Instant;
import com.hungpham.database.AccelerometerPoint;
import java.util.Objects;


public class ThresholdBasedVer1 implements Runnable{
    private DatabaseFetch fetch = new DatabaseFetch();
    private volatile boolean shutdown = false;
    private Long UTVtimestamp;

    int LIMIT;  //number of most recent accelerometer value taken into consideration
    double UTV; //upper threshold value
    double LTV; //lower threshold value

    public ThresholdBasedVer1( double UTV, double LTV) {
        this.UTV = UTV;
        this.LTV = LTV;
    }

    public void fetchAccePointAtCurrentSystemTime(){
        fetch.readDataMostRecentfromNow("2s");
    }

    public void fetchAccePointBeforeTimestamp(long timeStamp){
        //String s = Objects.toString(timeStamp, null);
        fetch.readDataInTimeInterval1( timeStamp + "s - 2s", timeStamp + "s");
    }

    public boolean findValueOverUTV(){
        for (AccelerometerPoint a : fetch.getValueList()) {
            if (a.getAcce_value() >= UTV)  {
                Instant time = a.getTimeStamp();
                UTVtimestamp = time.getEpochSecond();
                return true;
            }
        }
        return false;
    }

    public boolean findValueUnderLTV(){
        for (AccelerometerPoint a : fetch.getValueList()) {
            if (a.getAcce_value() <= LTV)  {
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
                fetchAccePointBeforeTimestamp(UTVtimestamp);
                if(findValueUnderLTV())
                    {
                    System.out.println("************FALL-DETECTED!************");
                    }
                }
            Thread.sleep(1000);
            }catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


}

