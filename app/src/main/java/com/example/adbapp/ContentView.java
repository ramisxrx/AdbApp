package com.example.adbapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.zip.Inflater;


public class ContentView{

    private static final String TAG = ContentView.class.getCanonicalName();

    private static final int TYPE_VIEW_0 = 0; // для показа fields
    private static final int TYPE_VIEW_1 = 1; // для записей
    private final int TYPE_VIEW_2 = 2; // для мнострочного текста
    private final int TYPE_VIEW_3 = 3; // для даты
    private final int TYPE_VIEW_4 = 4; // для времени
    private final int TYPE_VIEW_5 = 5; // для даты и времени
    private final int TYPE_VIEW_6 = 6; // для номера телефона
    private final int TYPE_VIEW_7 = 7; // для фото

    public ContentView() {

    }
    
    public static View getView(LayoutInflater layoutInflater,ViewGroup parent, int viewType){
        View view;

        switch(viewType){
            case TYPE_VIEW_0:
                view = layoutInflater.inflate(R.layout.field_item, parent, false);
                Log.d(TAG, "onCreateViewHolder: field_item");
                break;
            //case TYPE_VIEW_1:
            //    view = inflater.inflate(R.layout.record_item, parent, false);
            //    break;

            default:
                view = layoutInflater.inflate(R.layout.record_item, parent, false);
                Log.d(TAG, "onCreateViewHolder: record_item");
                break;
        }

        return view;
    }

}
