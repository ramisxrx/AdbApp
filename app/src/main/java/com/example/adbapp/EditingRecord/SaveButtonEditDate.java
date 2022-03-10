package com.example.adbapp.EditingRecord;

import android.widget.Button;

public class SaveButtonEditDate extends SaveButtonEdit{

    public SaveButtonEditDate(Button button){
        this.button = button;
        switchingToLock();
    }

    @Override
    public void switchingToDeleteCurrent() {
        super.switchingToDeleteCurrent();
        button.setText("Удалить дату");
    }

    @Override
    public void switchingToDeleteChain() {
        super.switchingToDeleteChain();
        button.setText("Удалить цепочку");
    }

    @Override
    public void switchingToUpdate() {
        super.switchingToUpdate();
        button.setText("Обновить дату");
    }

    @Override
    void setInitText() {
        button.setText("Обновить дату");
    }
}
