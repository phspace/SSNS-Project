package com.hungpham;

import com.hungpham.Controller.DatabaseFetch;
import com.hungpham.Controller.DatabasePush;
import com.hungpham.Controller.SerialPortController;
import com.hungpham.Data.AcceProcessing;
import com.hungpham.Data.BaroProcessing;
import com.hungpham.Data.DataFactory;
import com.hungpham.database.AccelerometerPoint;

public class FunctionsWrapper {

    public static void readFromDBExample() {
        DatabaseFetch fetch = new DatabaseFetch();
        fetch.readDataInTimeInterval("2018-05-19T13:33:58Z", "2018-05-19T13:34:01.766Z");
        for (AccelerometerPoint a : fetch.getValueList()) {
            System.out.println(a.getAcce_value());
        }

    }

    public static void startEverything() {
        // comment to turn off any function

        // to run serial port read write
        RunSerialController t = new RunSerialController();
        t.start(); // comment this line if not use

        // to start reading sensor value
        DataFactory df = new DataFactory();
        Thread runSD = new Thread(df);
        runSD.start(); // comment this line if not use

        // to process acce data
        AcceProcessing acceProcessing = new AcceProcessing();
        Thread accThread = new Thread(acceProcessing);
        accThread.start(); // comment this line if not use

        // to process baro data
        BaroProcessing baroProcessing = new BaroProcessing();
        Thread baroThread = new Thread(baroProcessing);
        baroThread.start(); // comment this line if not use

        // write to database
        DatabasePush push = new DatabasePush();
        Thread pushDB = new Thread(push);
        pushDB.start(); // comment this line if not use

    }

    static class RunSerialController extends Thread {
        @Override
        public void run() {
            SerialPortController c = new SerialPortController();
        }
    }

}
