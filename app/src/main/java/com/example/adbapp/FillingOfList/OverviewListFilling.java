package com.example.adbapp.FillingOfList;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.adbapp.RecordList.Record;
import com.example.adbapp.Threads.HandlerThreadOfFilling;

import java.util.ArrayList;

public class OverviewListFilling extends ListFilling{

    private String TAG = OverviewListFilling.class.getCanonicalName();

    public interface NotifyViews_after{
        void ActionDown();
        void ToPreviousLevel();
        void UpdateAfterAddNewRecords();
    }

    public ArrayList<Record> parentRecordsByLevel = new ArrayList<>();

    private final NotifyViews_after notifyViews_after;
    private Cursor cursorInit, cursorTEST;
    private boolean cmd_cursorInit;

    public OverviewListFilling(Context context, NotifyViews_after notifyViews_after){
        super(context);
        this.notifyViews_after=notifyViews_after;
        parentIdByLevels.add(cur_level,0);

        workThread = new HandlerThreadOfFilling("OverviewListFilling");

        cur_level = 0;
        parentRecordsByLevel.add(cur_level,new Record(0,"БАЗОВЫЙ УРОВЕНЬ",0,0));
        cmd_cursorInit = true;

        workThread.bg_operations(new Runnable() {
            @Override
            public void run() {
                FillingInitialList();
            }
        });
    }

    @Override
    public void ActionDown(int position){

        workThread.bg_operations(new Runnable() {
            @Override
            public void run() {
                if(cur_level==0)
                    CheckSelectionOfObjId(position);

                cur_level++;
                parentIdByLevels.add(cur_level,records.get(position).getRecord_id());
                parentRecordsByLevel.add(cur_level, records.get(position));
                Log.d(TAG, "ActionDown: cur_level:"+String.valueOf(cur_level)+" parentIdByLevels:"+String.valueOf(records.get(position).getRecord_id()));

                FillingOtherLevelToDown(parentIdByLevels.get(cur_level));

                workThread.ui_operations(new Runnable() {
                    @Override
                    public void run() {
                        notifyViews_after.ActionDown();
                    }
                });
            }
        });
    }

    @Override
    public void ToPreviousLevel(){

        Log.d(TAG, "ToPreviousLevel: cur_level="+String.valueOf(cur_level));

        if(cur_level>0){
            workThread.bg_operations(new Runnable() {
                @Override
                public void run() {
                    parentIdByLevels.remove(cur_level);
                    parentRecordsByLevel.remove(cur_level);
                    cur_level--;

                    if(cur_level==0)
                        FillingInitialList();
                    else
                        FillingOtherLevelToDown(parentIdByLevels.get(cur_level));

                    workThread.ui_operations(new Runnable() {
                        @Override
                        public void run() {
                            notifyViews_after.ToPreviousLevel();
                        }
                    });
                }
            });
        }
    }

    public void UpdateAfterAddNewRecords(){

        workThread.bg_operations(new Runnable() {
            @Override
            public void run() {

                Log.d(TAG, "UpdateAfterAddNewRecords: parentIdByLevels="+String.valueOf(parentIdByLevels.get(cur_level)));

                if(cur_level==0){
                    cmd_cursorInit=true;
                    FillingInitialList();
                }else {
                    cursor = readRequests.getRecords(selObjId);
                    FillingOtherLevelToDown(parentIdByLevels.get(cur_level));
                }

                workThread.ui_operations(new Runnable() {
                    @Override
                    public void run() {
                        notifyViews_after.UpdateAfterAddNewRecords();
                    }
                });
            }
        });
    }



    private void FillingOtherLevelToDown(int parent_id){
        Log.d(TAG, "FillingOfListDown: BG_Operations: Current thread="+Thread.currentThread());

        ClearRecords();

        for(int i=0;i<cursor.getCount();i++){
            cursor.moveToPosition(i);

            if(cursor.getInt(1)==parent_id) {
                AddNewItemInRecords(cursor.getInt(0), cursor.getString(2), cursor.getInt(3), cursor.getInt(4));

                Log.d(TAG, "FillingOfListDown: Record_id:"+cursor.getString(0)+" Parent_id:"+cursor.getString(1));
            }
        }
    };

    private void FillingInitialList(){

        Log.d(TAG, "FillingInitialList: BG_Operations: Current thread=" + Thread.currentThread());

        ClearRecords();
        objIdList.clear();

        if(cmd_cursorInit) {
            cursorInit = readRequests.getRecords_2(0);
            cmd_cursorInit = false;
            Log.d(TAG, "FillingInitialList: cmd_cursorInit");
        }

        for (int i=0;i<cursorInit.getCount();i++){
            cursorInit.moveToPosition(i);

            AddNewItemInRecords(cursorInit.getInt(0),cursorInit.getString(2),cursorInit.getInt(3),cursorInit.getInt(4));
            objIdList.add(cursorInit.getInt(5));

            Log.d(TAG, "FillingInitialList: Record_id:"+cursorInit.getString(0)+" Parent_id:"+cursorInit.getString(1));
        }
    };

    private void CheckSelectionOfObjId(int position){
        if(selObjId==0 || selObjId!=objIdList.get(position)){
            selObjId = objIdList.get(position);
            cursor = readRequests.getRecords(selObjId);
        }
    }

    public void Destroy(){
        records.clear();
        if(cursor!=null)
            cursor.close();
        if(cursorInit!=null)
            cursorInit.close();
        readRequests.Destroy();
        workThread.quit();
    }

    public void bdView(){
        cursorTEST = readRequests.getRecordsTEST();
        while (cursorTEST.moveToNext()){
            Log.d(TAG, "     record_id:"+cursorTEST.getString(0)+
                            " object_id:"+cursorTEST.getString(1)+
                            " parent_id:"+cursorTEST.getString(2)+
                            " name:"+cursorTEST.getString(3)+
                            " name_id:"+cursorTEST.getString(4)+
                            " field_id:"+cursorTEST.getString(5));
        }
    }
}
