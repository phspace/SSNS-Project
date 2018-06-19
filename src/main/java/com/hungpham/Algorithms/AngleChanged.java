
package com.hungpham.Algorithms;

import com.hungpham.Controller.DatabaseFetch;
import java.time.Instant;
import com.hungpham.Controller.SerialPortController;
import com.hungpham.database.SensorsPoint;


public class AngleChanged implements Runnable{
    private DatabaseFetch fetch = new DatabaseFetch();
    private Long UTVtimestamp;
    private int valueOfXIndicatingAngleChange = 0;

    double UTV; //upper threshold value
    double LTV; //lower threshold value

    public AngleChanged(double UTV, double LTV) {
        this.UTV = UTV;
        this.LTV = LTV;
    }

    public void fetchAccePointAtCurrentSystemTime(int elapsedTime){
        fetch.readAcceMostRecentfromNow(elapsedTime);
        //3 seconds?
    }

    public void fetchAccePointBeforeTimestamp(long timeStamp){
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

    public boolean findValueUnderLTV(){
        for (SensorsPoint a : fetch.getValueList()) {
            if (a.getAcce() <= LTV)  {
                return true;
            }
        }
        return false;
    }

    //Angle change detection
    public boolean findValueOfAngleChange(){
        int count = 0;
        String result = "";

        for (SensorsPoint a : fetch.getValueList()) {
            double Acc_x = a.getAcc_x();
            // -0.2 < Acc_x < 0.2
            if( (Double.compare(Acc_x, -0.3 ) >= 0) && (Double.compare(0.3, Acc_x) >= 0) ) {
                count++;
                System.out.println(Acc_x);
                valueOfXIndicatingAngleChange++;
            }
        }

        if (count > 13){
            return true;
        }
        else return false;

    }


    public void run() {
        while (true) {
            try
            {
                //check for low UTV and LTV to trigger most of the times
                fetchAccePointAtCurrentSystemTime(3);
                if(findValueOverUTV())
                {
                    fetchAccePointBeforeTimestamp(UTVtimestamp);
                    if(findValueUnderLTV())
                    {
                        Thread.sleep(2000);
                        fetchAccePointAtCurrentSystemTime(1);
                        if(findValueOfAngleChange())
                        {
                            System.out.println(" *$*$*$*$*$*$*$*$ Angle change detected *$*$*$*$*$*$*$*$* and the value is: " + valueOfXIndicatingAngleChange);
                            valueOfXIndicatingAngleChange = 0;
                            SerialPortController.mode = 0;
                            return;
                        }

                    }
                }


            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}