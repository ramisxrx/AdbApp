package com.example.adbapp.Container;

import android.view.View;
import android.widget.FrameLayout;

import com.example.adbapp.Interfaces.Fillable;
import com.example.adbapp.RecordList.Record;

public abstract class ContainerRecord {
    private FrameLayout frameLayout;
    protected View view;
    protected Record record;

    protected ContainerRecord(FrameLayout frameLayout, View view){
        this.frameLayout = frameLayout;
        this.view = view;
    }

    private void DeleteOldViewFramelayout(){
        frameLayout.removeAllViews();
    }

    private void AddNewViewFramelayout(){
        frameLayout.addView(view);
    }

    protected void setRecord(Record record){
        this.record = record;
    }

    abstract void FindingNewViews();

    public void FillingContainer(Record record){
        if(this.record!=record)
            FindingNewViews();
        setRecord(record);
        fillView();
        DeleteOldViewFramelayout();
        AddNewViewFramelayout();
    }

    abstract void fillView();

    public Record getRecord(){
        return record;
    }
}
