package com.example.adbapp.EditingRecord;

import android.view.View;
import android.widget.FrameLayout;

import com.example.adbapp.RecordList.Record;

public abstract class Editable {

    protected Record record;
    protected View view;
    protected FrameLayout frameLayout;

    abstract void fillView();

    abstract void findingViews();

    public void Initialization(Record record,View view, FrameLayout frameLayout){
        this.record = record;
        this.view = view;
        this.frameLayout = frameLayout;

    }

    public void FillingContainer(){
        findingViews();
        fillView();
        frameLayout.addView(view);
    }

}
