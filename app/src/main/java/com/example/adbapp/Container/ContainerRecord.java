package com.example.adbapp.Container;

import android.view.View;
import android.widget.FrameLayout;

import com.example.adbapp.Interfaces.Fillable;
import com.example.adbapp.RecordList.Record;

public abstract class ContainerRecord {
    private Fillable fillable;
    private FrameLayout frameLayout;
    private View view;
    protected Record record;

    protected ContainerRecord(FrameLayout frameLayout, View view){
        this.frameLayout = frameLayout;
        this.view = view;
        DeleteOldViewFramelayout();
        //if(thisIsNewView(view)){
         //   FindingNewViews();
        //}
    }

    private boolean thisIsNewView(View view){
        return this.view != view || this.view==null;
    }

    private void DeleteOldViewFramelayout(){
        frameLayout.removeAllViews();
    }

    private void AddNewViewFramelayout(){
        frameLayout.addView(view);
    }

    protected void setFillable(Fillable fillable){
        this.fillable = fillable;
    }

    protected void setRecord(Record record){
        this.record = record;
    }

    abstract void FindingNewViews();

    public void FillingContainer(Record record){
        setRecord(record);
       //if(thisIsNewView(view))
            FindingNewViews();

        fillable.fill(record);
        DeleteOldViewFramelayout();
        AddNewViewFramelayout();
    }

    public Record getRecord(){
        return record;
    }
}
