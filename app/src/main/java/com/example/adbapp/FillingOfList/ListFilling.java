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

    protected int cur_level=0, selObjId=0;
    public int selPosRec=0;


    protected void ActionDown(){

        if(selObjId==0){
            cursor = databaseHelper.getRecords(objIdList.get(selPosRec));
            selObjId = objIdList.get(selPosRec);
        }

    }

    protected void ActionUp(){

    }

    protected void FillingOfListDown(int parent_id){

    }

}
