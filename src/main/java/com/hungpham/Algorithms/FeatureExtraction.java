package com.hungpham.Algorithms;


import com.hungpham.Controller.DatabaseFetch;
import com.hungpham.database.SensorsPoint;
import java.util.LinkedList;

import static java.lang.Math.abs;

public class FeatureExtraction {
    private  double intercept, slope ;

    public FeatureExtraction() {

    }

    public void LinearRegression (double[] x, double[] y){
        if (x.length != y.length) {
            throw new IllegalArgumentException("array lengths are not equal");
        }
        int n = x.length;

        // first pass
        double sumx = 0.0, sumy = 0.0, sumx2 = 0.0;
        for (int i = 0; i < n; i++) {
            sumx  += x[i];
            sumx2 += x[i]*x[i];
            sumy  += y[i];
        }
        double xbar = sumx / n;
        double ybar = sumy / n;

        // second pass: compute summary statistics
        double xxbar = 0.0, yybar = 0.0, xybar = 0.0;
        for (int i = 0; i < n; i++) {
            xxbar += (x[i] - xbar) * (x[i] - xbar);
            yybar += (y[i] - ybar) * (y[i] - ybar);
            xybar += (x[i] - xbar) * (y[i] - ybar);
        }
        slope  = xybar / xxbar;
        intercept = ybar - slope * xbar;
    }
    public double intercept() {
        return intercept;
    }
    public double slope() {
        return slope;
    }
    public double pressureShift (double[] x, double[] y){
        double sumX=0, sumY=0;
        double result;
        for (int i=0; i<x.length; i++) sumX=sumX+x[i];
        for (int i=0; i<y.length; i++) sumY=sumY+y[i];
        result =abs((sumY/y.length)-(sumX/x.length));
        return result;
    }

}
