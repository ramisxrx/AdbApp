package com.example.adbapp.DataBase;

import static com.example.adbapp.DataBase.DataBaseHelper.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class WriteRequests {

    SQLiteDatabase db;
    DataBaseHelper dataBaseHelper;
    ContentValues cv;

    WriteRequests(Context context){
        dataBaseHelper = new DataBaseHelper(context);
        db = dataBaseHelper.getWritableDatabase();
        cv = new ContentValues();
    }

    public void AddObject(int object_id){
        cv.put(COLUMN_ID,object_id);
        db.insert(TABLE_OBJECTS,null,cv);
        cv.clear();
    }

    public void AddRecord(int object_id,int parent_id,int field_id,int _time){
        cv.put(COLUMN_TYPE,object_id);
        cv.put(COLUMN_PARENT_ID,parent_id);
        cv.put(COLUMN_FIELD_ID,field_id);
        cv.put(COLUMN_TIME,_time);
        db.insert(TABLE_RECORDS,null,cv);
        cv.clear();
    }

    public void AddName(String _name){
        cv.put(COLUMN_NAME,_name);
        db.insert(TABLE_NAMES,null,cv);
        cv.clear();
    }

    public void AddField(int _type, int name_id){
        cv.put(COLUMN_TYPE,_type);
        cv.put(COLUMN_NAME_ID,name_id);
        db.insert(TABLE_FIELDS,null,cv);
        cv.clear();
    }

    public void Destroy(){
        db.close();
        dataBaseHelper.close();
    }
}
