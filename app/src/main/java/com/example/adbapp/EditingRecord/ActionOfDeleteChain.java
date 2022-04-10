package com.example.adbapp.EditingRecord;

import android.database.Cursor;

import com.example.adbapp.DataBase.ReadRequests;
import com.example.adbapp.DataBase.WriteRequests;
import com.example.adbapp.RecordList.Record;
import com.example.adbapp.Threads.HandlerThreadOfFilling;

import java.util.ArrayList;

public class ActionOfDeleteChain implements ActionOnClickSave{

    private UIActionAtDeleteChain uiActionAtDeleteChain;

    private final Record record;
    private final HandlerThreadOfFilling workThread;
    private final WriteRequests writeRequests;
    private final ReadRequests readRequests;

    public ActionOfDeleteChain(Record record,
                               HandlerThreadOfFilling workThread,
                               WriteRequests writeRequests,
                               ReadRequests readRequests,
                               UIActionAtDeleteChain uiActionAtDeleteChain) {
        this.uiActionAtDeleteChain = uiActionAtDeleteChain;
        this.record = record;
        this.workThread = workThread;
        this.writeRequests = writeRequests;
        this.readRequests = readRequests;
    }

    @Override
    public void onClick() {
        workThread.bg_operations(new Runnable() {
            @Override
            public void run() {
                workThread.ui_operations(new Runnable() {
                    @Override
                    public void run() {
                        uiActionAtDeleteChain.beforeActionOfDeleteChain();
                    }
                });

                ArrayList<Integer> list_2 = new ArrayList<>();

                //writeRequests.DeleteRecord(record.getRecord_id());
                DeleteRecord(record.getRecord_id());

                Cursor cursor = readRequests.getRecord_id(record.getParent_id());

                //while (cursor.moveToNext())
                //    list_2.add(cursor.getInt(0));
                list_2.add(record.getRecord_id());

                while (!list_2.isEmpty()){
                    ArrayList<Integer> list_1 = (ArrayList<Integer>) list_2.clone();
                    list_2.clear();
                    for (int recordId:list_1) {
                        cursor = readRequests.getRecord_id(recordId);
                        while (cursor.moveToNext()) {
                            list_2.add(cursor.getInt(0));

                            //writeRequests.DeleteRecord(cursor.getInt(0));
                            DeleteRecord(cursor.getInt(0));

                        }
                    }
                }


                workThread.ui_operations(new Runnable() {
                    @Override
                    public void run() {
                        uiActionAtDeleteChain.afterActionOfDeleteChain();
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
