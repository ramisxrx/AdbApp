package com.example.adbapp.RecordList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.adbapp.ContentView;
import com.example.adbapp.ItemTouchHelper.ItemTouchHelperAdapter;
import com.example.adbapp.PopupMenuOfRecord.ActionsPopupMenu;
import com.example.adbapp.PopupMenuOfRecord.ItemPopupMenu;
import com.example.adbapp.PopupMenuOfRecord.RecordPopupMenu;
import com.example.adbapp.R;

import java.util.Date;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    private static final String TAG = "**RecordAdapter**";

    public interface OnRecordClickListener{
        void onRecordClick(Record record, int position);
    }
    public interface OnRecordLongClickListener{
        void onRecordLongClick(int position);
    }


    private final OnRecordClickListener onClickListener;
    private final OnRecordLongClickListener onLongClickListener;
    private final ActionsPopupMenu actionsPopupMenu=null;

    private boolean allowShowingPopupMenu;

    private final LayoutInflater inflater;
    private final Context context;
    public List<Record> records;
    public int typeView;
    public int posSelItem=-1;


    public RecordAdapter(Context context, List<Record> records, int typeView, OnRecordClickListener onClickListener,OnRecordLongClickListener onLongClickListener) {
        this.context = context;
        this.onClickListener = onClickListener;
        this.onLongClickListener = onLongClickListener;
        this.records = records;
        this.typeView = typeView;
        this.inflater = LayoutInflater.from(context);

        this.allowShowingPopupMenu = true;
    }

    @Override
    public RecordAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: viewType="+String.valueOf(viewType));
        View view = ContentView.getView(inflater,parent,viewType);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecordAdapter.ViewHolder holder, int position) {
        Record record = records.get(position);

        switch (getItemViewType(position)){
            case ContentView.TYPE_VIEW_0:
                holder.nameView.setText(record.getName());

                if(position==posSelItem) {
                    holder.itemView.setSelected(true);
                }
                else {
                    holder.itemView.setSelected(false);
                }

                Log.d(TAG, "onBindViewHolder: field");
                break;

            case ContentView.TYPE_RECORD+ContentView.FOUND_TYPE_BIAS:
                holder.nameView.setText(record.getName());

                if(position==posSelItem) {
                    holder.itemView.setSelected(true);
                }
                else {
                    holder.itemView.setSelected(false);
                }

                Log.d(TAG, "onBindViewHolder: found_record");
                break;

            case ContentView.TYPE_RECORD:
                holder.nameView.setText(record.getName());
                holder.timeView.setText(ContentView.getDateTimeFormat().format(new Date(record.getTime()*1000)));
                Log.d(TAG, "onBindViewHolder: record");
                break;
            case ContentView.TYPE_TEXT:
                holder.textView.setText(record.getName());
                holder.timeView.setText(ContentView.getDateTimeFormat().format(new Date(record.getTime()*1000)));
                Log.d(TAG, "onBindViewHolder: text");
                break;
            default:

                break;
        }


        // обработка нажатия
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                // вызываем метод слушателя, передавая ему данные
                onClickListener.onRecordClick(record, holder.getAdapterPosition());
                ItemPopupMenu itemPopupMenu = new ItemPopupMenu(context,v,record,actionsPopupMenu,allowShowingPopupMenu);
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
        Log.d(TAG, "getItemViewType: ");
        return ContentView.getListItemViewType(records.get(position),typeView);
    }

    public void setAllowShowingPopupMenu(boolean allowShowingPopupMenu) {
        this.allowShowingPopupMenu = allowShowingPopupMenu;
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
        final TextView textView;
        ViewHolder(View view){
            super(view);
            nameView = (TextView) view.findViewById(R.id.name);
            timeView = (TextView) view.findViewById(R.id.time);
            textView = (TextView) view.findViewById(R.id.text);

        }
    }

}
