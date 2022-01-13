package com.example.adbapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adbapp.RecordList.Record;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.zip.Inflater;


public class ContentView{

    private static final String TAG = ContentView.class.getCanonicalName();

    public static final int TYPE_VIEW_0 = 0; // для показа fields
    public static final int TYPE_VIEW_1 = 1; // для записей
    public static final int TYPE_VIEW_2 = 2; // для мнострочного текста
    public static final int TYPE_VIEW_3 = 3; // для даты
    public static final int TYPE_VIEW_4 = 4; // для времени
    public static final int TYPE_VIEW_5 = 5; // для даты и времени
    public static final int TYPE_VIEW_6 = 6; // для номера телефона
    public static final int TYPE_VIEW_7 = 7; // для фото

    public ContentView() {

    }
    
    public static View getView(LayoutInflater layoutInflater,ViewGroup parent, int viewType){
        View view;

        switch(viewType){
            case TYPE_VIEW_0:
                view = layoutInflater.inflate(R.layout.field_item, parent, false);
                Log.d(TAG, "onCreateView: field_item");
                break;
            //case TYPE_VIEW_1:
            //    view = inflater.inflate(R.layout.record_item, parent, false);
            //    break;

            default:
                view = layoutInflater.inflate(R.layout.record_item, parent, false);
                Log.d(TAG, "onCreateView: record_item");
                break;
        }

        return view;
    }

    public static int getListItemViewType(Record record, int typeView){
        if(typeView==0)
            return TYPE_VIEW_0;
        else
            return record.getField_type();
    }

    public static SimpleDateFormat getDateTimeFormat(){
        return new SimpleDateFormat("dd-MM-yyyy HH:mm"+ "", Locale.US);
    }
}
