package com.hungpham.UI.graphs;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class AcceGraph extends RTGraph {
    private AddToQueue addToQueue;

    private int conn;

    public static volatile LinkedBlockingQueue<String>[] acceGraph = new LinkedBlockingQueue[2];

    public AcceGraph(int conn) {
        this.conn = conn;
        acceGraph[conn] = new LinkedBlockingQueue<>();
        init("Accelerometer Data : " + conn, -1, 9, 0.5);
    }

    private class AddToQueue implements Runnable {
        public void run() {
            // add a item of random data to queue
            String data = null;
            double acceSQRT = 0;
            try {
                data = acceGraph[conn].take();
                int[] loc = new int[3];
                int j = 0;
                for (int i = -1; (i = data.indexOf(" ", i + 1)) != -1; i++) {
                    loc[j] = i;
                    j++;
                }
                double[] acce = new double[3];
                acce[0] = Double.parseDouble(data.substring(0, loc[0]));
                acce[1] = Double.parseDouble(data.substring(loc[0] + 1, loc[1]));
                acce[2] = Double.parseDouble(data.substring(loc[1] + 1, loc[2]));
                acceSQRT = Double.parseDouble(data.substring(loc[2] + 1));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dataQ.add(Double.parseDouble(String.valueOf(acceSQRT)));
            executor.execute(this);
        }
    }

    @Override
    public void executeGraph() {
        //-- Prepare Executor Services
        executor = Executors.newCachedThreadPool();
        addToQueue = new AddToQueue();
        executor.execute(addToQueue);
        //-- Prepare Timeline
        prepareTimeline();
    }
}
