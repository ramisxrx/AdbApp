package com.example.adbapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adbapp.ItemTouchHelper.ItemTouchHelperAdapter;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private static final String TAG = "**RecordAdapter**";

    private final int TYPE_VIEW_0 = 0; // для показа fields
    private final int TYPE_VIEW_1 = 1; // для показа текстовых records

    interface OnRecordClickListener{
        void onRecordClick(Record record, int position);
    }


    private final OnRecordClickListener onClickListener;

    private final LayoutInflater inflater;
    private List<Record> records;
    private int typeView;
    public int posSelItem=-1;


    RecordAdapter(Context context, List<Record> records,int typeView, OnRecordClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.records = records;
        this.typeView = typeView;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

        switch(viewType){
            case TYPE_VIEW_0:
                view = inflater.inflate(R.layout.field_item, parent, false);
                Log.d(TAG, "onCreateViewHolder: field_item");
                break;
            //case TYPE_VIEW_1:
            //    view = inflater.inflate(R.layout.record_item, parent, false);
            //    break;

            default:
                view = inflater.inflate(R.layout.record_item, parent, false);
                Log.d(TAG, "onCreateViewHolder: record_item");
                break;
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecordAdapter.ViewHolder holder, int position) {
        Record record = records.get(position);

    //    holder.blockView.setPadding(records.get(position).getLevel()*80,0,0,0);

        switch (records.get(0).getRecord_id()){
            case TYPE_VIEW_0:
                holder.nameView.setText(record.getName());

                if(position==posSelItem) {
                    holder.radioButton.setChecked(true);
                    holder.itemView.setSelected(true);
                }
                else {
                    holder.radioButton.setChecked(false);
                    holder.itemView.setSelected(false);
                }

                Log.d(TAG, "onBindViewHolder: field");
                break;
            default:
                holder.nameView.setText(record.getName());
                holder.timeView.setText(String.valueOf(record.getTime()));
                Log.d(TAG, "onBindViewHolder: record");
                break;
        }


        // обработка нажатия
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                // вызываем метод слушателя, передавая ему данные
                onClickListener.onRecordClick(record, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        // условие для определения айтем какого типа выводить в конкретной позиции
        switch (records.get(0).getRecord_id()) {
            case TYPE_VIEW_0:
                return TYPE_VIEW_0;
            default:
                return TYPE_VIEW_1;
        }
    }

    @Override
    public void onItemDismiss(int position) {

    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition){

            return true;
    }


    @Override
    public int getItemCount() {
        return records.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameView, timeView;
        final RadioButton radioButton;
        final ConstraintLayout blockView;
        ViewHolder(View view){
            super(view);
            nameView = (TextView) view.findViewById(R.id.name);
            timeView = (TextView) view.findViewById(R.id.time);
            radioButton = (RadioButton) view.findViewById(R.id.radioButton);
            blockView = (ConstraintLayout) view.findViewById(R.id.block_field);
        }
    }

}
