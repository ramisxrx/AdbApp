package com.example.adbapp;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;

public class Record {

    private int record_id,_time, level, parent_id=0;
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

    public int getParent_id(){
        return this.parent_id;
    }

    public void setParent_id(int parent_id){
        this.parent_id = parent_id;
    }

    public boolean getHasChildRec(){
        return this.hasChildRec;
    }

    public void setHasChildRec(boolean hasChildRec){
        this.hasChildRec = hasChildRec;
    }


    public static boolean CursorMatchFound_1(Cursor cursor, int columnIndex_1, int columnIndex_2, ArrayList<Record> records, int valToCheck){
        for(int i=0;i<cursor.getCount();i++){
            cursor.moveToPosition(i);
            if(cursor.getInt(columnIndex_1)==valToCheck && RecIdMatchNotFound(records,cursor.getInt(columnIndex_2)))
                return true;
        }
        return false;
    }

    public static boolean RecIdMatchNotFound (ArrayList<Record> records, int idToCheck){
        for(int i=0; i<records.size();i++){
            if (records.get(i).getRecord_id() == idToCheck)
                return false;
        }
        return true;
    }

    public static boolean CursorMatchFound_2(Cursor cursor, int columnIndex, int valToCheck){
        for(int i=0;i<cursor.getCount();i++){
            cursor.moveToPosition(i);
            if(cursor.getInt(columnIndex)==valToCheck)
                return true;
        }
        return false;
    }

    public static int posInListById(ArrayList<Record> records, int _id){
        for(int i=0; i<records.size();i++) {
            if(records.get(i).getRecord_id()==_id)
                return i;
        }
        return -1;
    }

    public static int posInListByParentId(ArrayList<Record> records, int parent_id){
        for(int i=0; i<records.size();i++) {
            if(records.get(i).getParent_id()==parent_id)
                return i;
        }
        return -1;
    }

    public static int ParentIdByRecId(ArrayList<Record> records, int _id){
        for(int i=0; i<records.size();i++) {
            if(records.get(i).getRecord_id()==_id)
                return records.get(i).getParent_id();
        }
        return -1;
    }

}
