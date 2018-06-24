package com.hungpham;

import com.hungpham.Controller.AlgorithmsController;
import com.hungpham.Controller.AlgorithmsRunner;
import com.hungpham.Controller.DatabasePush;
import com.hungpham.Controller.SerialPortController;
import com.hungpham.Data.AcceProcessing;
import com.hungpham.Data.BaroProcessing;
import com.hungpham.UI.MainScene;

import static com.hungpham.Controller.AlgorithmsController.voting;
import static com.hungpham.UI.MainScene.operatingDevicesNumber;

public class FunctionsWrapper {


    public static void startEverything() {
        // comment to turn off any function
        System.out.println("Backend functions are starting...");

        MainScene mainScene = new MainScene();
        mainScene.checkPort();


        while (MainApplication.mode == 0) ;

        mainScene.checkReadyOperating();

        // to run serial port read write
        RunSerialController[] serialControllers = new RunSerialController[operatingDevicesNumber];

        AcceProcessing[] acceProcessings = new AcceProcessing[operatingDevicesNumber];
        Thread[] acceProcessingThreads = new Thread[operatingDevicesNumber];

        BaroProcessing[] baroProcessings = new BaroProcessing[operatingDevicesNumber];
        Thread[] baroProcessingsThread = new Thread[operatingDevicesNumber];

        // write to database
        DatabasePush[] pushAcce = new DatabasePush[operatingDevicesNumber];
        Thread[] pushAcceDB = new Thread[operatingDevicesNumber];

        DatabasePush[] pushBaro = new DatabasePush[operatingDevicesNumber];
        Thread[] pushBaroDB = new Thread[operatingDevicesNumber];


        // for running algorithm
        AlgorithmsController[] ac = new AlgorithmsController[operatingDevicesNumber];
        Thread[] acThread = new Thread[operatingDevicesNumber];

        AlgorithmsRunner[] ar = new AlgorithmsRunner[operatingDevicesNumber];
        Thread[] arThread = new Thread[operatingDevicesNumber];

        CheckVoting[] checkVoting = new CheckVoting[operatingDevicesNumber];

        for (int i = 0; i < operatingDevicesNumber; i++) {
            serialControllers[i] = new RunSerialController(i);
            serialControllers[i].start();

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

            ac[i] = new AlgorithmsController(i);
            acThread[i] = new Thread(ac[i]);
            acThread[i].start();

            ar[i] = new AlgorithmsRunner(i);
            arThread[i] = new Thread(ar[i]);
            arThread[i].start();

            checkVoting[i] = new CheckVoting(i);
            checkVoting[i].start();
        }

        System.out.println("All functions started.");

    }

    static class RunSerialController extends Thread {
        private int conn;
        private SerialPortController serialPortController;

        public RunSerialController(int conn) {
            this.conn = conn;
        }

        @Override
        public void run() {
            System.out.println("Starting connection with launchpad number " + conn);
            serialPortController = new SerialPortController(conn);
        }
    }

    static class CheckVoting extends Thread {
        private int conn;

        public CheckVoting(int conn) {
            this.conn = conn;
        }

        @Override
        public void run() {
            while (true) {
                if (voting[conn] == 2) {
                    MainApplication.command = "stop";
                    System.out.println("!!!!!!!!!!!!*********** Fall Detected ********!!!!!!!!!!!!");
                    break;
                }
            }
        }
    }

}
