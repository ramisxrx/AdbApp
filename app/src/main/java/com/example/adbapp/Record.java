package com.example.adbapp;

import java.util.Date;

public class Record {

    private String _name;
    private int _time;


    public Record(String _name, int _time) {

        this._name = _name;
        this._time = _time;
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
}
