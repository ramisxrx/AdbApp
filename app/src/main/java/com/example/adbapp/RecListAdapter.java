package com.example.adbapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.ListAdapter;

public class RecListAdapter extends ListAdapter<Record,RecordAdapter.ViewHolder> {

    interface OnRecordClickListener{
        void onRecordClick(Record record, int position);
    }

    private final OnRecordClickListener onClickListener;



    RecListAdapter(OnRecordClickListener onClickListener){
        this.onClickListener = onClickListener;

    }


}
