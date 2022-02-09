package com.example.adbapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.adbapp.Container.ContainerRecord;
import com.example.adbapp.Container.FactoryParentRecord;
import com.example.adbapp.FillingOfList.AddingNewRecord;
import com.example.adbapp.Fragments.AddRecordFragment;
import com.example.adbapp.Fragments.AddTextFragment;
import com.example.adbapp.Fragments.RecordContainer;
import com.example.adbapp.RecordList.Record;

public class AddActivity extends AppCompatActivity {

    Button saveButton;
    ActionBar actionBar;
    FrameLayout frameLayout;

    AddRecordFragment addRecordFragment;
    AddTextFragment addTextFragment;
    FragmentTransaction fragmentTransaction;
    public FactoryParentRecord factoryParentRecord;
    public ContainerRecord parentContainer;

    private static final String TAG = "**AddActivity**";

    AddingNewRecord addingNewRecord;

    int recordId, object_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        saveButton = (Button) findViewById(R.id.saveButton);
        //spinner = findViewById(R.id.spinner);
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

        factoryParentRecord = new FactoryParentRecord(getApplicationContext(),frameLayout);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            parentContainer = factoryParentRecord.createInitialContainer(new Record(extras.getInt("record_id"),
                    extras.getString("name"),
                    extras.getLong("time"),
                    extras.getInt("field_type")));

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
        addingNewRecord = new AddingNewRecord(getApplicationContext(),object_id,parentContainer.getRecord().getRecord_id(),notifyViews_before,notifyViews_after);
        addingNewRecord.setTypeContent(ContentView.TYPE_RECORD);
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
                if(addingNewRecord.getTypeContent()!=ContentView.TYPE_RECORD){
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container_add,addRecordFragment);
                    fragmentTransaction.commit();
                    addingNewRecord.setTypeContent(ContentView.TYPE_RECORD);
                    actionBar.setSubtitle("Добавление записи");
                    saveButton.setEnabled(false);
                }
                return true;
            case R.id.item2:
                if(addingNewRecord.getTypeContent()!=ContentView.TYPE_TEXT){
                    if(addTextFragment==null)
                        addTextFragment = new AddTextFragment();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container_add,addTextFragment);
                    fragmentTransaction.commit();
                    addingNewRecord.setTypeContent(ContentView.TYPE_TEXT);
                    actionBar.setSubtitle("Добавление текста");
                    saveButton.setEnabled(false);
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
        switch (addingNewRecord.getTypeContent()){
            case ContentView.TYPE_RECORD:
                addingNewRecord.setParametersToAdd(addRecordFragment.field_id_ToAdd,addRecordFragment.name_ToAdd);
                break;
            case ContentView.TYPE_TEXT:
                addingNewRecord.setParametersToAdd(addTextFragment.field_id_ToAdd,addTextFragment.name_ToAdd);
                break;
            default:
                break;
        }


        addingNewRecord.Save();
    }

    public void back(View view){
        goHome(true);
    }

    private void goHome(boolean cancel){

        Intent data = new Intent();
        data.putExtra("id of added record",recordId);
        data.putExtra("Activity","Add");
        if(cancel) {
            setResult(RESULT_CANCELED,data);
        }else {
            setResult(RESULT_OK, data);
        }
        addingNewRecord.Destroy();
        finish();
    }
}