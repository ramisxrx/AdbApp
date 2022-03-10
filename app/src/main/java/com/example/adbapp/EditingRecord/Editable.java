package com.example.adbapp.EditingRecord;

import android.view.View;
import android.widget.FrameLayout;

import com.example.adbapp.RecordList.Record;

public abstract class Editable {

    protected Record record;
    protected View view;
    protected FrameLayout frameLayout;
    private ChangingEdit changingEdit;
    protected String initValue;

    abstract void fillView();

    abstract void findingViews();

    protected void changing(String change, String initValue){
        changingEdit.changedListener(change, initValue);
    }

    public void Initialization(Record record,View view, FrameLayout frameLayout,ChangingEdit changingEdit){
        this.record = record;
        this.view = view;
        this.frameLayout = frameLayout;
        this.changingEdit = changingEdit;
    }

    public void FillingContainer(){
        frameLayout.addView(view);
        findingViews();
        fillView();
    }

    public int getRecord_id(){
        return record.getRecord_id();
    }

    public Record getRecord(){
        return record;
    }


}
