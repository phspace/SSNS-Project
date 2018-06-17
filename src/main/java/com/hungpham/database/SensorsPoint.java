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

    @Column(name = "conn")
    private String conn;

    @Column(name = "acc_x")
    private double acc_x;

    @Column(name = "acc_y")
    private double acc_y;

    @Column(name = "acc_z")
    private double acc_z;

    public double getAcc_x() {
        return acc_x;
    }

    public double getAcc_y() {
        return acc_y;
    }

    public double getAcc_z() {
        return acc_z;
    }

    public double getBaro() {
        return baro_value;
    }

    public String getConn() {
        return conn;
    }

    public double getAcce() {
        return acce_value;
    }

    public Instant getTime() {
        return time;
    }
}
