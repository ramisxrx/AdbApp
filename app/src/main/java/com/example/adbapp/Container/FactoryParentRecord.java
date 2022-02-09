package com.example.adbapp.Container;

import static com.example.adbapp.ContentView.TYPE_VIEW_0;
import static com.example.adbapp.ContentView.TYPE_RECORD;
import static com.example.adbapp.ContentView.TYPE_TEXT;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupMenu;

import com.example.adbapp.ContentView;
import com.example.adbapp.PopupMenuOfRecord.ActionsPopupMenu;
import com.example.adbapp.R;
import com.example.adbapp.RecordList.Record;

public class FactoryParentRecord {
    private final FrameLayout frameLayout;
    private final LayoutInflater inflater;
    private View view;
    private final Context context;
    private ActionsPopupMenu actionsPopupMenu;

    ContainerRecord containerRecord;

    public FactoryParentRecord(Context context, FrameLayout frameLayout) {
        this.context = context;
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

                case TYPE_RECORD:
                    containerRecord = new ParentRecord(view, frameLayout);
                    break;

                case TYPE_TEXT:
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


    public ContainerRecord createInitialContainer(Record record){
        view = ContentView.getViewParentRecord(inflater,frameLayout,record.getField_type());
        ContainerRecord containerRecord = getContainer(record.getField_type(),view,frameLayout);

        containerRecord.FillingContainer(record);

        return containerRecord;
    }

    public ContainerRecord recreateContainer(Record record, ContainerRecord containerRecord){

        if (containerRecord.getRecord().getField_type()!=record.getField_type()) {
            view = ContentView.getViewParentRecord(inflater,frameLayout,record.getField_type());
            ContainerRecord newContainerRecord = getContainer(record.getField_type(),view,frameLayout);
            newContainerRecord.FillingContainer(record);
            return newContainerRecord;
        }
        containerRecord.FillingContainer(record);
        return containerRecord;
    }

    private ContainerRecord getContainer(int type, View view, FrameLayout frameLayout){
        ContainerRecord containerRecord;
        switch (type) {
            case TYPE_RECORD:
                containerRecord = new ParentRecord(view, frameLayout);
                break;

            case TYPE_TEXT:
                containerRecord = new ParentText(view, frameLayout);
                break;
            default:
                containerRecord = new ParentZeroLevel(view,frameLayout);
                break;
        }

        return containerRecord;
    }

}
