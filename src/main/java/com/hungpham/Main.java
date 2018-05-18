package com.hungpham;

import com.hungpham.Controller.SerialPortController;
import com.hungpham.Data.AcceProcessing;
import com.hungpham.Data.DataFactory;
import com.hungpham.UI.RTGraph;
import com.hungpham.UI.UI;

public class Main {

    public static void main(String[] args) {
        // write your code here

        UIRunner u = new UIRunner();
        u.start();
        RunSerialController t = new RunSerialController();
        t.start();
        DataFactory df = new DataFactory();
        Thread runSD = new Thread(df);
        runSD.start();
        AcceProcessing acceProcessing = new AcceProcessing();
        Thread accThread = new Thread(acceProcessing);
        accThread.start();
        RTGraph rtGraph = new RTGraph();
        Thread graph = new Thread(rtGraph);
        graph.start();
    }

    static class RunSerialController extends Thread {
        @Override
        public void run() {
            SerialPortController c = new SerialPortController();
        }
    }

    static class UIRunner extends Thread {
        @Override
        public void run() {
            UI ui = new UI();
        }
    }
}
