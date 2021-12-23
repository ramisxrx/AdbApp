package com.example.adbapp.FillingOfList;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.adbapp.DataBase.DataBaseHelper;
import com.example.adbapp.DataBase.ReadRequests;
import com.example.adbapp.Threads.HandlerThreadOfFilling;

import java.util.ArrayList;



public class ListFilling extends RecordList{

    private String TAG = ListFilling.class.getCanonicalName();

    protected ReadRequests readRequests;
    protected Cursor cursor;

    protected ArrayList<Integer> objIdList = new ArrayList<>();
    protected ArrayList<Integer> parentIdByLevels = new ArrayList<>();

    protected HandlerThreadOfFilling workThread;

    protected int selObjId=0,selItemPos=0;
    public int cur_level=0;

    public ListFilling(Context context){
        readRequests = new ReadRequests(context);
    }


    public void ActionDown(int position){

    }

    public void ActionUp(int position){

    }

    public void ToPreviousLevel(){

    }

    public int get_ParentIdOfCurrentLevel(){

        Log.d(TAG, "get_ParentIdOfCurrentLevel: ParentIdOfCurrentLevel="+String.valueOf(parentIdByLevels.get(cur_level)));

        return parentIdByLevels.get(cur_level);
    }

}
