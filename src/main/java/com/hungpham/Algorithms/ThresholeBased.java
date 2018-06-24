package com.hungpham.Algorithms;

import com.hungpham.Controller.DatabaseFetch;
import com.hungpham.database.SensorsPoint;

import java.time.Instant;

public abstract class ThresholeBased {
    protected DatabaseFetch fetch;
    protected Long UTVtimestamp;

    protected double UTV; //upper threshold value
    protected double LTV; //lower threshold value

    protected int conn;

    protected void fetchAccePointAtCurrentSystemTime(int elapsedTime) {
        fetch.readAcceMostRecentfromNow(elapsedTime);
        //3 seconds?
    }

    protected boolean findValueOverUTV() {
        for (SensorsPoint a : fetch.getValueList()) {
            if (a.getAcce() >= UTV) {
                Instant time = a.getTime();
                UTVtimestamp = time.getEpochSecond();
                return true;
            }
        }
        return false;
    }

    protected boolean findValueUnderLTV() {
        for (SensorsPoint a : fetch.getValueList()) {
            if (a.getAcce() <= LTV) {
                return true;
            }
        }
        return false;
    }

}
