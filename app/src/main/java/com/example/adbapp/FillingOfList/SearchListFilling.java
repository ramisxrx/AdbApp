package com.example.adbapp.FillingOfList;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.adbapp.RecordList.Record;
import com.example.adbapp.Threads.HandlerThreadOfFilling;

public class SearchListFilling extends AssociativeListFilling{

    private String TAG = SearchListFilling.class.getCanonicalName();

    private String searchString;

    public SearchListFilling(Context context, HandlerThreadOfFilling workThread, NotifyViews_after notifyViews_after) {
        super(context, workThread, notifyViews_after);
        this.searchString = "";
    }

    @Override
    public void ActionOfInitialization(int record_id) {
        // здесь не как record_id, а как type
        int _type = record_id;
        workThread.bg_operations(new Runnable() {
            @Override
            public void run() {
                ClearRecords();
                cursorInit = readRequests.getRecords_7(searchString,_type);
                cur_level = 0;
                parentRecordByLevel.add(cur_level,new Record(0,"РЕЗУЛЬТАТ ФИЛЬТРАЦИИ:",0,0));
                objIdList.clear();
                while (cursorInit.moveToNext())
                    objIdList.add(cursorInit.getInt(5));

                FillingInitialList();

                workThread.ui_operations(new Runnable() {
                    @Override
                    public void run() {
                        notifyViews_after.ActionOfInitialization();
                    }
                });
            }
        });
    }

    public void setSearchString(String searchString){
        this.searchString = searchString;
    }

}
