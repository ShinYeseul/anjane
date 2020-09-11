package com.test.anjane.earthquakes;

public class EarData {

    private String Time = new String();
    private String size = new String();
    private String lat = new String();
    private String lot = new String();
    private String gps = new String();


    public String getListTime(){
        return Time;
    }
    public String getListgps(){ return gps; }
    public String getListsize(){
        return size;
    }
    public String getListlat(){
        return lat;
    }
    public String getListlot(){
        return lot;
    }

    public void setlistTime(String Time) {
        this.Time = Time;
    }
    public float setlistlat(String lat) {
        this.lat = lat;
        return 0;
    }
    public float setlistlot(String lot) {
        this.lot = lot;
        return 0;
    }
    public void setlistgps(String gps) {
        this.gps = gps;
    }
    public void setlistsize(String size) {
        this.size = size;
    }
}