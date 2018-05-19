package com.hungpham.database;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;

@Measurement(name = "accelerometer")
public class AccelerometerPoint {

    @Column(name = "time")
    private Instant time;

    @Column(name = "acce_value")
    private double acce_value;

    public double getAcce_value() {
        return acce_value;
    }
}
