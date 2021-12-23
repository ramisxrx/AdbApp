package com.example.adbapp.ItemTouchHelper;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    interface OnRecordSwipeLeftListener{
        void onRecordSwipe(int position);
    }

    private final ItemTouchHelperAdapter mAdapter;
    private final OnRecordSwipeLeftListener onSwipeLeftListener;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter,OnRecordSwipeLeftListener onSwipeLeftListener) {
        mAdapter = adapter;
        this.onSwipeLeftListener = onSwipeLeftListener;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
        onSwipeLeftListener.onRecordSwipe(viewHolder.getAdapterPosition());
    }

}
