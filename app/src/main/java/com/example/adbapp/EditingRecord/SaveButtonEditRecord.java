package com.example.adbapp.EditingRecord;

import android.widget.Button;

public class SaveButtonEditRecord extends SaveButtonEdit{

    public SaveButtonEditRecord(Button button){
        this.button = button;
        switchingToLock();
    }

    @Override
    public void switchingToDeleteCurrent() {
        super.switchingToDeleteCurrent();
        button.setText("Удалить запись");
    }

    @Override
    public void switchingToDeleteChain() {
        super.switchingToDeleteChain();
        button.setText("Удалить цепочку");
    }

    @Override
    public void switchingToUpdate() {
        super.switchingToUpdate();
        button.setText("Обновить запись");
    }

    @Override
    void setInitText() {
        button.setText("Обновить запись");
    }
}
