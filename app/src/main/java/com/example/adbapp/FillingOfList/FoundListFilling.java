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
    private Cursor cursorSearch;
    private ArrayList<Integer> fieldIdList = new ArrayList<>();
    private ArrayList<Record> fields = new ArrayList<>();

    public FoundListFilling(Context context, NotifyViews_after notifyViews_after) {
        super(context);
        this.notifyViews_after = notifyViews_after;

        workThread = new HandlerThreadOfFilling("FoundListFilling");
    }

    @Override
    public void ActionDown(int position) {

    }

    @Override
    public void ActionUp(int position) {

    }

    @Override
    public void ToPreviousLevel() {

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

        fields.clear();

        if(charsequence.length()>0) {
            cursorSearch = databaseHelper.getFields(charsequence.toString());

            while (cursorSearch.moveToNext())
                fields.add(new Record(cursorSearch.getInt(0),cursorSearch.getString(1),0,0));

            CopyFieldsToRecords();

        }
    }

    private void CopyFieldsToRecords(){
        ClearRecords();
        for(int i=0;i<fields.size();i++)
            records.add(i,fields.get(i));
    }

}
