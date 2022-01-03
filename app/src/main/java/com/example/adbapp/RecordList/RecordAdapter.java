package com.example.adbapp.RecordList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.adbapp.ContentView;
import com.example.adbapp.ItemTouchHelper.ItemTouchHelperAdapter;
import com.example.adbapp.R;

import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private static final String TAG = "**RecordAdapter**";

    private final int TYPE_VIEW_0 = 0; // для показа fields
    private final int TYPE_VIEW_1 = 1; // для записей
    private final int TYPE_VIEW_2 = 2; // для мнострочного текста
    private final int TYPE_VIEW_3 = 3; // для даты
    private final int TYPE_VIEW_4 = 4; // для времени
    private final int TYPE_VIEW_5 = 5; // для даты и времени
    private final int TYPE_VIEW_6 = 6; // для номера телефона
    private final int TYPE_VIEW_7 = 7; // для фото


    public interface OnRecordClickListener{
        void onRecordClick(Record record, int position);
    }
    public interface OnRecordLongClickListener{
        void onRecordLongClick(int position);
    }


    private final OnRecordClickListener onClickListener;
    private final OnRecordLongClickListener onLongClickListener;

    private final LayoutInflater inflater;
    public List<Record> records;
    public int typeView;
    public int posSelItem=-1;


    public RecordAdapter(Context context, List<Record> records, int typeView, OnRecordClickListener onClickListener,OnRecordLongClickListener onLongClickListener) {
        this.onClickListener = onClickListener;
        this.onLongClickListener = onLongClickListener;
        this.records = records;
        this.typeView = typeView;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = ContentView.getView(inflater,parent,viewType);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecordAdapter.ViewHolder holder, int position) {
        Record record = records.get(position);

        switch (getItemViewType(position)){
            case TYPE_VIEW_0:
                holder.nameView.setText(record.getName());

                if(position==posSelItem) {
                    holder.itemView.setSelected(true);
                }
                else {
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

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onLongClickListener.onRecordLongClick(holder.getAdapterPosition());
                return true;
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return ContentView.getListItemViewType(records.get(position),typeView);
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
        ViewHolder(View view){
            super(view);
            nameView = (TextView) view.findViewById(R.id.name);
            timeView = (TextView) view.findViewById(R.id.time);

        }
    }

}
