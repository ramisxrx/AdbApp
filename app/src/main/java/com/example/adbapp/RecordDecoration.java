package com.example.adbapp;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class RecordDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

// // Если это не первый, установить значение top.
        if (parent.getChildAdapterPosition(view) != 0){
            // Здесь прямо прописан в 1px
            outRect.top = 1;
        }
    }
}
