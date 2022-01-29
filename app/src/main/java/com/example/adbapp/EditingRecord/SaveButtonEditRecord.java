package com.example.adbapp.EditingRecord;

import android.widget.Button;

public class SaveButtonEditRecord extends SaveButtonEdit{

    public SaveButtonEditRecord(Button button){
        this.button = button;
    }

    @Override
    public void switchingToDelete() {
        super.switchingToDelete();
        button.setText("Удалить запись");
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
