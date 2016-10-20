package com.example.kamil.br;

/**
 * Created by kamil on 05.05.16.
 */
public class Record {

    private int id;
    private String name;
    private String address;
    private int rssi;
    private long time;
    private int edgeNumber;

    public Record()
    {
    }

    public Record(int id,String name,int rssi, long time, int edgeNumber)
    {
        this.id=id;
        this.name=name;
        this.rssi=rssi;
        this.time=time;
        this.edgeNumber=edgeNumber;
    }

    public Record(String name,int rssi, long time, int edgeNumber)
    {
        this.name=name;
        this.rssi=rssi;
        this.time=time;
        this.edgeNumber=edgeNumber;
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

    public void setEdgeNumber(int edgeNumber)
    {
        this.edgeNumber = edgeNumber;
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

    public int getEdgeNumber(){
        return edgeNumber;
    }
}
