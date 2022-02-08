package com.example.adbapp.PopupMenuOfRecord;

import static com.example.adbapp.ContentView.TYPE_VIEW_0;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupMenu;

import com.example.adbapp.R;
import com.example.adbapp.RecordList.Record;

public class RecordPopupMenu{

    private Context context;
    private View view;
    private Record record;
    private final ActionsPopupMenu actionsPopupMenu;

    public RecordPopupMenu(Context context,View view,Record record,ActionsPopupMenu actionsPopupMenu) {
        this.context = context;
        this.record = record;
        this.actionsPopupMenu = actionsPopupMenu;

        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(R.menu.popup_menu);

        popupMenu.setOnMenuItemClickListener(new android.widget.PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.search_associations:
                        actionsPopupMenu.CheckingAssociations(record);
                        return true;

                    case R.id.edit_record:
                        actionsPopupMenu.SwitchingToEditing(record);
                        return true;
                }
                return false;
            }
        });

        if(record.getField_type()!=TYPE_VIEW_0)
            popupMenu.show();

    }

}
