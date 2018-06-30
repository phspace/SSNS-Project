package com.hungpham.Controller;

import com.hungpham.Algorithms.AngleChanged;
import com.hungpham.Algorithms.PressureBased;
import com.hungpham.GraphStage;

import java.util.concurrent.LinkedBlockingQueue;

import static com.hungpham.UI.MainScene.operatingDevicesNumber;

public class AlgorithmsController implements Runnable {
    private int conn;
    private PressureBased pressureBased;
    private AngleChanged Algorithm3;
    public static volatile LinkedBlockingQueue<Runnable> algorithmsQueue[] = new LinkedBlockingQueue[operatingDevicesNumber];

    public static volatile int voting[] = new int[operatingDevicesNumber];

    public AlgorithmsController(int conn) {
        this.conn = conn;
        algorithmsQueue[conn] = new LinkedBlockingQueue<>();
    }

    public void runAlgorithm() {
        while (SerialPortController.mode != 1) ;
        try {
            Thread.sleep(10000);
            while (!(GraphStage.command.equalsIgnoreCase("stop"))) {
                pressureBased = new PressureBased(conn);
                Algorithm3 = new AngleChanged(conn, 2.5, 0.4);
                algorithmsQueue[conn].add(Algorithm3);
                algorithmsQueue[conn].add(pressureBased);
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        runAlgorithm();
    }
}
