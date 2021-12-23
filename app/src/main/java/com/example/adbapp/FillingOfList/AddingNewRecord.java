package com.example.adbapp.FillingOfList;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.adbapp.DataBase.ReadRequests;
import com.example.adbapp.DataBase.WriteRequests;

public class AddingNewRecord {

    private String TAG = AddingNewRecord.class.getCanonicalName();
    
    private WriteRequests writeRequests;
    private ReadRequests readRequests;
    private int parent_id, object_id, _time, name_id, _type;
    private Cursor cursor;
    private String _name;

    public String parentName;
    public int parentType,field_id;

    public AddingNewRecord(Context context, int parent_id, String _name, int _type){
        this.parent_id=parent_id;
        this._name=_name;
        this._type=_type;
        readRequests = new ReadRequests(context);
        writeRequests = new WriteRequests(context);

        DefinitionsInit();
    }

    public void Save(){
        if(parent_id==0)
            AddNewObject();

        if(field_id>0) {
            Log.d(TAG, "Save: writeRequests.AddRecord");
            writeRequests.AddRecord(object_id, parent_id, field_id, _time);
        }
        else{
            if(_name.length()>0){
                DefinitionNameId();
                DefinitionFieldId();
                writeRequests.AddRecord(object_id,parent_id,field_id,_time);
            }
        }
    }

    private void DefinitionsInit(){
        if(parent_id>0){
            cursor = readRequests.getObjectNameType(parent_id);
            if(cursor.moveToFirst()) {
                object_id = cursor.getInt(0);
                parentName = cursor.getString(1);
                parentType = cursor.getInt(2);
            }
        }
    }

    private void AddNewObject(){
        cursor = readRequests.getObjects();
        if(cursor.moveToLast())
            object_id = cursor.getInt(0)+1;
        else
            object_id = 1;
        writeRequests.AddObject(object_id);
    }

    private void DefinitionNameId(){
        cursor = readRequests.getNames(_name);
        if(cursor.moveToFirst())
            name_id = cursor.getInt(0);
        else{
            writeRequests.AddName(_name);
            name_id = 1;
        }
    }

    private void DefinitionFieldId(){
        writeRequests.AddField(_type,name_id);
        cursor = readRequests.getFields_2(_name);
        cursor.moveToLast();
        field_id = cursor.getInt(0);
    }
}
