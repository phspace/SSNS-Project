package com.hungpham.database;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Pong;

public class DatabaseConnector {

    public static void test() {
        InfluxDB influxDB = InfluxDBFactory.connect("http://localhost:8086", "ssns", "ssns-project");
        Pong response = influxDB.ping();
        System.out.println(response);
        if (response.getVersion().equalsIgnoreCase("unknown")) {
            System.out.println("Error pinging server.");
            return;
        }
    }
}
