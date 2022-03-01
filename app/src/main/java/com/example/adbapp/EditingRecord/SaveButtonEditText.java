package com.example.adbapp.EditingRecord;

import android.widget.Button;

public class SaveButtonEditText extends SaveButtonEdit{

    public SaveButtonEditText(Button button){
        this.button = button;
        switchingToLock();
    }

    @Override
    public void switchingToDeleteCurrent() {
        super.switchingToDeleteCurrent();
        button.setText("Удалить текст");
    }

    @Override
    public void switchingToDeleteChain() {
        super.switchingToDeleteChain();
        button.setText("Удалить цепочку");
    }

    @Override
    public void switchingToUpdate() {
        super.switchingToUpdate();
        button.setText("Обновить текст");
    }

    @Override
    void setInitText() {
        button.setText("Обновить текст");
    }
}
