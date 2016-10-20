package com.example.kamil.br;

/**
 * Created by kamil on 05.05.16.
 */
public class Record {

    private int id;
    private String name;
    private int rssi;
    private long time;
    private String direction;

    public Record()
    {
    }

    public Record(int id,String name,int rssi, long time, String direction)
    {
        this.id=id;
        this.name=name;
        this.rssi=rssi;
        this.time=time;
        this.direction=direction;
    }

    public Record(String name,int rssi, long time, String direction)
    {
        this.name=name;
        this.rssi=rssi;
        this.time=time;
        this.direction=direction;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public void setDirection(String direction)
    {
        this.direction = direction;
    }

    public void setTime(long time) {this.time = time;}

    public int getId() {return id;}

    public long getTime() {return time;}

    public int getRssi() {
        return rssi;
    }

    public String getName() {
        return name;
    }

    public String getDirection(){
        return direction;
    }
}
