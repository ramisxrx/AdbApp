package com.example.adbapp.EditingRecord;

import android.database.Cursor;
import android.widget.RadioGroup;

import com.example.adbapp.DataBase.ReadRequests;
import com.example.adbapp.DataBase.WriteRequests;
import com.example.adbapp.RecordList.Record;
import com.example.adbapp.Threads.HandlerThreadOfFilling;

public class ActionOfDeleteCurrent implements ActionOnClickSave{

    private UIActionAtDeleteCurrent uiActionAtDeleteCurrent;

    private final Record record;
    private final HandlerThreadOfFilling workThread;
    private final WriteRequests writeRequests;
    private final ReadRequests readRequests;

    public ActionOfDeleteCurrent(Record record,
                                 HandlerThreadOfFilling workThread,
                                 WriteRequests writeRequests,
                                 ReadRequests readRequests,
                                 UIActionAtDeleteCurrent uiActionAtDeleteCurrent) {
        this.record = record;
        this.workThread = workThread;
        this.writeRequests = writeRequests;
        this.readRequests = readRequests;
        this.uiActionAtDeleteCurrent = uiActionAtDeleteCurrent;
    }

    @Override
    public void onClick() {
        workThread.bg_operations(new Runnable() {
            @Override
            public void run() {
                workThread.ui_operations(new Runnable() {
                    @Override
                    public void run() {
                        uiActionAtDeleteCurrent.beforeActionOfDeleteCurrent();
                    }
                });

                Cursor cursor = readRequests.getRecord_id(record.getRecord_id());

                while(cursor.moveToNext()){
                    writeRequests.UpdateParent_id(cursor.getInt(0),record.getParent_id());
                }
                writeRequests.DeleteRecord(record.getRecord_id());

                workThread.ui_operations(new Runnable() {
                    @Override
                    public void run() {
                        uiActionAtDeleteCurrent.afterActionOfDeleteCurrent();
                    }
                });
            }
        });


    }
}
