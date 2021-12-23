package com.example.adbapp.FillingOfList;

import com.example.adbapp.RecordList.Record;

import java.util.ArrayList;

public class RecordList {

    public ArrayList<Record> records = new ArrayList<>();

    protected void AddNewItemInRecords(int record_id, String _name, int _time, int field_type){
        Record record = new Record(record_id, _name, _time, field_type);

        records.add(record);
    }

    protected void ClearRecords(){
        records.clear();
    }

}
