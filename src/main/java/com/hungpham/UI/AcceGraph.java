package com.hungpham.UI;

import com.hungpham.Controller.Definitions;

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
            String acce = null;
            try {
                acce = acceGraph[conn].take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dataQ.add(Double.parseDouble(acce));
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
