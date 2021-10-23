package com.example.adbapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    interface OnRecordClickListener{
        void onRecordClick(Record record, int position);
    }

    interface OnRecordCBindListener{
        void onRecordBind(int position);
    }

    private final OnRecordClickListener onClickListener;
    private final OnRecordCBindListener onBindListener;

    private final LayoutInflater inflater;
    private List<Record> records;


    RecordAdapter(Context context, List<Record> records, OnRecordClickListener onClickListener,OnRecordCBindListener onBindListener) {
        this.onClickListener = onClickListener;
        this.records = records;
        this.inflater = LayoutInflater.from(context);
        this.onBindListener=onBindListener;
    }

    @Override
    public RecordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.record_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecordAdapter.ViewHolder holder, int position) {
        Record record = records.get(position);
        holder.nameView.setText(record.getName());
        holder.timeView.setText(String.valueOf(record.getTime()));

    //    holder.blockView.setPadding(records.get(position).getLevel()*80,0,0,0);


        // обработка нажатия
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                // вызываем метод слушателя, передавая ему данные
                onClickListener.onRecordClick(record, holder.getAdapterPosition());
            }
        });

        onBindListener.onRecordBind(holder.getAdapterPosition());

    }

    public List<Record> getData(){
        return this.records;
    }

    public void setData(List<Record> records){
        this.records.clear();
        this.records = records;
    }


    @Override
    public int getItemCount() {
        return records.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameView, timeView;
        final ConstraintLayout blockView;
        ViewHolder(View view){
            super(view);
            nameView = (TextView) view.findViewById(R.id.name);
            timeView = (TextView) view.findViewById(R.id.time);
            blockView = (ConstraintLayout) view.findViewById(R.id.block_field);
        }
    }
}
