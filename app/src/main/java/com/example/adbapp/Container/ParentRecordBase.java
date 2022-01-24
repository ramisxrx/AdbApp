package com.example.adbapp.Container;

import static com.example.adbapp.ContentView.TYPE_VIEW_0;
import static com.example.adbapp.ContentView.TYPE_VIEW_1;
import static com.example.adbapp.ContentView.TYPE_VIEW_2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.example.adbapp.ContentView;
import com.example.adbapp.Interfaces.Fillable;
import com.example.adbapp.Interfaces.JumpCommand;
import com.example.adbapp.R;
import com.example.adbapp.RecordList.Record;

public class ParentRecordBase {
    private final FrameLayout frameLayout;
    private final LayoutInflater inflater;
    private View view;

    ContainerRecord containerRecord;

    public ParentRecordBase(Context context, FrameLayout frameLayout) {
        this.inflater = LayoutInflater.from(context);
        this.frameLayout = frameLayout;
    }

    public void FillingContainer(Record record){
        if (containerRecord==null || containerRecord.getRecord().getField_type()!=record.getField_type()) {

            view = ContentView.getViewParentRecord(inflater,frameLayout,record.getField_type());

            switch (record.getField_type()) {
                case TYPE_VIEW_0:
                    containerRecord = new ParentZeroLevel(view,frameLayout);
                    break;

                case TYPE_VIEW_1:
                    containerRecord = new ParentRecord(view, frameLayout);
                    break;

                case TYPE_VIEW_2:
                    containerRecord = new ParentText(view, frameLayout);
                    break;
                default:

                    break;
            }
        }
        containerRecord.FillingContainer(record);
    }

    public Record getRecord(){
        return containerRecord.getRecord();
    }
}
