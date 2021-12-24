package com.example.adbapp.FillingOfList;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.adbapp.RecordList.Record;
import com.example.adbapp.Threads.HandlerThreadOfFilling;

import java.util.ArrayList;

public class FoundListFilling extends ListFilling{

    private String TAG = FoundListFilling.class.getCanonicalName();

    private final NotifyViews_after notifyViews_after;
    private Cursor cursorRecordsByField;
    private ArrayList<Record> fields = new ArrayList<>();
    private ArrayList<Integer> objIdList = new ArrayList<>();
    private ArrayList<Integer> recordIdByLevels = new ArrayList<>();

    public interface NotifyViews_after{
        void ActionOfSearch();
        void ActionDown();
        void ActionUp();
        void ToPreviousLevel();
    }

    public int selFieldId=0;
    public boolean selDirection;

    public FoundListFilling(Context context, NotifyViews_after notifyViews_after) {
        super(context);
        this.notifyViews_after = notifyViews_after;
        cur_level = -1;
        workThread = new HandlerThreadOfFilling("FoundListFilling");
    }

    @Override
    public void ActionDown(int position) {
        workThread.bg_operations(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "ActionDown: bg_operations");
                if(cur_level==-1) {
                    cur_level++;
                    parentIdByLevels.add(cur_level,0);
                    CheckSelectionOfFieldId(position);
                    FillingZeroLevelToDown();
                    selDirection = false;
                }else {
                    cur_level++;
                    parentIdByLevels.add(cur_level,records.get(position).getRecord_id());
                    if(cur_level==1)
                        CheckSelectionOfObjId(position);
                    FillingOtherLevelToDown(parentIdByLevels.get(cur_level));
                }
                workThread.ui_operations(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "ActionDown: ui_operations");
                        notifyViews_after.ActionDown();
                    }
                });
            }
        });
    }

    @Override
    public void ActionUp(int position) {
        workThread.bg_operations(new Runnable() {
            @Override
            public void run() {
                if(cur_level==-1){
                    cur_level++;
                    recordIdByLevels.add(cur_level,0);
                    CheckSelectionOfFieldId(position);
                    FillingZeroLevelToUp();
                    selDirection = true;
                }else{
                    cur_level++;
                    recordIdByLevels.add(cur_level,records.get(position).getParent_id());
                    if(cur_level==1)
                        CheckSelectionOfObjId(position);
                    FillingOtherLevelToUp(recordIdByLevels.get(cur_level));
                }
                workThread.ui_operations(new Runnable() {
                    @Override
                    public void run() {
                        notifyViews_after.ActionUp();
                    }
                });
            }
        });
    }

    @Override
    public void ToPreviousLevel() {

        if(cur_level>-1){
            workThread.bg_operations(new Runnable() {
                @Override
                public void run() {
                    if(selDirection){
                        recordIdByLevels.remove(cur_level);
                        cur_level--;
                        if (cur_level == -1) {
                            CopyFieldsToRecords();
                        } else {
                            if (cur_level == 0)
                                FillingZeroLevelToUp();
                            else
                                FillingOtherLevelToUp(recordIdByLevels.get(cur_level));
                        }
                    }else {
                        parentIdByLevels.remove(cur_level);
                        cur_level--;
                        if (cur_level == -1) {
                            CopyFieldsToRecords();
                        } else {
                            if (cur_level == 0)
                                FillingZeroLevelToDown();
                            else
                                FillingOtherLevelToDown(parentIdByLevels.get(cur_level));
                        }
                    }
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

    public void ActionOfSearch(CharSequence charsequence){

        workThread.bg_operations(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "ActionOfSearch: bg_operations:charsequence="+charsequence);
                FillingFoundList(charsequence);

                workThread.ui_operations(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "ActionOfSearch: ui_operations");
                        notifyViews_after.ActionOfSearch();
                    }
                });
            }
        });
    }




    private void FillingFoundList(CharSequence charsequence){
        Cursor cursorSearch;
        fields.clear();
        cur_level=-1;
        parentIdByLevels.clear();

        if(charsequence.length()>0) {
            Log.d(TAG, "FillingFoundList: charsequence.length()="+String.valueOf(charsequence.length()));
            cursorSearch = readRequests.getFields(charsequence.toString());

            while (cursorSearch.moveToNext()) {
                Log.d(TAG, "FillingFoundList: cursorSearch.moveToNext()");
                fields.add(new Record(cursorSearch.getInt(0), cursorSearch.getString(1), 0, 0));
            }    

            cursorSearch.close();
        }
        CopyFieldsToRecords();
    }

    private void CopyFieldsToRecords(){
        ClearRecords();
        for(int i=0;i<fields.size();i++)
            records.add(i,fields.get(i));
    }

    private void FillingZeroLevelToDown(){
        Cursor cursorZeroLevel = null;
        int k=0;
        ClearRecords();
        objIdList.clear();

        for (int i = 0; i < cursorRecordsByField.getCount(); i++) {
            cursorRecordsByField.moveToPosition(i);
            cursorZeroLevel = readRequests.getRecords_2(cursorRecordsByField.getInt(0));
            while (cursorZeroLevel.moveToNext()) {
                AddNewItemInRecords(cursorZeroLevel.getInt(0), cursorZeroLevel.getString(2), cursorZeroLevel.getInt(3), cursorZeroLevel.getInt(4));
                records.get(k).setParent_id(cursorZeroLevel.getInt(1));
                k++;
                objIdList.add(cursorZeroLevel.getInt(5));
            }
        }
        if (cursorZeroLevel != null) {
            cursorZeroLevel.close();
        }
    }

    private void FillingZeroLevelToUp(){
        Cursor cursorZeroLevel = null;
        int k=0;
        ClearRecords();
        objIdList.clear();

        for(int i=0;i<cursorRecordsByField.getCount();i++){
            cursorRecordsByField.moveToPosition(i);
            cursorZeroLevel = readRequests.getRecords_4(cursorRecordsByField.getInt(1));
            while (cursorZeroLevel.moveToNext()){
                AddNewItemInRecords(cursorZeroLevel.getInt(0),cursorZeroLevel.getString(2),cursorZeroLevel.getInt(3),cursorZeroLevel.getInt(4));
                records.get(k).setParent_id(cursorZeroLevel.getInt(1));
                k++;
                objIdList.add(cursorZeroLevel.getInt(5));
            }
        }
        if (cursorZeroLevel != null) {
            cursorZeroLevel.close();
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

    private void CheckSelectionOfFieldId(int position){
        if(selFieldId==0 || selFieldId!=records.get(position).getRecord_id()){
            selFieldId=records.get(position).getRecord_id();
            cursorRecordsByField = readRequests.getRecords_3(selFieldId);
        }
    }

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
        if(cursorRecordsByField!=null)
            cursorRecordsByField.close();
        readRequests.Destroy();
        workThread.quit();
    }
}
