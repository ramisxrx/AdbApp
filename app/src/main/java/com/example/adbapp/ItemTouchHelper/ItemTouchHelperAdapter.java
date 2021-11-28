package com.example.adbapp.ItemTouchHelper;

public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);



    void onItemDismiss(int position);
}
