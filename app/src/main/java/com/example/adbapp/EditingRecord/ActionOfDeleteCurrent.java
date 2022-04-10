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

                DeleteRecord(record.getRecord_id());

                /*
                cursor = readRequests.getFieldId(record.getRecord_id());
                cursor.moveToFirst();
                int fieldId = cursor.getInt(0);

                writeRequests.DeleteRecord(record.getRecord_id());

                cursor = readRequests.getRecords_8(fieldId);
                if(!cursor.moveToFirst())
                    DeleteField(fieldId);

                 */

                workThread.ui_operations(new Runnable() {
                    @Override
                    public void run() {
                        uiActionAtDeleteCurrent.afterActionOfDeleteCurrent();
                    }
                });
            }
        });
    }

    private void DeleteRecord(int recordId){
        Cursor cursor = readRequests.getFieldId(recordId);
        cursor.moveToFirst();
        int fieldId = cursor.getInt(0);

        writeRequests.DeleteRecord(recordId);

        cursor = readRequests.getRecords_8(fieldId);
        if(!cursor.moveToFirst())
            DeleteField(fieldId);
    }

    private void DeleteField(int fieldId){
        Cursor cursor = readRequests.getName_id_2(fieldId);
        cursor.moveToFirst();
        writeRequests.DeleteField(fieldId);
        writeRequests.DeleteName(cursor.getInt(0));
    }

}
