package com.example.adbapp.FillingOfList;

import android.content.Context;
import android.database.Cursor;

import com.example.adbapp.Record;
import com.example.adbapp.Threads.HandlerThreadOfFilling;

import java.util.ArrayList;

public class FoundListFilling extends ListFilling{


    public interface NotifyViews_after{
        void ActionOfSearch();
    }

    private final NotifyViews_after notifyViews_after;
    private Cursor cursorRecordsByField;
    private ArrayList<Record> fields = new ArrayList<>();
    private ArrayList<Integer> objIdList = new ArrayList<>();

    private int selFieldId;

    public FoundListFilling(Context context, NotifyViews_after notifyViews_after) {
        super(context);
        this.notifyViews_after = notifyViews_after;
        cur_level = -1;
        workThread = new HandlerThreadOfFilling("FoundListFilling");
    }

    @Override
    public void ActionDown(int position) {

        if(cur_level==-1) {
            cur_level++;
            parentIdByLevels.add(cur_level,0);
            if(selFieldId==0 || selFieldId!=records.get(position).getRecord_id()){
                selFieldId=records.get(position).getRecord_id();
                cursorRecordsByField = databaseHelper.getRecords_3(selFieldId);
            }
            FillingZeroLevelToDown();
        }else {
            cur_level++;
            parentIdByLevels.add(cur_level,records.get(position).getRecord_id());
            if(selObjId==0){
                selObjId = objIdList.get(position);
                cursor = databaseHelper.getRecords(selObjId);
            }
            FillingOtherLevelToDown(cursor.getInt(0));
        }
    }

    @Override
    public void ActionUp(int position) {

    }

    @Override
    public void ToPreviousLevel() {

        if(cur_level>-1){
            parentIdByLevels.remove(cur_level);
            cur_level--;
            if(cur_level==-1){
                CopyFieldsToRecords();
            }else{
                if(cur_level==0)
                    FillingZeroLevelToDown();
                else
                    FillingOtherLevelToDown(parentIdByLevels.get(cur_level));
            }
        }
    }

    public void ActionOfSearch(CharSequence charsequence){
        workThread.bg_operations(new Runnable() {
            @Override
            public void run() {
                FillingFoundList(charsequence);
            }
        });

        workThread.ui_operations(new Runnable() {
            @Override
            public void run() {
                notifyViews_after.ActionOfSearch();
            }
        });
    }


    private void FillingFoundList(CharSequence charsequence){
        Cursor cursorSearch;
        fields.clear();

        if(charsequence.length()>0) {
            cursorSearch = databaseHelper.getFields(charsequence.toString());

            while (cursorSearch.moveToNext())
                fields.add(new Record(cursorSearch.getInt(0),cursorSearch.getString(1),0,0));

            CopyFieldsToRecords();
            cursorSearch.close();
        }
    }

    private void CopyFieldsToRecords(){
        ClearRecords();
        for(int i=0;i<fields.size();i++)
            records.add(i,fields.get(i));
    }

    private void FillingZeroLevelToDown(){
        int k=0;
        ClearRecords();
        objIdList.clear();
        selObjId=0;

        for(int i=0;i<cursorRecordsByField.getCount();i++){
            cursorRecordsByField.moveToPosition(i);
            cursor = databaseHelper.getRecords_2(cursorRecordsByField.getInt(0));
            while (cursor.moveToNext()){
                AddNewItemInRecords(cursor.getInt(0),cursor.getString(2),cursor.getInt(3),cursor.getInt(4));
                records.get(k).setParent_id(cursor.getInt(1));
                k++;
                objIdList.add(cursor.getInt(5));
            }
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

}
