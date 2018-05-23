package com.hungpham.Algorithms;
import com.hungpham.Controller.DatabaseFetch;
import com.hungpham.database.AccelerometerPoint;


public class ThresholdBased implements Runnable{

    private DatabaseFetch fetch = new DatabaseFetch();
    private volatile boolean shutdown = false;

    int LIMIT;  //number of most recent accelerometer value taken into consideration
    double UTV; //upper threshold value
    double LTV; //lower threshold value


    public ThresholdBased(int LIMIT, double UTV, double LTV) {
        this.LIMIT = LIMIT;
        this.UTV = UTV;
        this.LTV = LTV;
    }


    public void fetchMostRecentAcceValue(){
        fetch.readDataMostRecent(LIMIT);
    }

    public boolean compareWithUTV() {
        if(fetch.getValueList().getFirst().getAcce_value() >= UTV) return true;
        else return false;
    }

    public boolean findValueUnderLTV(){
        for (AccelerometerPoint a : fetch.getValueList()) {
            if (a.getAcce_value() <= LTV) return true;
        }
        return false;
    }

    public void run() {
        while (!shutdown) {
            fetchMostRecentAcceValue();
            if(compareWithUTV()){

                if(findValueUnderLTV()){
                    System.out.println("************FALL-DETECTED!************");
                }

            }
            else shutdown = true;
        }
    }

}
