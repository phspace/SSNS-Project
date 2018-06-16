package com.hungpham.Controller;

public interface Definitions {
    public static int RECEIVING_PACKAGE_PORT = 9001;
    public static int RECEIVING_ACC_VALUE_PORT = 9300;
    public static int RECEIVING_BAR_VALUE_PORT = 9200;
    public static int GRAPH_ACCE_PORT = 9004;
    public static int GRAPH_BARO_PORT = 9005;
    public static int DATABASE_ACCE_PORT = 9400;
    public static int DATABASE_BARO_PORT = 9500;
    public static String OS_NAME = System.getProperty("os.name", "").toLowerCase();
    public static String BARO_NOTIFY = "04FF0E1B05";
    public static String ACCE_NOTIFY = "04FF1A1B05";
}
