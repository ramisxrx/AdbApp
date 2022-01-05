package com.example.adbapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adbapp.FillingOfList.AddingNewRecord;
import com.example.adbapp.FillingOfList.FoundListFilling;
import com.example.adbapp.RecordList.Record;
import com.example.adbapp.RecordList.RecordAdapter;
import com.example.adbapp.RecordList.RecordDecoration;

public class AddActivity extends AppCompatActivity {

    Button saveButton;
    ActionBar actionBar;
    Spinner spinner;
    FrameLayout frameLayout;

    RecordContainer recordContainer;
    TypeRecordAdapter typeRecordAdapter;
    AddRecordFragment addRecordFragment;
    AddTextFragment addTextFragment;
    FragmentTransaction fragmentTransaction;

    private static final String TAG = "**AddActivity**";

    private final String[] typesRecord = { "Запись", "Текст", "Дата" };

    AddingNewRecord addingNewRecord;

    int recordId, object_id, field_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        saveButton = (Button) findViewById(R.id.saveButton);
        spinner = findViewById(R.id.spinner);
        frameLayout = (FrameLayout) findViewById(R.id.container_parent);

        addRecordFragment = new AddRecordFragment();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.container_add,addRecordFragment);
        fragmentTransaction.commit();


        actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setSubtitle("Добавление записи");
        }

        recordContainer = new RecordContainer(getApplicationContext(),frameLayout);
        typeRecordAdapter = new TypeRecordAdapter(getApplicationContext(),R.layout.record_item,typesRecord);
        field_id = ContentView.TYPE_VIEW_1;

        spinner.setAdapter(typeRecordAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Ваш выбор: " + typesRecord[i], Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            recordContainer.FillingContainer(new Record(extras.getInt("record_id"),
                    extras.getString("name"),
                    extras.getInt("time"),
                    extras.getInt("field_type")), 1);

            object_id = extras.getInt("object_id");
        }
        else
            goHome(true);

        AddingNewRecord.NotifyViews_before notifyViews_before = new AddingNewRecord.NotifyViews_before() {
            @Override
            public void Save() {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Добавление новой записи...", Toast.LENGTH_SHORT);
                toast.show();
            }
        };

        AddingNewRecord.NotifyViews_after notifyViews_after = new AddingNewRecord.NotifyViews_after() {
            @Override
            public void Save() {
                goHome(!addingNewRecord.successAddingNewRecord);
            }
        };
        addingNewRecord = new AddingNewRecord(getApplicationContext(),object_id,recordContainer.getRecord().getRecord_id(),field_id,notifyViews_before,notifyViews_after);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.activity_add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goHome(true);
                return true;
            case R.id.item1:
                if(field_id!=ContentView.TYPE_VIEW_1){
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container_add,addRecordFragment);
                    fragmentTransaction.commit();
                    field_id = ContentView.TYPE_VIEW_1;
                    actionBar.setSubtitle("Добавление записи");
                }
                return true;
            case R.id.item2:
                if(field_id!=ContentView.TYPE_VIEW_2){
                    if(addTextFragment==null)
                        addTextFragment = new AddTextFragment();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container_add,addTextFragment);
                    fragmentTransaction.commit();
                    field_id = ContentView.TYPE_VIEW_2;
                    actionBar.setSubtitle("Добавление текста");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void save(View view){
        addingNewRecord.Save();
    }

    public void back(View view){
        goHome(true);
    }

    private void goHome(boolean cancel){

        Intent data = new Intent();
        data.putExtra("id of added record",recordId);
        if(cancel) {
            setResult(RESULT_CANCELED,data);
        }else {
            setResult(RESULT_OK, data);
        }
        addingNewRecord.Destroy();
        finish();
    }
}