package com.hungpham;

import com.hungpham.Algorithms.AngleChanged;
import com.hungpham.Algorithms.Khoatest;
import com.hungpham.Algorithms.ThresholdBased;
import com.hungpham.Controller.DatabasePush;
import com.hungpham.Controller.SerialPortController;
import com.hungpham.Data.AcceProcessing;
import com.hungpham.Data.BaroProcessing;

import java.util.concurrent.LinkedBlockingQueue;

public class FunctionsWrapper {


    public static void startEverything() {
        // comment to turn off any function

        // to run serial port read write
        RunSerialController t = new RunSerialController();
        t.start(); // comment this line if not use

//        // to process acce data
//        AcceProcessing acceProcessing = new AcceProcessing();
//        Thread accThread = new Thread(acceProcessing);
//        accThread.start(); // comment this line if not use
//
//        // to process baro data
//        BaroProcessing baroProcessing = new BaroProcessing();
//        Thread baroThread = new Thread(baroProcessing);
//        baroThread.start(); // comment this line if not use

        AcceProcessing[] acceProcessings = new AcceProcessing[2];
        Thread[] acceProcessingThreads = new Thread[2];

        BaroProcessing[] baroProcessings = new BaroProcessing[2];
        Thread[] baroProcessingsThread = new Thread[2];

        // write to database
        DatabasePush[] pushAcce = new DatabasePush[2];
        Thread[] pushAcceDB = new Thread[2];

        DatabasePush[] pushBaro = new DatabasePush[2];
        Thread[] pushBaroDB = new Thread[2];

        for (int i = 0; i < 2; i++) {
            acceProcessings[i] = new AcceProcessing(i);
            acceProcessingThreads[i] = new Thread(acceProcessings[i]);
            acceProcessingThreads[i].start();

            baroProcessings[i] = new BaroProcessing(i);
            baroProcessingsThread[i] = new Thread(baroProcessings[i]);
            baroProcessingsThread[i].start();

            pushAcce[i] = new DatabasePush("acce", i);
            pushAcceDB[i] = new Thread(pushAcce[i]);
            pushAcceDB[i].start();

            pushBaro[i] = new DatabasePush("baro", i);
            pushBaroDB[i] = new Thread(pushBaro[i]);
            pushBaroDB[i].start();
        }

        // run fall detection algorithm
       // ThresholdBased Algorithm1 = new ThresholdBased(10, 0);
        //Thread Algorithm1Thread = new Thread(Algorithm1);
       // Algorithm1Thread.start();
        /*Khoatest Algorithm1 = new Khoatest(3);
        Thread Algorithm1Thread = new Thread(Algorithm1);
        Algorithm1Thread.start();*/

        AngleChanged Algorithm3 = new AngleChanged(3,0.4);
        Thread Algorithm3Thread = new Thread(Algorithm3);
        Algorithm3Thread.start();


    }

    static class RunSerialController extends Thread {
        @Override
        public void run() {
            SerialPortController c = new SerialPortController();
        }
    }

}
