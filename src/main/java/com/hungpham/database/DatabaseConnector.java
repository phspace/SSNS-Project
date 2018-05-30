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

    public void writeDB(Double acce) {
        Point point = Point.measurement("accelerometer")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("acce_value", acce)
                .build();
        database.write(point);
    }

    public LinkedList<AccelerometerPoint> readDB(String fromTime, String toTime) {
        String qu = "select * from accelerometer where time >= '" + fromTime + "' and time < '" + toTime + "'";
        Query q = new Query(qu, "ssns");
        QueryResult queryResult = database.query(q);
        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        List<AccelerometerPoint> accelerometerPointsPointList = resultMapper.toPOJO(queryResult, AccelerometerPoint.class);
        return (LinkedList<AccelerometerPoint>) accelerometerPointsPointList;
    }

    // this method create new query to take most recent values from database
    public LinkedList<AccelerometerPoint> readDB(long LIMIT) {
        String qu = "select acce_value from accelerometer ORDER BY time DESC LIMIT " + LIMIT;
        Query q = new Query(qu, "ssns");
        QueryResult queryResult = database.query(q);
        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        List<AccelerometerPoint> accelerometerPointsPointList = resultMapper.toPOJO(queryResult, AccelerometerPoint.class);
        return (LinkedList<AccelerometerPoint>) accelerometerPointsPointList;
    }

    // this method create new query to take values from database zwischen now() and now() - elapseTime
    public LinkedList<AccelerometerPoint> readDB(String elapseTime) {
        String qu = String.format("select * from accelerometer where time >= (now() - %s) and time < now()", elapseTime);
        Query q = new Query(qu, "ssns");
        QueryResult queryResult = database.query(q);
        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        List<AccelerometerPoint> accelerometerPointsPointList = resultMapper.toPOJO(queryResult, AccelerometerPoint.class);
        return (LinkedList<AccelerometerPoint>) accelerometerPointsPointList;
    }

    // dung timestamp format
    public LinkedList<AccelerometerPoint> readDB1(String fromTime, String toTime) {
        String qu = "select * from accelerometer where time >= " + fromTime + " and time < " + toTime;
        Query q = new Query(qu, "ssns");
        QueryResult queryResult = database.query(q);
        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        List<AccelerometerPoint> accelerometerPointsPointList = resultMapper.toPOJO(queryResult, AccelerometerPoint.class);
        return (LinkedList<AccelerometerPoint>) accelerometerPointsPointList;
    }

}
