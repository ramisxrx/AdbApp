package com.example.adbapp.FillingOfList;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.adbapp.DataBase.ReadRequests;
import com.example.adbapp.DataBase.WriteRequests;
import com.example.adbapp.Threads.HandlerThreadOfFilling;

public class AddingNewRecord {

    private String TAG = AddingNewRecord.class.getCanonicalName();

    public interface NotifyViews_before{
        void Save();
    }
    public interface NotifyViews_after{
        void Save();
    }

    private final NotifyViews_before notifyViews_before;
    private final NotifyViews_after notifyViews_after;
    private WriteRequests writeRequests;
    private ReadRequests readRequests;
    private int parent_id, object_id, _time, name_id, _type;
    private Cursor cursor;
    private HandlerThreadOfFilling workThread;

    public String parentName,_name;
    public int parentType,field_id;
    public boolean successAddingNewRecord;

    public AddingNewRecord(Context context, int parent_id, int _type, NotifyViews_before notifyViews_before,NotifyViews_after notifyViews_after){
        this.parent_id=parent_id;
        this._type=_type;
        this.notifyViews_before = notifyViews_before;
        this.notifyViews_after = notifyViews_after;
        readRequests = new ReadRequests(context);
        writeRequests = new WriteRequests(context);
        successAddingNewRecord = false;
        DefinitionsInit();

        workThread = new HandlerThreadOfFilling("AddingNewRecord");
    }

    public void Save(){

        workThread.bg_operations(new Runnable() {
            @Override
            public void run() {
                workThread.ui_operations(new Runnable() {
                    @Override
                    public void run() {
                        notifyViews_before.Save();
                    }
                });

                if(parent_id==0)
                    AddNewObject();

                if(field_id>0) {
                    Log.d(TAG, "Save: writeRequests.AddRecord");
                    writeRequests.AddRecord(object_id, parent_id, field_id, _time);
                    successAddingNewRecord = true;
                }
                else{
                    Log.d(TAG, "Save: _name.length()="+String.valueOf(_name.length())+" _name="+_name);

                    if(_name.length()>0){
                        DefinitionNameId();
                        DefinitionFieldId();
                        writeRequests.AddRecord(object_id,parent_id,field_id,_time);
                        successAddingNewRecord = true;
                    }else
                        successAddingNewRecord = false;
                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                workThread.ui_operations(new Runnable() {
                    @Override
                    public void run() {
                        notifyViews_after.Save();
                    }
                });
            }
        });
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
            cursor = readRequests.getNames(_name);
            if(cursor.moveToFirst())
                name_id = cursor.getInt(0);
        }
    }

    private void DefinitionFieldId(){
        writeRequests.AddField(_type,name_id);
        cursor = readRequests.getFields_2(name_id);
        if(cursor.moveToLast())
            field_id = cursor.getInt(0);
    }

    public void Destroy(){
        if(!cursor.isClosed())
            cursor.close();
        readRequests.Destroy();
        writeRequests.Destroy();
        workThread.quit();
    }
}
