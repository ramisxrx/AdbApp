package com.example.adbapp.FillingOfList;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.adbapp.DatabaseHelper;
import com.example.adbapp.Record;

import java.util.ArrayList;



public class ListFilling extends RecordList{

    private String TAG = ListFilling.class.getCanonicalName();

    protected DatabaseHelper databaseHelper;
    protected Cursor cursor;

    protected ArrayList<Integer> objIdList = new ArrayList<>();
    protected ArrayList<Integer> parentIdByLevels = new ArrayList<>();

    protected int selObjId=0,selItemPos=0;
    protected boolean selDirection;
    public int cur_level=0;

    public ListFilling(Context context){
        this.cur_level=0;
        this.parentIdByLevels.add(cur_level,0);

        databaseHelper = new DatabaseHelper(context);
    }


    public void ActionDown(int position){

    }

    public void ActionUp(int position){

    }

    public int get_ParentIdOfCurrentLevel(){

        Log.d(TAG, "get_ParentIdOfCurrentLevel: ParentIdOfCurrentLevel="+String.valueOf(parentIdByLevels.get(cur_level)));

        return parentIdByLevels.get(cur_level);
    }

}
