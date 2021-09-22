package com.example.adbapp;

import java.util.Date;

public class Record {

    private int record_id,_time, level;
    private String _name;
    private boolean hasChildRec=false;


    public Record(int record_id, String _name, int _time,int level) {

        this.record_id =record_id;
        this._name = _name;
        this._time = _time;
        this.level = level;
    }

    public int getRecord_id(){
        return this.record_id;
    }

    public void setRecord_id(int record_id){
        this.record_id = record_id;
    }


    public String getName(){
        return this._name;
    }

    public void setName(String _name){
        this._name = _name;
    }

    public int getTime(){
        return this._time;
    }

    public void setTime(int _time){
        this._time = _time;
    }

    public int getLevel(){
        return this.level;
    }

    public void setLevel(int level){
        this.level = level;
    }

    public boolean getHasChildRec(){
        return this.hasChildRec;
    }

    public void setHasChildRec(boolean hasChildRec){
        this.hasChildRec = hasChildRec;
    }
}
