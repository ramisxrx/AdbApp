package com.example.adbapp.DataBase;

import static com.example.adbapp.DataBase.DataBaseHelper.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.adbapp.FillingOfList.OverviewListFilling;

public class WriteRequests {

    private String TAG = WriteRequests.class.getCanonicalName();

    SQLiteDatabase db;
    DataBaseHelper dataBaseHelper;
    ContentValues cv;

    public WriteRequests(Context context){
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
        Log.d(TAG, "AddRecord: ");
        cv.put(COLUMN_OBJECT_ID,object_id);
        cv.put(COLUMN_PARENT_ID,parent_id);
        cv.put(COLUMN_FIELD_ID,field_id);
        cv.put(COLUMN_TIME,System.currentTimeMillis()/1000);
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

    public void UpdateName(int name_id, String _name){
        cv.put(COLUMN_NAME,_name);
        db.update(TABLE_NAMES,cv,COLUMN_ID+"="+name_id,null);
        cv.clear();
    }

    public void SetCurrentTime(int record_id){
        cv.put(COLUMN_TIME,System.currentTimeMillis()/1000);
        db.update(TABLE_RECORDS,cv,COLUMN_ID+"="+record_id,null);
        cv.clear();
    }

    public void UpdateParent_id(int record_id,int parent_id){
        cv.put(COLUMN_PARENT_ID,parent_id);
        db.update(TABLE_RECORDS,cv,COLUMN_ID+"="+record_id,null);
        cv.clear();
    }

    public void DeleteRecord(int record_id){
        db.delete(TABLE_RECORDS,COLUMN_ID+"=?", new String[]{String.valueOf(record_id)});
    }

    public void DeleteField(int field_id){
        db.delete(TABLE_FIELDS,COLUMN_ID+"=?", new String[]{String.valueOf(field_id)});
    }

    public void DeleteName(int name_id){
        db.delete(TABLE_NAMES,COLUMN_ID+"=?", new String[]{String.valueOf(name_id)});
    }

    public void Destroy(){
        db.close();
        dataBaseHelper.close();
    }
}
