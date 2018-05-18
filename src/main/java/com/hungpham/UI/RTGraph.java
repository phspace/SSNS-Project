package com.hungpham.UI;

import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingWorker;

import com.hungpham.Controller.Definitions;
import com.hungpham.Utils.Utils;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

/**
 * Creates a real-time chart using SwingWorker
 */
public class RTGraph implements Runnable {

    GraphWorker graphWorker;
    SwingWrapper<XYChart> sw;
    XYChart chart;
    private Utils utils;

    public RTGraph() {
        utils = new Utils();
    }

    private void go() {

        // Create Chart
        chart = QuickChart.getChart("SwingWorker XChart Real-time Demo", "Time", "Value", "randomWalk", new double[] { 0 }, new double[] { 0 });
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setXAxisTicksVisible(false);

        // Show it
        sw = new SwingWrapper<XYChart>(chart);
        sw.displayChart();

        graphWorker = new GraphWorker();
        graphWorker.execute();
    }

    @Override
    public void run() {
        go();
    }

    private class GraphWorker extends SwingWorker<Boolean, double[]> {

        LinkedList<Double> fifo = new LinkedList<>();

        public GraphWorker() {
            fifo.add(0.0);
        }

        @Override
        protected Boolean doInBackground() {

            while (!isCancelled() ) {
                int count = 0;
                double[] array = new double[300];
                while (count < 10) {
                    String accz = utils.TCPReceive(Definitions.GRAPH_PORT);
                    fifo.add(Double.parseDouble(accz));
                    if (fifo.size() > 300) {
                        fifo.removeFirst();
                    }
                    count++;
                }
                for (int i = 0; i < fifo.size(); i++) {
                    array[i] = fifo.get(i);
                }
                publish(array);
            }

            return true;
        }

        @Override
        protected void process(List<double[]> chunks) {

            //System.out.println("number of chunks: " + chunks.size());

            double[] mostRecentDataSet = chunks.get(chunks.size() - 1);

            chart.updateXYSeries("randomWalk", null, mostRecentDataSet, null);
            sw.repaintChart();

            long start = System.currentTimeMillis();
            long duration = System.currentTimeMillis() - start;
            try {
                Thread.sleep(20 - duration); // 40 ms ==> 25fps
                // Thread.sleep(400 - duration); // 40 ms ==> 2.5fps
            } catch (InterruptedException e) {
            }

        }
    }
}