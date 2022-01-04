package com.example.adbapp;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class TypeRecordAdapter extends ArrayAdapter {

    private final LayoutInflater inflater;
    private final String[] objects = { "Запись", "Текст", "Дата" };
    private Context context;

    public TypeRecordAdapter(Context context, int textViewResourceId, String[] objects) {
        super(context, textViewResourceId, objects);
        this.context =context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        Toast toast = Toast.makeText(context,
                "Показываем: " + String.valueOf(position), Toast.LENGTH_SHORT);
        toast.show();
        if(convertView==null)
            convertView = inflater.inflate(R.layout.field_item, parent, false);
        TextView label = (TextView) convertView.findViewById(R.id.name);
        label.setText(objects[position]);

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Toast toast = Toast.makeText(context,
                "Ваш выбор: " + String.valueOf(position), Toast.LENGTH_SHORT);
        toast.show();
        if(convertView==null)
             convertView = inflater.inflate(R.layout.record_item, parent, false);
        TextView label = (TextView) convertView.findViewById(R.id.name);
        label.setText(objects[position]);

        return convertView;
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        View row = inflater.inflate(R.layout.field_item, parent, false);
        TextView label = (TextView) row.findViewById(R.id.name);
        label.setText(objects[position]);

        return row;
    }
}
