package com.hungpham.Controller;

import com.hungpham.GraphStage;

public class AlgorithmsRunner implements Runnable{

    private int conn;

    public AlgorithmsRunner(int conn) {
        this.conn = conn;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        while(SerialPortController.mode != 1);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (!(GraphStage.command.equalsIgnoreCase("stop"))) {
            try {
                Runnable task = AlgorithmsController.algorithmsQueue[conn].take();
                task.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
