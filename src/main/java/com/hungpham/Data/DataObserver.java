package com.hungpham.Data;

public abstract class DataObserver {
    protected SensorData subject;
    protected String name;
    public abstract void update();
    public abstract String getName();
}
