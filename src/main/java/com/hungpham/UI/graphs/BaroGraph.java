package com.hungpham.UI.graphs;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class BaroGraph extends RTGraph {
    private AddToQueue addToQueue;

    private int conn;

    public static volatile LinkedBlockingQueue<String>[] baroGraph = new LinkedBlockingQueue[2];

    public BaroGraph(int conn) {
        this.conn = conn;
        baroGraph[conn] = new LinkedBlockingQueue<>();
        init("Barometer Data : " + conn, 9950, 10050, 5);
    }

    private class AddToQueue implements Runnable {
        public void run() {
            // add a item of random data to queue
            String baro = null;
            try {
                baro = baroGraph[conn].take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dataQ.add(Double.parseDouble(baro));
            executor.execute(this);
            yAxis.setUpperBound(Double.parseDouble(baro) + 50);
            yAxis.setLowerBound(Double.parseDouble(baro) - 50);
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
