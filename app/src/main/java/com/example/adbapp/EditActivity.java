package com.example.adbapp;

import static com.example.adbapp.ContentView.TYPE_RECORD;
import static com.example.adbapp.ContentView.TYPE_TEXT;
import static com.example.adbapp.ContentView.TYPE_VIEW_0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.adbapp.Container.ParentRecord;
import com.example.adbapp.Container.ParentText;
import com.example.adbapp.Container.ParentZeroLevel;
import com.example.adbapp.EditingRecord.Editable;
import com.example.adbapp.EditingRecord.EditableRecord;
import com.example.adbapp.RecordList.Record;

public class EditActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    LayoutInflater inflater;
    View viewEditable;

    Editable editable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Bundle extras = getIntent().getExtras();
        inflater = LayoutInflater.from(getApplicationContext());

        if (extras != null) {
            viewEditable = ContentView.getViewEditableRecord(inflater,frameLayout,extras.getInt("field_type"));
            switch (extras.getInt("field_type")) {
                case TYPE_RECORD:
                    editable = new EditableRecord();
                    break;
                default:

                    break;
            }
        }
        else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "ОШИБКА! Редактируемый не выбран", Toast.LENGTH_SHORT);
            toast.show();
            goHome(true);
        }

        editable.Initialization(new Record(extras.getInt("record_id"),
                                           extras.getString("name"),
                                           extras.getLong("time"),
                                           extras.getInt("field_type")),
                                viewEditable,
                                frameLayout);

        editable.FillingContainer();

    }

    private void goHome(boolean cancel){
        Intent data = new Intent();
        //data.putExtra("id of added record",recordId);
        if(cancel) {
            setResult(RESULT_CANCELED,data);
        }else {
            setResult(RESULT_OK, data);
        }
        finish();
    }
}