package com.hungpham.Data;

public abstract class SensorsObserver {
    protected SerialData subject;
    protected String name;
    public abstract void update();
    public abstract String getName();

}
