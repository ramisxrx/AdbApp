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
    private Paint mPaint;
    private View view;

    RecordDecoration(List<Record> records) {
        this.records=records;
    }
    
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        outRect.left=records.get(parent.getChildViewHolder(view).getAdapterPosition()).getLevel()*80;
// // Если это не первый, установить значение top.
        if (parent.getChildAdapterPosition(view) != 0){
            // Здесь прямо прописан в 1px
            outRect.top = 10;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        int childCount = parent.getChildCount();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
/*
        for ( int i = 0; i < childCount; i++ ) {

            int index = parent.getChildAdapterPosition(view);
            // Первый ItemView не нужно рисовать
            if ( index == 0 ) {
                continue;
            }

            float dividerTop = view.getTop() - mDividerHeight;
            float dividerLeft = parent.getPaddingLeft();
            float dividerBottom = view.getTop();
            float dividerRight = parent.getWidth() - parent.getPaddingRight();

            c.drawRect(dividerLeft,dividerTop,dividerRight,dividerBottom,mPaint);

        }
*/
        mPaint.setStyle(Paint.Style.FILL);

        for ( int i = 0; i < childCount; i++ ) {

            view = parent.getChildAt(i);
            c.drawRoundRect(view.getLeft(),view.getTop(),view.getRight(),view.getBottom(),50,50,mPaint);
        }
    }

}
