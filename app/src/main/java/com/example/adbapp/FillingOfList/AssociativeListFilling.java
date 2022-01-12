package com.example.adbapp.FillingOfList;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.adbapp.RecordList.Record;
import com.example.adbapp.Threads.HandlerThreadOfFilling;

import java.util.ArrayList;

public class AssociativeListFilling extends ListFilling{

    public ArrayList<Record> parentRecordByLevel = new ArrayList<>();

    public interface NotifyViews_after{
        void ActionOfInitialization();
        void ActionDown();
        void ActionUp();
        void ToPreviousLevel();
    }

    private String TAG = AssociativeListFilling.class.getCanonicalName();

    private HandlerThreadOfFilling workThread;
    private final NotifyViews_after notifyViews_after;
    private Cursor cursorInit;
    private int field_id;
    private ArrayList<Integer> recordIdByLevels = new ArrayList<>();

    public boolean selDirection,hasAssociations;

    public AssociativeListFilling(Context context,HandlerThreadOfFilling workThread, NotifyViews_after notifyViews_after) {
        super(context);
        this.workThread = workThread;
        this.notifyViews_after = notifyViews_after;
        parentIdByLevels.add(0,0);
        recordIdByLevels.add(0,0);
    }

    public void ActionOfInitialization(int record_id){
        workThread.bg_operations(new Runnable() {
            @Override
            public void run() {
                cursorInit = readRequests.getFieldId(record_id);
                cursorInit.moveToFirst();
                field_id = cursorInit.getInt(0);
                Log.d(TAG, "ActionOfInitialization: field_id="+String.valueOf(field_id));
                cursorInit = readRequests.getRecords_5(field_id);
                Log.d(TAG, "ActionOfInitialization: cursorInit.getCount()="+String.valueOf(cursorInit.getCount()));
                hasAssociations = cursorInit.getCount()>1;
                if(hasAssociations) {
                    cur_level = 0;
                    parentRecordByLevel.clear();
                    parentRecordByLevel.add(cur_level,new Record(0,"АСОЦИАЦИИ:",0,0));
                    objIdList.clear();
                    while (cursorInit.moveToNext())
                        objIdList.add(cursorInit.getInt(5));

                    FillingInitialList();
                }

                workThread.ui_operations(new Runnable() {
                    @Override
                    public void run() {
                        notifyViews_after.ActionOfInitialization();
                    }
                });
            }
        });
    }

    @Override
    public void ActionDown(int position) {
        workThread.bg_operations(new Runnable() {
            @Override
            public void run() {
                if(cur_level==0) {
                    selDirection = false;
                    CheckSelectionOfObjId(position);
                }
                cur_level++;
                parentRecordByLevel.add(cur_level, records.get(position));
                parentIdByLevels.add(cur_level,records.get(position).getRecord_id());
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
    public void ActionUp(int position) {
        workThread.bg_operations(new Runnable() {
            @Override
            public void run() {
                if(cur_level==0) {
                    selDirection = true;
                    CheckSelectionOfObjId(position);
                }
                cur_level++;
                recordIdByLevels.add(cur_level,records.get(position).getParent_id());
                FillingOtherLevelToUp(recordIdByLevels.get(cur_level));
                addParentRecordByLevel();

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
        if(cur_level>0) {
            workThread.bg_operations(new Runnable() {
                @Override
                public void run() {
                    parentRecordByLevel.remove(cur_level);
                    if (selDirection) {
                        recordIdByLevels.remove(cur_level);
                        cur_level--;
                        if (cur_level == 0)
                            FillingInitialList();
                        else
                            FillingOtherLevelToUp(recordIdByLevels.get(cur_level));
                    } else {
                        parentIdByLevels.remove(cur_level);
                        cur_level--;
                        if (cur_level == 0)
                            FillingInitialList();
                        else
                            FillingOtherLevelToDown(parentIdByLevels.get(cur_level));
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

    private void FillingInitialList(){
        ClearRecords();
        int k=0;
        for (int i = 0; i < cursorInit.getCount(); i++) {
            cursorInit.moveToPosition(i);
            AddNewItemInRecords(cursorInit);
        }
    }

    private void FillingOtherLevelToDown(int parent_id){
        ClearRecords();
        for (int i=0;i<cursor.getCount();i++){
            cursor.moveToPosition(i);
            if(cursor.getInt(1)==parent_id)
                AddNewItemInRecords(cursor);

        }
    }

    private void FillingOtherLevelToUp(int record_id){
        ClearRecords();
        for (int i=0;i<cursor.getCount();i++){
            cursor.moveToPosition(i);
            if(cursor.getInt(0)==record_id)
                AddNewItemInRecords(cursor);

        }
    }

    private void addParentRecordByLevel(){
        if(records.size()>0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                if (cursor.getInt(0) == records.get(records.size()-1).getParent_id()) {
                    parentRecordByLevel.add(cur_level, new Record(cursor.getInt(0), cursor.getString(2), cursor.getInt(3), cursor.getInt(4)));
                }
            }
            if(cur_level>parentRecordByLevel.size()-1)
                parentRecordByLevel.add(cur_level,new Record(0,"БАЗОВЫЙ УРОВЕНЬ",0,0));
        }else
            parentRecordByLevel.add(cur_level,new Record(0,"БАЗОВЫЙ УРОВЕНЬ",0,0));
    }

    private void CheckSelectionOfObjId(int position){
        if(selObjId==0 || selObjId!=objIdList.get(position)){
            selObjId = objIdList.get(position);
            cursor = readRequests.getRecords(selObjId);
        }
    }

    public void Destroy(){
        records.clear();
        parentRecordByLevel.clear();
        parentIdByLevels.clear();
        recordIdByLevels.clear();
        if(cursor!=null)
            cursor.close();
        if(cursorInit!=null)
            cursorInit.close();
        readRequests.Destroy();
        workThread.quit();
    }
}
