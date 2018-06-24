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
    private int conn;

    public DatabaseConnector(int conn) {
        this.conn = conn;
        database = InfluxDBFactory.connect("http://localhost:8086", "ssns" + conn, "ssns");
        database.setDatabase("ssns" + conn);
//        verifyDatabase();
    }

    private void verifyDatabase() {
        Pong response = database.ping();
        System.out.println(response);
        if (response.getVersion().equalsIgnoreCase("unknown")) {
            System.out.println("Error pinging server.");
            return;
        }
        database.setDatabase("ssns" + conn);
    }

    public void writeDB(String field, Double data) {
        Point point = Point.measurement("ssnsproject")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField(field, data)
                .build();
        database.write(point);
    }

    public void writeDB(String field, Double data, String conn) {
        Point point = Point.measurement("ssnsproject")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField(field, data)
                .addField("conn", conn)
                .build();
        database.write(point);
    }

    public void writeAcceDB(String field, Double data, String conn, double[] acc) {
        Point point = Point.measurement("ssnsproject")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField(field, data)
                .addField("acc_x", acc[0])
                .addField("acc_y", acc[1])
                .addField("acc_z", acc[2])
                .addField("conn", conn)
                .build();
        database.write(point);
    }

    public LinkedList<SensorsPoint> readDB(String typeValue, String fromTime, String toTime) {
        String qu = "select " + typeValue + " from ssnsproject where time >= " + fromTime + " and time < " + toTime;
        Query q = new Query(qu, "ssns" + conn);
        QueryResult queryResult = database.query(q);
        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        List<SensorsPoint> sensorsPointsPointList = resultMapper.toPOJO(queryResult, SensorsPoint.class);
        return (LinkedList<SensorsPoint>) sensorsPointsPointList;
    }

    public LinkedList<SensorsPoint> executeQuery(String query) {
        Query q = new Query(query, "ssns" + conn);
        QueryResult queryResult = database.query(q);
        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        List<SensorsPoint> sensorsPointsPointList = resultMapper.toPOJO(queryResult, SensorsPoint.class);
        return (LinkedList<SensorsPoint>) sensorsPointsPointList;
    }

}
