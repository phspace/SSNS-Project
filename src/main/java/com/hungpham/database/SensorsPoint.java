package com.hungpham.database;

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;

@Measurement(name = "ssnsproject")
public class SensorsPoint {

    @Column(name = "time")
    private Instant time;

    @Column(name = "acce_value")
    private double acce_value;

    @Column(name = "baro_value")
    private double baro_value;

    public double getBaro() {
        return baro_value;
    }

    public double getAcce() {
        return acce_value;
    }

    public Instant getTime() {
        return time;
    }
}
