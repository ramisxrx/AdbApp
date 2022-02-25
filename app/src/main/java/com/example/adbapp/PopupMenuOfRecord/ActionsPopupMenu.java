package com.example.adbapp.PopupMenuOfRecord;

import com.example.adbapp.RecordList.Record;

public interface ActionsPopupMenu {
    void SwitchingToAddRecord();
    void CheckingAssociations(Record record);
    void SwitchingToEditing(Record record);
}
