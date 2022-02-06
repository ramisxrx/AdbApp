package com.example.adbapp.RecordList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class OnScrollListenerRecyclerView extends RecyclerView.OnScrollListener {

    FloatingActionButton floatingActionButton;

    public OnScrollListenerRecyclerView(FloatingActionButton floatingActionButton){
        this.floatingActionButton = floatingActionButton;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        //if(dy>0)
            //floatingActionButton.hide();
        //if(dy<0)
            //floatingActionButton.show();

        if (dy > 0 ||dy<0 && floatingActionButton.isShown())
        {
            floatingActionButton.hide();
        }
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

        if (newState == RecyclerView.SCROLL_STATE_IDLE)
        {
            floatingActionButton.show();
        }

        super.onScrollStateChanged(recyclerView, newState);
    }
}
