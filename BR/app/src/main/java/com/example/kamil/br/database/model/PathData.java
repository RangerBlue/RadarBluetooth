package com.example.kamil.br.database.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by kamil on 30.07.16.
 */
public class PathData implements Serializable
{
    public static final String TABLE = "PathData";

    public static final String ID_PATHDATA = "idpathadata";
    public static final String A = "a";
    public static final String B = "b";
    public static final String X = "x";
    public static final String LINEAR = "linear";
    public static final String P1 = "p1";
    public static final String P2 = "p2";
    public static final String EDGENUMBER = "edgenumber";
    public static final String ID_ROOMS = "idrooms";

    //nazwy pól
    private int idPathData;
    private float a;
    private float b;
    private float x;
    private int ifLinear;
    private float p1;
    private float p2;
    private int edgeNumber;
    private int idRooms;

    public int getIdPathData() {
        return idPathData;
    }

    public void setIdPathData(int idPathData) {
        this.idPathData = idPathData;
    }



    public int getIdRooms() {
        return idRooms;
    }

    public void setIdRooms(int idRooms) {
        this.idRooms = idRooms;
    }

    public PathData() {
    }

    public int getEdgeNumber() {
        return edgeNumber;


    }

    public void setEdgeNumber(int edgeNumber) {
        this.edgeNumber = edgeNumber;
    }

    public float getP1() {
        return p1;
    }

    public void setP1(float p1) {
        this.p1 = p1;
    }

    public float getP2() {
        return p2;
    }

    public float getP2Reverse(){return -p2;}

    public void setP2(float p2) {
        this.p2 = p2;
    }



    public int getIsIfLinear() {
        return ifLinear;
    }

    public float getX() {
        return x;
    }

    public void setIfLinear(int ifLinear) {
        this.ifLinear = ifLinear;
    }

    public void setX(float x) {
        this.x = x;
    }

    public PathData(float a, float b, int p1, int p2) {
        this.a = a;
        this.b = b;
        this.p1 = p1;
        this.p2 = p2;
        this.ifLinear=1;
    }

    public PathData(float x, int p1, int p2) {
        this.x = x;
        this.p1 = p1;
        this.p2 = p2;
        this.ifLinear=0;
    }

    public float getB() {
        return b;
    }

    public float getA() {
        return a;
    }

    public void setA(float a) {
        this.a = a;
    }

    public void setB(float b) {
        this.b = b;
    }


}
