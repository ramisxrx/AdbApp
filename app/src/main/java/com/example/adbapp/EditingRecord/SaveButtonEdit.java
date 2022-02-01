package com.example.adbapp.EditingRecord;

import android.view.View;
import android.widget.Button;

public abstract class SaveButtonEdit {

    ActionOnClickSave actionOnClickSave;

    Button button;

    public void setActionOnClickSave(ActionOnClickSave actionOnClickSave){
        this.actionOnClickSave = actionOnClickSave;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionOnClickSave.onClick();
            }
        });
    }

    public void switchingToLock(){
        button.setEnabled(false);
        if(button.getText()=="")
            setInitText();
    }

    public void switchingToDeleteCurrent(){
        button.setEnabled(true);
    }

    public void switchingToDeleteChain(){
        button.setEnabled(true);
    }

    public void switchingToUpdate(){
        button.setEnabled(true);
    }

    public void onClick(){
        actionOnClickSave.onClick();
    }

    abstract void setInitText();
}
