package com.example.adbapp;

import static com.example.adbapp.ContentView.TYPE_RECORD;
import static com.example.adbapp.ContentView.TYPE_TEXT;
import static com.example.adbapp.ContentView.TYPE_VIEW_0;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.adbapp.Container.ParentRecord;
import com.example.adbapp.Container.ParentText;
import com.example.adbapp.Container.ParentZeroLevel;
import com.example.adbapp.DataBase.ReadRequests;
import com.example.adbapp.DataBase.WriteRequests;
import com.example.adbapp.EditingRecord.ActionOfDeleteChain;
import com.example.adbapp.EditingRecord.ActionOfDeleteCurrent;
import com.example.adbapp.EditingRecord.ActionOfUpdate;
import com.example.adbapp.EditingRecord.ChangingEdit;
import com.example.adbapp.EditingRecord.Editable;
import com.example.adbapp.EditingRecord.EditableRecord;
import com.example.adbapp.EditingRecord.SaveButtonEdit;
import com.example.adbapp.EditingRecord.SaveButtonEditRecord;
import com.example.adbapp.EditingRecord.SaveButtonEditText;
import com.example.adbapp.EditingRecord.UIActionAtDeleteChain;
import com.example.adbapp.EditingRecord.UIActionAtDeleteCurrent;
import com.example.adbapp.EditingRecord.UIActionAtUpdate;
import com.example.adbapp.RecordList.Record;
import com.example.adbapp.Threads.HandlerThreadOfFilling;

public class EditActivity extends AppCompatActivity implements ChangingEdit, UIActionAtUpdate, UIActionAtDeleteCurrent, UIActionAtDeleteChain {

    FrameLayout frameLayout;
    LayoutInflater inflater;
    View viewEditable;
    Button saveButton;
    RadioGroup radioGroup;
    ActionBar actionBar;

    HandlerThreadOfFilling workThread;
    WriteRequests writeRequests;
    ReadRequests readRequests;

    Editable editable;
    SaveButtonEdit saveButtonEdit;

    int editable_type;
    String saveMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Bundle extras = getIntent().getExtras();
        inflater = LayoutInflater.from(getApplicationContext());
        frameLayout = (FrameLayout) findViewById(R.id.container_edit_record);
        saveButton = (Button) findViewById(R.id.saveButton);
        radioGroup = (RadioGroup)findViewById(R.id.radios);
        actionBar = getSupportActionBar();

        workThread = new HandlerThreadOfFilling("EditActivity");
        writeRequests = new WriteRequests(getApplicationContext());
        readRequests = new ReadRequests(getApplicationContext());

        if(actionBar!=null) {
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int id) {
                switch (id) {
                    case R.id.deleteCurrent:
                        saveButtonEdit.switchingToDeleteCurrent();
                        saveMode = "DeleteCurrent";
                        saveButtonEdit.setActionOnClickSave(new ActionOfDeleteCurrent(editable.getRecord(),
                                workThread,
                                writeRequests,
                                readRequests,
                                EditActivity.this));
                        break;
                    case R.id.deleteChain:
                        saveButtonEdit.switchingToDeleteChain();
                        saveMode = "DeleteChain";
                        saveButtonEdit.setActionOnClickSave(new ActionOfDeleteChain(editable.getRecord(),
                                workThread,
                                writeRequests,
                                readRequests,
                                EditActivity.this));
                        break;
                    default:
                        break;
                }
            }});

        if (extras != null) {
            editable_type = extras.getInt("field_type");
            viewEditable = ContentView.getViewEditableRecord(inflater,frameLayout,extras.getInt("field_type"));
            switch (extras.getInt("field_type")) {
                case TYPE_RECORD:
                    actionBar.setSubtitle("Редактирование записи");
                    editable = new EditableRecord();
                    saveButtonEdit = new SaveButtonEditRecord(saveButton);
                    break;
                case TYPE_TEXT:
                    actionBar.setSubtitle("Редактирование текста");
                    editable = new EditableRecord();
                    saveButtonEdit = new SaveButtonEditText(saveButton);
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
                                frameLayout,
                     this);

        editable.FillingContainer();

    }

    private void goHome(boolean cancel){
        Intent data = new Intent();
        data.putExtra("Activity","Edit");
        data.putExtra("SaveMode",saveMode);
        if(cancel) {
            setResult(RESULT_CANCELED,data);
        }else {
            setResult(RESULT_OK, data);
        }
        finish();
    }

    @Override
    public void changedListener(String change, String initValue) {
        radioGroup.clearCheck();
        saveMode = "Update";
        saveButtonEdit.setActionOnClickSave(new ActionOfUpdate(editable.getRecord_id(),
                change,
                workThread,
                readRequests,
                writeRequests,
                this));

        if(change.equals(initValue))
            saveButtonEdit.switchingToLock();
        else
            saveButtonEdit.switchingToUpdate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goHome(true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void beforeActionOfUpdate() {

    }

    @Override
    public void afterActionOfUpdate() {
        String message;
        switch (editable_type) {
            case TYPE_RECORD:
                message = "Запись обновлена";
                break;
            case TYPE_TEXT:
                message = "Текст обновлен";
                break;
            default:
                message = "";
                break;
        }

        Toast toast = Toast.makeText(getApplicationContext(),
                message, Toast.LENGTH_SHORT);
        toast.show();

        goHome(false);
    }

    @Override
    public void beforeActionOfDeleteCurrent() {

    }

    @Override
    public void afterActionOfDeleteCurrent() {
        String message;
        switch (editable_type) {
            case TYPE_RECORD:
                message = "Текущая запись удалена";
                break;
            case TYPE_TEXT:
                message = "Текущий текст удален";
                break;
            default:
                message = "";
                break;
        }

        Toast toast = Toast.makeText(getApplicationContext(),
                message, Toast.LENGTH_SHORT);
        toast.show();

        goHome(false);
    }

    @Override
    public void beforeActionOfDeleteChain() {

    }

    @Override
    public void afterActionOfDeleteChain() {
        String message;
        switch (editable_type) {
            case TYPE_RECORD:
                message = "Текущая запись и цепочка удалены";
                break;
            case TYPE_TEXT:
                message = "Текущий текст и цепочка удалены";
                break;
            default:
                message = "";
                break;
        }

        Toast toast = Toast.makeText(getApplicationContext(),
                message, Toast.LENGTH_SHORT);
        toast.show();

        goHome(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        workThread.quit();
    }

}