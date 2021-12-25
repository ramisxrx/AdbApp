package com.example.adbapp.FillingOfList;

import android.content.Context;
import android.database.Cursor;

import com.example.adbapp.Threads.HandlerThreadOfFilling;

import java.util.ArrayList;

public class AssociativeListFilling extends ListFilling{

    public interface NotifyViews_after{
        void ActionOfInitialization();
        void ActionDown();
        void ActionUp();
        void ToPreviousLevel();
    }

    private final NotifyViews_after notifyViews_after;
    private Cursor cursorInit;
    private int field_id;
    private ArrayList<Integer> recordIdByLevels = new ArrayList<>();

    public boolean selDirection;

    public AssociativeListFilling(Context context, NotifyViews_after notifyViews_after) {
        super(context);
        this.notifyViews_after = notifyViews_after;
        parentIdByLevels.add(0,0);
        recordIdByLevels.add(0,0);

        workThread = new HandlerThreadOfFilling("ActionOfInitialization");
    }

    public void ActionOfInitialization(int field_id){

        this.field_id = field_id;
        cur_level = 0;
        objIdList.clear();
        cursorInit = readRequests.getRecords_5(field_id);
        while (cursorInit.moveToNext())
            objIdList.add(cursorInit.getInt(5));

        FillingInitialList();
    }

    @Override
    public void ActionDown(int position) {
        if(cur_level==0)
            CheckSelectionOfObjId(position);
        cur_level++;
        parentIdByLevels.add(cur_level,records.get(position).getRecord_id());
        FillingOtherLevelToDown(parentIdByLevels.get(cur_level));
    }

    @Override
    public void ActionUp(int position) {
        if(cur_level==0)
            CheckSelectionOfObjId(position);
        cur_level++;
        recordIdByLevels.add(cur_level,records.get(position).getRecord_id());
        FillingOtherLevelToUp(recordIdByLevels.get(cur_level));
    }

    @Override
    public void ToPreviousLevel() {
        if(cur_level==0){

        }else{

        }
    }

    private void FillingInitialList(){
        ClearRecords();
        for (int i = 0; i < cursorInit.getCount(); i++) {
            cursorInit.moveToPosition(i);
            AddNewItemInRecords(cursorInit.getInt(0), cursorInit.getString(2), cursorInit.getInt(3), cursorInit.getInt(4));
        }
    }

    private void FillingOtherLevelToDown(int parent_id){
        ClearRecords();
        int k=0;
        for (int i=0;i<cursor.getCount();i++){
            cursor.moveToPosition(i);
            if(cursor.getInt(1)==parent_id){
                AddNewItemInRecords(cursor.getInt(0),cursor.getString(2),cursor.getInt(3),cursor.getInt(4));
                records.get(k).setParent_id(cursor.getInt(1));
                k++;
            }
        }
    }

    private void FillingOtherLevelToUp(int record_id){
        ClearRecords();
        int k=0;
        for (int i=0;i<cursor.getCount();i++){
            cursor.moveToPosition(i);
            if(cursor.getInt(0)==record_id){
                AddNewItemInRecords(cursor.getInt(0),cursor.getString(2),cursor.getInt(3),cursor.getInt(4));
                records.get(k).setParent_id(cursor.getInt(1));
                k++;
            }
        }
    }

    private void CheckSelectionOfObjId(int position){
        if(selObjId==0 || selObjId!=objIdList.get(position)){
            selObjId = objIdList.get(position);
            cursor = readRequests.getRecords(selObjId);
        }
    }
}
