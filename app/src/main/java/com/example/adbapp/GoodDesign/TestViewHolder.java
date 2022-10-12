package com.example.adbapp.GoodDesign;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adbapp.R;

public class TestViewHolder extends RecyclerView.ViewHolder {
    final TextView textView; 
    public TestViewHolder(View itemView) {
        super(itemView);
        textView = (TextView)itemView.findViewById(R.id.textTestItem);
    }
}
