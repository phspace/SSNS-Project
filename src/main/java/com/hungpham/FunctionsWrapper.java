package com.hungpham;

import com.hungpham.Algorithms.ThresholdBased;
import com.hungpham.Controller.DatabasePush;
import com.hungpham.Controller.SerialPortController;
import com.hungpham.Data.AcceProcessing;
import com.hungpham.Data.BaroProcessing;
import jssc.SerialPortList;

import java.io.IOException;

public class FunctionsWrapper {


    public static void startEverything() {
        // comment to turn off any function
        System.out.println("Backend functions are starting...");
        // to run serial port read write
        RunSerialController t = new RunSerialController();
        t.start(); // comment this line if not use

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
        ThresholdBased Algorithm1 = new ThresholdBased(10, 0);
        Thread Algorithm1Thread = new Thread(Algorithm1);
        Algorithm1Thread.start();

    }

    private static void checkPorts() {
        String[] portNames = SerialPortList.getPortNames();

        if (portNames.length == 0) {
            System.out.println("There are no serial-ports :( You can use an emulator, such ad VSPE, to create a virtual serial port.");
            System.out.println("Press Enter to exit...");
            try {
                System.in.read();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return;
        }

        for (int i = 0; i < portNames.length; i++) {
            System.out.println(portNames[i]);
        }
    }

    static class RunSerialController extends Thread {
        @Override
        public void run() {
            checkPorts();
            while (MainApplication.mode == 0) ;
            if (MainApplication.mode == 1) {
                SerialPortController c = new SerialPortController(0);
            } else if (MainApplication.mode == 2) {
                System.out.println("Start 2 launchpad");
                SerialPortController c = new SerialPortController(0);
                SerialPortController c1 = new SerialPortController(1);
            }
        }
    }

}
