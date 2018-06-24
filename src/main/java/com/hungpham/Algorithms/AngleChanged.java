
package com.hungpham.Algorithms;

import com.hungpham.Controller.DatabaseFetch;
import com.hungpham.database.SensorsPoint;

import static com.hungpham.Controller.AlgorithmsController.voting;

public class AngleChanged extends ThresholeBased implements Runnable {

    public AngleChanged(int conn, double UTV, double LTV) {
        this.UTV = UTV;
        this.LTV = LTV;
        this.conn = conn;
        fetch = new DatabaseFetch(conn);
    }

    //Angle change detection
    public boolean findValueOfAngleChange() {
        int count = 0;
        String result = "";

        for (SensorsPoint a : fetch.getValueList()) {
            double Acc_x = a.getAcc_x();
            // -0.2 < Acc_x < 0.2
            if ((Double.compare(Acc_x, -0.3) >= 0) && (Double.compare(0.3, Acc_x) >= 0)) {
                count++;
//                System.out.println(Acc_x);
            }
        }
        if (count > 10) {
            return true;
        } else return false;
    }

    public void run() {
        try {
            //check for low UTV and LTV to trigger most of the times
            fetchAccePointAtCurrentSystemTime(6);
            if (findValueOverUTV()) {
                System.out.println("UTV detected.");
                if (findValueUnderLTV()) {
                    fetchAccePointAtCurrentSystemTime(2);
                    if (findValueOfAngleChange() && voting[conn] != 2) {
                        System.out.println("********* Angle change detected *********");
                        voting[conn] = 1;
                    } else if (voting[conn] == 2) ;
                    else voting[conn] = 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}