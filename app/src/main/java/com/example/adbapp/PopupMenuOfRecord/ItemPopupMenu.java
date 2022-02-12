package com.example.adbapp.PopupMenuOfRecord;

import static com.example.adbapp.ContentView.TYPE_VIEW_0;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.example.adbapp.R;
import com.example.adbapp.RecordList.Record;

public class ItemPopupMenu {

    private Context context;
    private View view;
    private Record record;
    private final ActionsPopupMenu actionsPopupMenu;
    private boolean allowShowingMenu;

    public ItemPopupMenu(Context context, View view, Record record, ActionsPopupMenu actionsPopupMenu,boolean allowShowingMenu) {
        this.context = context;
        this.record = record;
        this.actionsPopupMenu = actionsPopupMenu;
        this.allowShowingMenu = allowShowingMenu;

        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(R.menu.item_popup_menu);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.search_associations:
                        actionsPopupMenu.CheckingAssociations(record);
                        return true;
                }
                return false;
            }
        });

        if(allowShowingMenu)
            popupMenu.show();

    }

}
