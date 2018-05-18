package com.hungpham.Algorithms;

public class KalmanFilter {
    double Q; //process noise covariance
    double R; //measurement noise covariance
    double x; //value
    double P; //estimation error covariance
    double K; //kalman gain

    public KalmanFilter(double Q, double R, double P, double intial_value) {
        this.Q = Q;
        this.R = R;
        this.P = P;
        this.x = intial_value;
    }

    public double kalman_filter_update(double measurement)
    {
        // prediction state
        P = P + Q;

        // measurement update
        K = P / (P + R);
        x = x + K * (measurement - x);
        P = (1 - K) * P;

        return x;
    }
}
