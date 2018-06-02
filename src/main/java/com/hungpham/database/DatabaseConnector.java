package com.hungpham.database;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Pong;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DatabaseConnector {
    private InfluxDB database;

    public DatabaseConnector() {
        database = InfluxDBFactory.connect("http://localhost:8086", "ssns", "ssns-project");
        verifyDatabase();
    }

    private void verifyDatabase() {
        Pong response = database.ping();
        System.out.println(response);
        if (response.getVersion().equalsIgnoreCase("unknown")) {
            System.out.println("Error pinging server.");
            return;
        }
        database.setDatabase("ssns");
    }

    public void writeDB(String field, Double data) {
        Point point = Point.measurement("ssnsproject")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField(field, data)
                .build();
        database.write(point);
    }

    public LinkedList<SensorsPoint> readDB(String typeValue, String fromTime, String toTime) {
        String qu = "select " + typeValue + " from ssnsproject where time >= " + fromTime + " and time < " + toTime;
        Query q = new Query(qu, "ssns");
        QueryResult queryResult = database.query(q);
        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        List<SensorsPoint> sensorsPointsPointList = resultMapper.toPOJO(queryResult, SensorsPoint.class);
        return (LinkedList<SensorsPoint>) sensorsPointsPointList;
    }

}
