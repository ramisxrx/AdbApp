package com.example.adbapp.Interfaces;

public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);



    void onItemDismiss(int position);
}
