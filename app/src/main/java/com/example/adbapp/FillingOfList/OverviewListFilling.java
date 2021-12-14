package com.example.adbapp.FillingOfList;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.adbapp.DatabaseHelper;

import java.util.ArrayList;


public class OverviewListFilling extends ListFilling{

    private String TAG = OverviewListFilling.class.getCanonicalName();

    protected Cursor cursorInit;

    protected boolean cmd_cursorInit;

    public OverviewListFilling(Context context){
        super(context);

        cmd_cursorInit = true;
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

        Log.d(TAG, "FillingOfListDown: run");

        ClearRecords();

        for(int i=0;i<cursor.getCount();i++){
            cursor.moveToPosition(i);

            if(cursor.getInt(1)==parent_id) {
                AddNewItemInRecords(cursor.getInt(0), cursor.getString(2), cursor.getInt(3), cursor.getInt(4));

                Log.d(TAG, "FillingOfListDown: Record_id:"+cursor.getString(0)+" Parent_id:"+cursor.getString(1));
            }
        }

    }

    public void FillingInitialList(){

        Log.d(TAG, "FillingInitialList: run");

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
