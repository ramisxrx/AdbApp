package com.example.adbapp.RecordList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adbapp.ToPreviousLevel.FAB_ToPreviousLevel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class OnScrollListenerRecyclerView extends RecyclerView.OnScrollListener {

    private final FAB_ToPreviousLevel fab_toPreviousLevel;

    public OnScrollListenerRecyclerView(FAB_ToPreviousLevel fab_toPreviousLevel){
        this.fab_toPreviousLevel = fab_toPreviousLevel;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        //if(dy>0)
            //floatingActionButton.hide();
        //if(dy<0)
            //floatingActionButton.show();

        if (dy > 0 ||dy<0 && fab_toPreviousLevel.isShown())
        {
            fab_toPreviousLevel.hideAtScroll();
        }
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

        if (newState == RecyclerView.SCROLL_STATE_IDLE)
        {
            fab_toPreviousLevel.showAtScroll();
        }

        super.onScrollStateChanged(recyclerView, newState);
    }
}
