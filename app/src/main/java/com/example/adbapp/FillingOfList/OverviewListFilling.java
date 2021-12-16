package com.example.adbapp.FillingOfList;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.MainThread;

import com.example.adbapp.DatabaseHelper;
import com.example.adbapp.RecordAdapter;
import com.example.adbapp.Threads.HandlerThreadOfFilling;
import com.example.adbapp.Threads.ThreadOfFilling;

import java.util.ArrayList;


public class OverviewListFilling extends ListFilling{

    private String TAG = OverviewListFilling.class.getCanonicalName();

    public HandlerThreadOfFilling workThread;

    public RecordAdapter recordAdapter;

    protected Cursor cursorInit;

    protected boolean cmd_cursorInit;

    public OverviewListFilling(Context context, RecordAdapter recordAdapter){
        super(context);
        this.recordAdapter = recordAdapter;

        workThread = new HandlerThreadOfFilling();

        cmd_cursorInit = true;

        //workThread.start();
        /*
        workThread.handler.post(new Runnable() {
            @Override
            public void run() {
                FillingInitialList();
            }
        });
        */
         /*


        Handler handler = new Handler(workThread.looperOfThread);

        handler.post(new Runnable() {
            @Override
            public void run() {
                FillingInitialList();
            }
        });
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                FillingInitialList();
            }
        }).start();
        */
        FillingInitialList();
    }

    @Override
    public void ActionDown(int position){

        if(selObjId==0){
            cursor = databaseHelper.getRecords(objIdList.get(position));
            selObjId = objIdList.get(position);
            selDirection = false;
        }

        cur_level++;

        parentIdByLevels.add(cur_level,records.get(position).getRecord_id());

        Log.d(TAG, "ActionDown: cur_level:"+String.valueOf(cur_level)+" parentIdByLevels:"+String.valueOf(records.get(position).getRecord_id()));

        FillingOfListDown(parentIdByLevels.get(cur_level));
    }

    public void ActionUp(){

    }

    public void FillingOfListDown(int parent_id){

        workThread.execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "FillingOfListDown: BG_Operations: Current thread="+Thread.currentThread());

                ClearRecords();

                for(int i=0;i<cursor.getCount();i++){
                    cursor.moveToPosition(i);

                    if(cursor.getInt(1)==parent_id) {
                        AddNewItemInRecords(cursor.getInt(0), cursor.getString(2), cursor.getInt(3), cursor.getInt(4));

                        Log.d(TAG, "FillingOfListDown: Record_id:"+cursor.getString(0)+" Parent_id:"+cursor.getString(1));
                    }
                }
            }
        });

        workThread.show(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "FillingOfListDown: UI_Operations: Current thread="+Thread.currentThread());

                recordAdapter.notifyDataSetChanged();
            }
        });

        };



    public void FillingInitialList(){

        workThread.execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "FillingInitialList: BG_Operations: Current thread=" + Thread.currentThread());

                ClearRecords();
                objIdList.clear();

                if(cmd_cursorInit) {
                    cursorInit = databaseHelper.getRecords_2(0);
                    cmd_cursorInit = false;
                    Log.d(TAG, "FillingInitialList: cmd_cursorInit");
                }

                for (int i=0;i<cursorInit.getCount();i++){
                    cursorInit.moveToPosition(i);

                    AddNewItemInRecords(cursorInit.getInt(0),cursorInit.getString(2),cursorInit.getInt(3),cursorInit.getInt(4));
                    objIdList.add(cursorInit.getInt(5));

                    Log.d(TAG, "FillingInitialList: Record_id:"+cursorInit.getString(0)+" Parent_id:"+cursorInit.getString(1));
                }
            }
        });

        workThread.show(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "FillingInitialList: UI_Operations: Current thread="+Thread.currentThread());

                recordAdapter.notifyDataSetChanged();
            }
        });

        };


    public void UpdateAfterAddNewRecords(){

        cursorInit = databaseHelper.getRecords_2(parentIdByLevels.get(cur_level));

        cursorInit.moveToLast();

        if(cur_level==0){
            cmd_cursorInit=false;
            objIdList.add(cursorInit.getInt(5));
        }else
            cmd_cursorInit=true;

        AddNewItemInRecords(cursorInit.getInt(0),cursorInit.getString(2),cursorInit.getInt(3),cursorInit.getInt(4));

    }

    public void ToPreviousLevel(){

        Log.d(TAG, "ToPreviousLevel: cur_level="+String.valueOf(cur_level));

        if(cur_level>0){
            parentIdByLevels.remove(cur_level);
            cur_level--;

            if(cur_level==0)
                FillingInitialList();
            else
                FillingOfListDown(parentIdByLevels.get(cur_level));

        }

    }

}
