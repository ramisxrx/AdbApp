package com.example.adbapp.DataBase;

import static com.example.adbapp.DataBase.DataBaseHelper.*;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ReadRequests {

   SQLiteDatabase db;
   DataBaseHelper dataBaseHelper;

    public ReadRequests(Context context){
        dataBaseHelper = new DataBaseHelper(context);
        db = dataBaseHelper.getReadableDatabase();
    }

    public Cursor getRecords(int object_id){
        Cursor cursor;

        cursor = db.rawQuery("SELECT "
                +TABLE_RECORDS+"."+COLUMN_ID+","
                +COLUMN_PARENT_ID+","
                +COLUMN_NAME+","
                +COLUMN_TIME+","
                +COLUMN_TYPE+
                " FROM "+
                TABLE_RECORDS+" INNER JOIN "+TABLE_FIELDS+" ON "+TABLE_RECORDS+"."+COLUMN_FIELD_ID+"="+TABLE_FIELDS+"."+COLUMN_ID+
                " INNER JOIN "+TABLE_NAMES+" ON "+TABLE_FIELDS+"."+COLUMN_NAME_ID+"="+TABLE_NAMES+"."+COLUMN_ID+
                " WHERE "+COLUMN_OBJECT_ID+"=?", new String[]{String.valueOf(object_id)});

        return cursor;
    }

    public Cursor getRecords_2(int parent_id){
        Cursor cursor;

        cursor = db.rawQuery("SELECT "
                +TABLE_RECORDS+"."+COLUMN_ID+","
                +COLUMN_PARENT_ID+","
                +COLUMN_NAME+","
                +COLUMN_TIME+","
                +COLUMN_TYPE+","
                +COLUMN_OBJECT_ID+
                " FROM "+
                TABLE_RECORDS+" INNER JOIN "+TABLE_FIELDS+" ON "+TABLE_RECORDS+"."+COLUMN_FIELD_ID+"="+TABLE_FIELDS+"."+COLUMN_ID+
                " INNER JOIN "+TABLE_NAMES+" ON "+TABLE_FIELDS+"."+COLUMN_NAME_ID+"="+TABLE_NAMES+"."+COLUMN_ID+
                " WHERE "+COLUMN_PARENT_ID+"=?", new String[]{String.valueOf(parent_id)});

        return cursor;
    }

    public Cursor getRecords_3(int field_id){
        Cursor cursor;

        cursor = db.rawQuery("SELECT "
                +COLUMN_ID+","
                +COLUMN_PARENT_ID+
                " FROM "+
                TABLE_RECORDS+
                " WHERE "+COLUMN_FIELD_ID+"=?", new String[]{String.valueOf(field_id)});

        return cursor;
    }

    public Cursor getRecords_4(int record_id){
        Cursor cursor;

        cursor = db.rawQuery("SELECT "
                +TABLE_RECORDS+"."+COLUMN_ID+","
                +COLUMN_PARENT_ID+","
                +COLUMN_NAME+","
                +COLUMN_TIME+","
                +COLUMN_TYPE+","
                +COLUMN_OBJECT_ID+
                " FROM "+
                TABLE_RECORDS+" INNER JOIN "+TABLE_FIELDS+" ON "+TABLE_RECORDS+"."+COLUMN_FIELD_ID+"="+TABLE_FIELDS+"."+COLUMN_ID+
                " INNER JOIN "+TABLE_NAMES+" ON "+TABLE_FIELDS+"."+COLUMN_NAME_ID+"="+TABLE_NAMES+"."+COLUMN_ID+
                " WHERE "+TABLE_RECORDS+"."+COLUMN_ID+"=?", new String[]{String.valueOf(record_id)});

        return cursor;
    }

    public Cursor getFields(String _name){
        return db.rawQuery("SELECT "
                +TABLE_FIELDS+"."+COLUMN_ID+","
                +COLUMN_NAME+","
                +COLUMN_TYPE+
                " FROM "+
                TABLE_FIELDS+" INNER JOIN "+TABLE_NAMES+" ON "+TABLE_FIELDS+"."+COLUMN_NAME_ID+"="+TABLE_NAMES+"."+COLUMN_ID+
                " WHERE "+COLUMN_NAME+" LIKE ?", new String[]{"%" + _name + "%"});
    }

    public Cursor getObjects(){
        return db.rawQuery("SELECT "
                +COLUMN_ID+
                " FROM "+
                TABLE_OBJECTS, null);
    }

    public Cursor getNames(String _name){
        return db.rawQuery("SELECT "
                +COLUMN_ID+
                " FROM "+
                TABLE_NAMES+
                " WHERE "+COLUMN_NAME+"=?", new String[]{_name});
    }

    public Cursor getObjectNameType(int record_id){
        Cursor cursor;

        cursor = db.rawQuery("SELECT "
                +COLUMN_OBJECT_ID+","
                +COLUMN_NAME+","
                +COLUMN_TYPE+
                " FROM "+
                TABLE_RECORDS+" INNER JOIN "+TABLE_FIELDS+" ON "+TABLE_RECORDS+"."+COLUMN_FIELD_ID+"="+TABLE_FIELDS+"."+COLUMN_ID+
                " INNER JOIN "+TABLE_NAMES+" ON "+TABLE_FIELDS+"."+COLUMN_NAME_ID+"="+TABLE_NAMES+"."+COLUMN_ID+
                " WHERE "+TABLE_RECORDS+"."+COLUMN_ID+"=?", new String[]{String.valueOf(record_id)});

        return cursor;
    }

    public Cursor getFields_2(int name_id){
        return db.rawQuery("SELECT "
                +COLUMN_ID+
                " FROM "+
                TABLE_FIELDS+
                " WHERE "+COLUMN_NAME_ID+"=?", new String[]{String.valueOf(name_id)});
    }

    public Cursor getRecords_5(int field_id){
        Cursor cursor;

        cursor = db.rawQuery("SELECT "
                +TABLE_RECORDS+"."+COLUMN_ID+","
                +COLUMN_PARENT_ID+","
                +COLUMN_NAME+","
                +COLUMN_TIME+","
                +COLUMN_TYPE+","
                +COLUMN_OBJECT_ID+
                " FROM "+
                TABLE_RECORDS+" INNER JOIN "+TABLE_FIELDS+" ON "+TABLE_RECORDS+"."+COLUMN_FIELD_ID+"="+TABLE_FIELDS+"."+COLUMN_ID+
                " INNER JOIN "+TABLE_NAMES+" ON "+TABLE_FIELDS+"."+COLUMN_NAME_ID+"="+TABLE_NAMES+"."+COLUMN_ID+
                " WHERE "+COLUMN_FIELD_ID+"=?", new String[]{String.valueOf(field_id)});

        return cursor;
    }

    public Cursor getRecordsTEST(){
        Cursor cursor;

        cursor = db.rawQuery("SELECT "
                +TABLE_RECORDS+"."+COLUMN_ID+","
                +COLUMN_OBJECT_ID+","
                +COLUMN_PARENT_ID+","
                +COLUMN_NAME+","
                +COLUMN_NAME_ID+","
                +COLUMN_FIELD_ID+","
                +COLUMN_TIME+","
                +COLUMN_TYPE+
                " FROM "+
                TABLE_RECORDS+" INNER JOIN "+TABLE_FIELDS+" ON "+TABLE_RECORDS+"."+COLUMN_FIELD_ID+"="+TABLE_FIELDS+"."+COLUMN_ID+
                " INNER JOIN "+TABLE_NAMES+" ON "+TABLE_FIELDS+"."+COLUMN_NAME_ID+"="+TABLE_NAMES+"."+COLUMN_ID, null);

        return cursor;
    }

    public void Destroy(){
        db.close();
        dataBaseHelper.close();
    }
}
