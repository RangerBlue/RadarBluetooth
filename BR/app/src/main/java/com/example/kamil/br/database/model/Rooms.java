package com.example.kamil.br.database.model;

/**
 * Obiekt opisujacy pomieszczenia
 * Created by Kamil on 2016-09-21.
 */
public class Rooms
{
    public static final String TABLE = "Rooms";

    public static final String ID_ROOMS = "idrooms";
    public static final String NAME = "name";
    public static final String WALK_RATIO = "walkRatio";
    public static final String A_VALUE= "A";
    public static final String N_VALUE= "n";


    //nazwy pól
    /**
     * identyfikator
     */
    private int idRooms;

    /**
     * nazwa pokoju
     */
    private String name;

    /**
     * typ pokoju
     */
    private float walkRatio;

    /**
     * wartość referencyjna RSSI
     */
    private float A;

    /**
     * wartość współczynnika dopasowującego funkcję
     */
    private float n;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdRooms() {
        return idRooms;
    }

    public void setIdRooms(int idRooms) {
        this.idRooms = idRooms;
    }

    public float getWalkRatio() {
        return walkRatio;
    }

    public void setWalkRatio(float walkRatio) {
        this.walkRatio = walkRatio;
    }

    public float getA() {
        return A;
    }

    public void setA(float a) {
        A = a;
    }

    public float getN() {
        return n;
    }

    public void setN(float n) {
        this.n = n;
    }
}
