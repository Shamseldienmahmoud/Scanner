package com.example.scanner.model;

public class StationsItem {
    String StationName;
    int LineName;
    int id;

    public String getStationName() {
        return StationName;
    }

    public void setStationName(String stationName) {
        StationName = stationName;
    }

    public int getLineName() {
        return LineName;
    }

    public void setLineName(int lineName) {
        LineName = lineName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StationsItem(String stationName, int lineName, int id) {
        StationName = stationName;
        LineName = lineName;
        this.id = id;
    }
}
