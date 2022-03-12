package com.example.adbapp.PopupMenuOfRecord;

import static com.example.adbapp.ContentView.TYPE_VIEW_0;

import android.content.Context;
import android.view.View;

import com.example.adbapp.ContentView;
import com.example.adbapp.R;
import com.example.adbapp.RecordList.Record;

public class RecordPopupMenuAssociations extends RecordPopupMenu{
    public RecordPopupMenuAssociations(Context context, View view, Record record, ActionsPopupMenu actionsPopupMenu) {
        super(context, view, record, actionsPopupMenu);
    }

    @Override
    protected void selectionInflate() {
        if(record.getField_type() == ContentView.TYPE_RECORD ||
            record.getField_type() == ContentView.TYPE_DATE)
            popupMenu.inflate(R.menu.item_popup_menu);
    }
}
