package com.example.adbapp.EditingRecord;

import android.database.Cursor;

import com.example.adbapp.DataBase.ReadRequests;
import com.example.adbapp.DataBase.WriteRequests;
import com.example.adbapp.Threads.HandlerThreadOfFilling;

public class ActionOfUpdate implements ActionOnClickSave{

    private UIActionAtUpdate uiActionAtUpdate;

    private final HandlerThreadOfFilling workThread;
    private final WriteRequests writeRequests;
    private final ReadRequests readRequests;
    private final int record_id;
    private final String change;

    public ActionOfUpdate(int record_id,
                          String change,
                          HandlerThreadOfFilling workThread,
                          ReadRequests readRequests,
                          WriteRequests writeRequests,
                          UIActionAtUpdate uiActionAtUpdate){
        this.record_id = record_id;
        this.change = change;
        this.workThread = workThread;
        this.readRequests = readRequests;
        this.writeRequests = writeRequests;
        this.uiActionAtUpdate = uiActionAtUpdate;
    }

    @Override
    public void onClick() {
        workThread.bg_operations(new Runnable() {
            @Override
            public void run() {
                workThread.ui_operations(new Runnable() {
                    @Override
                    public void run() {
                        uiActionAtUpdate.beforeActionOfUpdate();
                    }
                });

                Cursor cursor = readRequests.getName_id(record_id);
                if(cursor.moveToFirst()){
                    writeRequests.UpdateName(cursor.getInt(0),change);
                    writeRequests.SetCurrentTime(record_id);
                }

                workThread.ui_operations(new Runnable() {
                    @Override
                    public void run() {
                        uiActionAtUpdate.afterActionOfUpdate();
                    }
                });
            }
        });

    }
}
