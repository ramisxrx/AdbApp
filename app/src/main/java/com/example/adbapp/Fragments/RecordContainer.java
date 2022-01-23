package com.example.adbapp.Fragments;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.adbapp.Container.ContainerRecord;
import com.example.adbapp.Container.ParentRecord;
import com.example.adbapp.Container.ParentRecordBase;
import com.example.adbapp.ContentView;
import com.example.adbapp.Interfaces.Fillable;
import com.example.adbapp.R;
import com.example.adbapp.RecordList.Record;

import org.w3c.dom.Text;

import java.util.Date;

public class RecordContainer {

    private final FrameLayout frameLayout;
    private View view;
    private final FrameLayout.LayoutParams layoutParams;
    private final LayoutInflater inflater;
    private Record record;
    private TextView nameView,timeView;
    private TextView textView;

    private ParentRecordBase parentRecordBase;

    public RecordContainer(Context context, FrameLayout frameLayout) {
        this.inflater = LayoutInflater.from(context);
        this.frameLayout = frameLayout;
        layoutParams = new FrameLayout.LayoutParams
                (FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;

        parentRecordBase = new ParentRecordBase(context,frameLayout);
    }

    public void FillingContainer(Record record,int viewType){

        parentRecordBase.FillingContainer(record);

        /*
        this.record = record;
        if(view!=null)
            frameLayout.removeView(view);

        view = ContentView.getView(inflater,frameLayout,ContentView.getListItemViewType(record,viewType));
        //view.setLayoutParams(layoutParams);

        nameView = (TextView) view.findViewById(R.id.name);
        timeView = (TextView) view.findViewById(R.id.time);
        textView = (TextView) view.findViewById(R.id.text);

        switch (ContentView.getListItemViewType(record,viewType)){
            case ContentView.TYPE_VIEW_0:
                nameView.setText(record.getName());
                break;
            case ContentView.TYPE_VIEW_1:
                nameView.setText(record.getName());
                timeView.setText(ContentView.getDateTimeFormat().format(new Date(record.getTime()*1000)));
                break;
            case ContentView.TYPE_VIEW_2:
                textView.setText(record.getName());
                textView.setMaxLines(5);
                textView.setVerticalScrollBarEnabled(true);
                textView.setMovementMethod(new ScrollingMovementMethod());
                textView.setEnabled(false);
                timeView.setText(ContentView.getDateTimeFormat().format(new Date(record.getTime()*1000)));
                break;
            default:

                break;
        }
        frameLayout.addView(view);

         */
    }

    public Record getRecord(){
        return parentRecordBase.getRecord();
    }
}
