package com.example.adbapp.Container;

import static com.example.adbapp.ContentView.TYPE_VIEW_0;
import static com.example.adbapp.ContentView.TYPE_RECORD;
import static com.example.adbapp.ContentView.TYPE_TEXT;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupMenu;

import com.example.adbapp.ContentView;
import com.example.adbapp.PopupMenuOfRecord.ActionsPopupMenu;
import com.example.adbapp.R;
import com.example.adbapp.RecordList.Record;

public class ParentRecordBase {
    private final FrameLayout frameLayout;
    private final LayoutInflater inflater;
    private View view;
    private final Context context;
    private ActionsPopupMenu actionsPopupMenu;

    ContainerRecord containerRecord;

    public ParentRecordBase(Context context, FrameLayout frameLayout, ActionsPopupMenu actionsPopupMenu) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.frameLayout = frameLayout;
        this.actionsPopupMenu = actionsPopupMenu;
    }

    public void FillingContainer(Record record){
        if (containerRecord==null || containerRecord.getRecord().getField_type()!=record.getField_type()) {

            view = ContentView.getViewParentRecord(inflater,frameLayout,record.getField_type());

            switch (record.getField_type()) {
                case TYPE_VIEW_0:
                    containerRecord = new ParentZeroLevel(view,frameLayout);
                    break;

                case TYPE_RECORD:
                    containerRecord = new ParentRecord(view, frameLayout);
                    break;

                case TYPE_TEXT:
                    containerRecord = new ParentText(view, frameLayout);
                    break;
                default:

                    break;
            }
        }
        containerRecord.FillingContainer(record);

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.inflate(R.menu.popup_menu);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.search_associations:
                                return true;

                            case R.id.edit_record:
                                actionsPopupMenu.SwitchingToEditing(containerRecord.getRecord());
                                return true;
                        }
                        return false;
                    }
                });

                if(containerRecord.getRecord().getField_type()!=TYPE_VIEW_0)
                    popupMenu.show();
            }
        });
    }

    public Record getRecord(){
        return containerRecord.getRecord();
    }
}
