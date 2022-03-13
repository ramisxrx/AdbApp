package com.example.adbapp.EditingRecord;

import android.widget.Button;

public class SaveButtonEditTime extends SaveButtonEdit{

    public SaveButtonEditTime(Button button){
        this.button = button;
        switchingToLock();
    }

    @Override
    public void switchingToDeleteCurrent() {
        super.switchingToDeleteCurrent();
        button.setText("Удалить время");
    }

    @Override
    public void switchingToDeleteChain() {
        super.switchingToDeleteChain();
        button.setText("Удалить цепочку");
    }

    @Override
    public void switchingToUpdate() {
        super.switchingToUpdate();
        button.setText("Обновить время");
    }

    @Override
    void setInitText() {
        button.setText("Обновить время");
    }
}
