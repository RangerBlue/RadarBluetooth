package com.example.kamil.br;

/**
 * Created by kamil on 05.05.16.
 */
public class Record {

    private int id;
    private String name;
    private String rssi;
    private String time;
    private String direction;

    public Record()
    {
    }

    public Record(int id,String name,String rssi, String time, String direction)
    {
        this.id=id;
        this.name=name;
        this.rssi=rssi;
        this.time=time;
        this.direction=direction;
    }

    public Record(String name,String rssi, String time, String direction)
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

    public void setRssi(String rssi) {
        this.rssi = rssi;
    }

    public void setDirection(String direction)
    {
        this.direction = direction;
    }

    public void setTime(String time) {this.time = time;}

    public int getId() {return id;}

    public String getTime() {return time;}

    public String getRssi() {
        return rssi;
    }

    public String getName() {
        return name;
    }

    public String getDirection(){
        return direction;
    }
}
