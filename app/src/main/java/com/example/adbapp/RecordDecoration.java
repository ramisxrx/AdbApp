package com.example.adbapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecordDecoration extends RecyclerView.ItemDecoration {

    private final List<Record> records;

    private float mDividerHeight=2;
    private int itemOffsets=50;
    private Paint mPaint;
    private View view;

    RecordDecoration(List<Record> records) {
        this.records=records;
    }
    
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        //outRect.left=records.get(parent.getChildViewHolder(view).getAdapterPosition()).getLevel()*itemOffsets;
        outRect.left=records.get(parent.getChildViewHolder(view).getAdapterPosition()).getLevel()*itemOffsets;
// // Если это не первый, установить значение top.
        if (parent.getChildAdapterPosition(view) != 0){
            // Здесь прямо прописан в 1px
            outRect.top = 10;
        }

        outRect.left = 15;

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
/*
        int childCount = parent.getChildCount();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);

        int levels;

        mPaint.setStyle(Paint.Style.FILL);

        for ( int i = 0; i < childCount; i++ ) {

            view = parent.getChildAt(i);
            levels=records.get(parent.getChildAdapterPosition(view)).getLevel();

            for ( int j = 0; j < levels; j++ ) {

                if (j == levels - 1) {
                    c.drawLine(view.getLeft() - itemOffsets / 2, view.getTop() + (view.getBottom() - view.getTop()) / 2, view.getLeft(), view.getTop() + (view.getBottom() - view.getTop()) / 2, mPaint);
                }

                c.drawLine(view.getLeft() - itemOffsets / 2-itemOffsets*j, view.getTop(), view.getLeft() - itemOffsets / 2-itemOffsets*j, view.getBottom(), mPaint);
            }

            c.drawRoundRect(view.getLeft(),view.getTop(),view.getRight(),view.getBottom(),30,30,mPaint);
        }

 */
    }

}
