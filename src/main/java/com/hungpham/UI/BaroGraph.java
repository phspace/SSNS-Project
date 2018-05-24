package com.hungpham.UI;

import com.hungpham.Controller.Definitions;

import java.util.concurrent.Executors;

public class BaroGraph extends RTGraph {
    private AddToQueue addToQueue;

    public BaroGraph() {
        init("Barometer Data");
    }

    private class AddToQueue implements Runnable {
        public void run() {
            // add a item of random data to queue
            String accz = utils.TCPReceive(Definitions.GRAPH_ACCE_PORT);
            dataQ.add(Double.parseDouble(accz));
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
