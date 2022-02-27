package com.example.adbapp.ToPreviousLevel;

import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FAB_ToPreviousLevel {

    private final FloatingActionButton floatingActionButton;
    private ActionsClickFAB actionsClickFAB;
    private boolean allowShow;

    public FAB_ToPreviousLevel(FloatingActionButton floatingActionButton) {
        this.floatingActionButton = floatingActionButton;

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionsClickFAB.ToPreviousLevel();
            }
        });
    }

    public void setActionsOnClick(ActionsClickFAB actionsClickFAB){
        this.actionsClickFAB = actionsClickFAB;
    }

    public void actionsAfterInitialization(){
        floatingActionButton.hide();
    }

    public void actionsAfterActionsDown(){
        floatingActionButton.show();
    }

    public void actionsAfterActionsUp(){
        floatingActionButton.show();
    }

    public void actionsAfterToPreviousLevel(boolean hideButton){
        if(hideButton)
            floatingActionButton.hide();
    }

    public void hideAtScroll(){
        floatingActionButton.hide();
    }

    public void showAtScroll(){
        if(allowShow)
            floatingActionButton.show();
    }

    public boolean isShown(){
        return floatingActionButton.isShown();
    }

}
