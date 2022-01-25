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
    public static final int TYPE_RECORD = 1; // для записей
    public static final int TYPE_TEXT = 2; // для мнострочного текста
    public static final int TYPE_DATE = 3; // для даты
    public static final int TYPE_TIME = 4; // для времени
    public static final int TYPE_VIEW_5 = 5; // для даты и времени
    public static final int TYPE_VIEW_6 = 6; // для номера телефона
    public static final int TYPE_VIEW_7 = 7; // для фото

    private static final int FOUND_TYPE_BIAS = 500;

    public ContentView() {

    }
    
    public static View getView(LayoutInflater layoutInflater,ViewGroup parent, int viewType){
        View view;

        switch(viewType){
            case TYPE_VIEW_0:
                view = layoutInflater.inflate(R.layout.field_item, parent, false);
                Log.d(TAG, "onCreateView: field_item");
                break;
            case TYPE_RECORD:
                view = layoutInflater.inflate(R.layout.record_item, parent, false);
                Log.d(TAG, "onCreateView: record_item");
                break;
            //case TYPE_RECORD+FOUND_TYPE_BIAS:
            case 501:
                view = layoutInflater.inflate(R.layout.found_record_item, parent, false);
                Log.d(TAG, "onCreateView: found_record_item");
                break;
            case TYPE_TEXT:
                view = layoutInflater.inflate(R.layout.text_item, parent, false);
                Log.d(TAG, "onCreateView: text_item");
                break;

            default:
                view = null;
                break;
        }

        return view;
    }

    public static View getViewParentRecord(LayoutInflater layoutInflater,ViewGroup parent, int viewType){
        View view;

        switch(viewType){
            case TYPE_VIEW_0:
                view = layoutInflater.inflate(R.layout.field_item, parent, false);
                Log.d(TAG, "getViewParentRecord: field");
                break;
            case TYPE_RECORD:
                view = layoutInflater.inflate(R.layout.record_item, parent, false);
                Log.d(TAG, "getViewParentRecord: record");
                break;
            case TYPE_TEXT:
                view = layoutInflater.inflate(R.layout.container_text, parent, false);
                Log.d(TAG, "getViewParentRecord: text");
                break;
            default:
                view = null;
                break;
        }

        return view;
    }

    public static int getListItemViewType(Record record, int typeView){
        if(typeView==0)
            //return record.getField_type()+FOUND_TYPE_BIAS;
            return 501;
        else
            return record.getField_type();
    }

    public static SimpleDateFormat getDateTimeFormat(){
        return new SimpleDateFormat("dd-MM-yyyy HH:mm"+ "", Locale.US);
    }
}
