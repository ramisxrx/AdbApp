package com.example.adbapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adbapp.RecordList.Record;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.zip.Inflater;


public class ContentView{

    private static final String TAG = ContentView.class.getCanonicalName();

    public static final int TYPE_VIEW_0 = 0; // для показа fields
    public static final int TYPE_RECORD = 1; // для записей
    public static final int TYPE_TEXT = 2; // для мнострочного текста
    public static final int TYPE_DATE = 3; // для даты
    public static final int TYPE_TIME = 4; // для времени
    public static final int TYPE_PHOTO = 5; // для фото
    public static final int TYPE_VIEW_6 = 6; // для номера телефона
    public static final int TYPE_VIEW_7 = 7; // для фото

    public static final int FOUND_TYPE_BIAS = 500;

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
            case TYPE_RECORD+FOUND_TYPE_BIAS:
                view = layoutInflater.inflate(R.layout.found_record_item, parent, false);
                Log.d(TAG, "onCreateView: found_record_item");
                break;
            case TYPE_TEXT:
                view = layoutInflater.inflate(R.layout.text_item, parent, false);
                Log.d(TAG, "onCreateView: text_item");
                break;
            case TYPE_DATE:
                view = layoutInflater.inflate(R.layout.date_item, parent, false);
                Log.d(TAG, "onCreateView: date_item");
                break;
            case TYPE_TIME:
                view = layoutInflater.inflate(R.layout.time_item, parent, false);
                Log.d(TAG, "onCreateView: time_item");
                break;
            case TYPE_PHOTO:
                view = layoutInflater.inflate(R.layout.photo_item, parent, false);
                Log.d(TAG, "onCreateView: photo_item");
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
            case TYPE_DATE:
                view = layoutInflater.inflate(R.layout.date_item, parent, false);
                Log.d(TAG, "getViewParentRecord: date");
                break;
            case TYPE_TIME:
                view = layoutInflater.inflate(R.layout.time_item, parent, false);
                Log.d(TAG, "getViewParentRecord: time");
                break;
            case TYPE_PHOTO:
                view = layoutInflater.inflate(R.layout.photo_item, parent, false);
                Log.d(TAG, "getViewParentRecord: photo");
                break;
            default:
                view = null;
                break;
        }

        return view;
    }

    public static View getViewEditableRecord(LayoutInflater layoutInflater,ViewGroup parent, int viewType){
        View view;
        switch(viewType){
            case TYPE_RECORD:
                view = layoutInflater.inflate(R.layout.container_edit_record, parent, false);
                Log.d(TAG, "getViewEditableRecord: record");
                break;
            case TYPE_TEXT:
                view = layoutInflater.inflate(R.layout.container_edit_text, parent, false);
                Log.d(TAG, "getViewEditableRecord: text");
                break;
            case TYPE_DATE:
                //view = layoutInflater.inflate(R.layout.container_edit_date, parent, false);
                view = layoutInflater.inflate(R.layout.container_edit_dialog, parent, false);
                Log.d(TAG, "getViewEditableRecord: date");
                break;
            case TYPE_TIME:
                view = layoutInflater.inflate(R.layout.container_edit_time, parent, false);
                Log.d(TAG, "getViewEditableRecord: time");
                break;
            case TYPE_PHOTO:
                view = layoutInflater.inflate(R.layout.container_edit_photo, parent, false);
                Log.d(TAG, "getViewEditableRecord: photo");
                break;
            default:
                view = null;
                break;
        }
        return view;
    }

    public static int getListItemViewType(Record record, int typeView){
        Log.d(TAG, "getListItemViewType: record.getField_type()="+String.valueOf(record.getField_type()));
        if(typeView==0) {
            Log.d(TAG, "getListItemViewType: FOUND_TYPE_BIAS="+String.valueOf(FOUND_TYPE_BIAS));
            Log.d(TAG, "getListItemViewType: record.getField_type() + FOUND_TYPE_BIAS="+String.valueOf(record.getField_type() + FOUND_TYPE_BIAS));
            return record.getField_type() + FOUND_TYPE_BIAS;
        }
        else
            return record.getField_type();
    }

    public static SimpleDateFormat getDateTimeFormat(){
        return new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US);
    }

    public static String getDateTime(long time){
        return getDateTimeFormat().format(new Date(time*1000));
    }

    private static SimpleDateFormat getDateFormatForDates(){
        return new SimpleDateFormat("dd MMMM yyyy года", new Locale("ru"));
    }

    public static String getDateForDates(Date date){
        return getDateFormatForDates().format(date);
    }

    public static Date getDateForDates(String date) {
        try {
            return getDateFormatForDates().parse(date);
        } catch (ParseException e){
            e.printStackTrace();
            Calendar c = Calendar.getInstance();
            return c.getTime();
        }
    }

    private static SimpleDateFormat getTimeFormatForTimes(){
        return new SimpleDateFormat(" HH:mm", new Locale("ru"));
    }

    public static String getTimeForTimes(Date date){
        return getTimeFormatForTimes().format(date);
    }

    public static Date getTimeForTimes(String date) {
        try {
            return getTimeFormatForTimes().parse(date);
        } catch (ParseException e){
            e.printStackTrace();
            Calendar c = Calendar.getInstance();
            return c.getTime();
        }
    }

}
