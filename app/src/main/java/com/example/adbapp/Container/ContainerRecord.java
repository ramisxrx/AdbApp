package com.example.adbapp.Container;

import android.media.Image;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.example.adbapp.PopupMenuOfRecord.CallPopupMenuContainer;
import com.example.adbapp.R;
import com.example.adbapp.RecordList.Record;

public abstract class ContainerRecord {
    private FrameLayout frameLayout;
    private ImageButton imageButton;
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

    protected void FindingNewViews(){
        imageButton = (ImageButton) view.findViewById(R.id.imageButton);
    }

    public void FillingContainer(Record record, CallPopupMenuContainer callPopupMenuContainer){
        if(this.record!=record)
            FindingNewViews();
        setRecord(record);
        fillView();
        DeleteOldViewFramelayout();
        AddNewViewFramelayout();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPopupMenuContainer.callPopupMenuContainer(view);
            }
        });
    }

    abstract void fillView();

    public Record getRecord(){
        return record;
    }

    public void setVisibleImageButton(boolean visibleImageButton){
        if(visibleImageButton)
            imageButton.setVisibility(View.VISIBLE);
        else
            imageButton.setVisibility(View.GONE);
    }
}
