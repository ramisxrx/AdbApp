package com.example.adbapp.RecordList;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.adbapp.ContentView;
import com.example.adbapp.Interfaces.ItemTouchHelperAdapter;
import com.example.adbapp.PopupMenuOfRecord.ActionsPopupMenu;
import com.example.adbapp.PopupMenuOfRecord.ItemPopupMenu;
import com.example.adbapp.R;

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
    private ActionsPopupMenu actionsPopupMenu=null;

    private boolean allowShowingPopupMenu;
    private boolean imageButtonUsed;

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

        this.imageButtonUsed = true;
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

        if(typeView==0) {
            holder.timeView.setVisibility(View.GONE);
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(position==posSelItem);
        }
        else {
            holder.timeView.setVisibility(View.VISIBLE);
            holder.itemView.setSelected(position==posSelItem);
            holder.checkBox.setVisibility(View.GONE);
        }

        switch (getItemViewType(position)){
            case ContentView.TYPE_RECORD:
                holder.nameView.setText(record.getName());
                if(typeView!=0)
                    holder.timeView.setText(ContentView.getDateTime(record.getTime()));
                Log.d(TAG, "onBindViewHolder: record");
                break;
            case ContentView.TYPE_TEXT:
                holder.textView.setText(record.getName());
                holder.timeView.setText(ContentView.getDateTime(record.getTime()));
                Log.d(TAG, "onBindViewHolder: text");
                break;
            case ContentView.TYPE_DATE:
                holder.nameView.setText(record.getName());
                if(typeView!=0)
                    holder.timeView.setText(ContentView.getDateTime(record.getTime()));
                Log.d(TAG, "onBindViewHolder: date");
                break;
            case ContentView.TYPE_TIME:
                holder.nameView.setText(record.getName());
                if(typeView!=0)
                    holder.timeView.setText(ContentView.getDateTime(record.getTime()));
                Log.d(TAG, "onBindViewHolder: time");
                break;
            case ContentView.TYPE_PHOTO:
                holder.imageView.setImageURI(Uri.parse(record.getName()));
                if(typeView!=0)
                    //holder.timeView.setText(ContentView.getDateTime(record.getTime()));
                Log.d(TAG, "onBindViewHolder: time");
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
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                onLongClickListener.onRecordLongClick(holder.getAdapterPosition());
                return true;
            }
        });

        if(typeView==0)
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onRecordClick(record, holder.getAdapterPosition());
                }
            });

        if(record.getField_type()==ContentView.TYPE_RECORD ||
            record.getField_type()==ContentView.TYPE_DATE ||
            record.getField_type()==ContentView.TYPE_TIME ||
            record.getField_type()==ContentView.TYPE_PHOTO){
            if(allowShowingPopupMenu){
                holder.imageButton.setVisibility(View.VISIBLE);
                holder.imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ItemPopupMenu itemPopupMenu = new ItemPopupMenu(context,view,record,actionsPopupMenu,allowShowingPopupMenu);
                    }
                });
            }else
                holder.imageButton.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "getItemViewType: ");
        //return ContentView.getListItemViewType(records.get(position),typeView);
        return records.get(position).getField_type();
    }

    public void setImageButtonUsed(boolean imageButtonUsed){
        this.imageButtonUsed = imageButtonUsed;
    }

    public void setAllowShowingPopupMenu(boolean allowShowingPopupMenu) {
        this.allowShowingPopupMenu = allowShowingPopupMenu;
    }

    public void setActionsPopupMenu(ActionsPopupMenu actionsPopupMenu) {
        this.actionsPopupMenu = actionsPopupMenu;
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
        final ImageButton imageButton;
        final ImageView imageView;
        final CheckBox checkBox;
        ViewHolder(View view){
            super(view);
            nameView = (TextView) view.findViewById(R.id.name);
            timeView = (TextView) view.findViewById(R.id.time);
            textView = (TextView) view.findViewById(R.id.text);
            imageButton = (ImageButton) view.findViewById(R.id.imageButton);
            imageView = view.findViewById(R.id.imageView);
            checkBox = view.findViewById(R.id.checkBox);
        }
    }

}
