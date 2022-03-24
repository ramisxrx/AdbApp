package com.example.adbapp.FillingOfList;

import android.database.Cursor;

import com.example.adbapp.RecordList.Record;

import java.util.ArrayList;

public class RecordList {

    public ArrayList<Record> records = new ArrayList<>();

    protected void AddNewItemInRecords(int record_id, String _name, int _time, int field_type){
        Record record = new Record(record_id, _name, _time, field_type);

        records.add(record);
    }

    protected void AddNewItemInRecords(Cursor cursor){
        Record record = new Record(cursor.getInt(0),cursor.getString(2),cursor.getLong(3),cursor.getInt(4),cursor.getInt(1));
        record.setParent_id(cursor.getInt(1));
        records.add(record);
    }

    protected void ClearRecords(){
        records.clear();
    }

}
