package com.example.adbapp.FillingOfList;

import android.database.Cursor;
import android.util.Log;

import com.example.adbapp.DatabaseHelper;
import com.example.adbapp.Record;

import java.util.ArrayList;



public class ListFilling extends RecordList{

    protected DatabaseHelper databaseHelper;
    protected Cursor cursor;

    protected ArrayList<Integer> objIdList = new ArrayList<>();
    protected ArrayList<Integer> parentIdByLevels = new ArrayList<>();

    protected int selObjId=0;
    protected boolean selDirection;
    public int selPosRec=0,cur_level=0;

    public ListFilling(){
        this.cur_level=0;
        this.parentIdByLevels.add(cur_level,0);
    }

    public void ActionDown(){

        if(selObjId==0){
            cursor = databaseHelper.getRecords(objIdList.get(selPosRec));
            selObjId = objIdList.get(selPosRec);
            selDirection = false;
        }

        cur_level++;

        parentIdByLevels.add(cur_level,records.get(selPosRec).getRecord_id());
        FillingOfListDown(parentIdByLevels.get(cur_level));
    }

    public void ActionUp(){

    }

    public void FillingOfListDown(int parent_id){

        ClearRecords();

        for(int i=0;i<cursor.getCount();i++){
            cursor.moveToPosition(i);

            if(cursor.getInt(1)==parent_id)
                AddNewItemInRecords(cursor.getInt(0),cursor.getString(2),cursor.getInt(3),cursor.getInt(4));
        }

    }

    public void ToPreviousLevel(){

        if(cur_level>0){
            parentIdByLevels.remove(cur_level);
            cur_level--;


        }

    }

}
