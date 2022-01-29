package com.example.adbapp.EditingRecord;

import android.widget.Button;

public abstract class SaveButtonEdit {

    ActionOnClickSave actionOnClickSave;

    Button button;

    public void setActionOnClickSave(ActionOnClickSave actionOnClickSave){
        this.actionOnClickSave = actionOnClickSave;
    }

    public void switchingToLock(){
        button.setEnabled(false);
        if(button.getText()=="")
            setInitText();
    }

    public void switchingToDelete(){
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
