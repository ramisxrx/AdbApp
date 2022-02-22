package com.example.adbapp.PopupMenuOfRecord;

import static com.example.adbapp.ContentView.TYPE_VIEW_0;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupMenu;

import com.example.adbapp.ContentView;
import com.example.adbapp.R;
import com.example.adbapp.RecordList.Record;

public class RecordPopupMenu{

    private Context context;
    private View view;
    protected Record record;
    private final ActionsPopupMenu actionsPopupMenu;
    protected PopupMenu popupMenu;

    public RecordPopupMenu(Context context,View view,Record record,ActionsPopupMenu actionsPopupMenu) {
        this.context = context;
        this.record = record;
        this.actionsPopupMenu = actionsPopupMenu;

        popupMenu = new PopupMenu(context, view);

        selectionInflate();

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

        popupMenu.show();
    }

    protected void selectionInflate(){
        if(record.getField_type() == ContentView.TYPE_RECORD)
            popupMenu.inflate(R.menu.popup_menu);

        if(record.getField_type() == ContentView.TYPE_TEXT)
            popupMenu.inflate(R.menu.popup_menu_only_edit);

        if(record.getField_type() == TYPE_VIEW_0)
            popupMenu.inflate(R.menu.popup_menu_only_add);
    }

}
