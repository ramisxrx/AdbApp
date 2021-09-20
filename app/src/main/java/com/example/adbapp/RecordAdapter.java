package com.example.adbapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    interface OnStateClickListener{
        void onStateClick(Record record, int position);
    }

    private final OnStateClickListener onClickListener;

    private final LayoutInflater inflater;
    private final List<Record> records;


    RecordAdapter(Context context, List<Record> records, OnStateClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.records = records;
        this.inflater = LayoutInflater.from(context);
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
        holder.timeView.setText(record.getTime());

        // обработка нажатия
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                // вызываем метод слушателя, передавая ему данные
                onClickListener.onStateClick(record, holder.getAdapterPosition());
            }
        });

    }


    @Override
    public int getItemCount() {
        return records.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameView, timeView;
        ViewHolder(View view){
            super(view);
            nameView = (TextView) view.findViewById(R.id.name);
            timeView = (TextView) view.findViewById(R.id.time);
        }
    }
}
