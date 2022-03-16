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
import android.widget.Toast;

import com.example.adbapp.AddingRecord.AddDateFragment;
import com.example.adbapp.AddingRecord.AddPhotoFragment;
import com.example.adbapp.AddingRecord.AddTimeFragment;
import com.example.adbapp.Container.ContainerRecord;
import com.example.adbapp.Container.FactoryParentRecord;
import com.example.adbapp.AddingRecord.AddingNewRecord;
import com.example.adbapp.AddingRecord.AddRecordFragment;
import com.example.adbapp.AddingRecord.AddTextFragment;
import com.example.adbapp.PopupMenuOfRecord.CallPopupMenuContainer;
import com.example.adbapp.RecordList.Record;

public class AddActivity extends AppCompatActivity implements CallPopupMenuContainer {

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
        frameLayout = (FrameLayout) findViewById(R.id.container_parent);

        addRecordFragment = new AddRecordFragment(ContentView.TYPE_RECORD);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_add,addRecordFragment);
        fragmentTransaction.commit();

        actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setSubtitle("Добавление записи");
        }

        factoryParentRecord = new FactoryParentRecord(getApplicationContext(),frameLayout,this);
        factoryParentRecord.setVisibleImageButton(false);

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
                String message = "";
                switch (addingNewRecord.getTypeContent()){
                    case ContentView.TYPE_RECORD:
                        message = "Добавление новой записи...";
                        break;
                    case ContentView.TYPE_TEXT:
                        message = "Добавление нового текста...";
                        break;
                    case ContentView.TYPE_DATE:
                        message = "Добавление новой даты...";
                        break;
                    case ContentView.TYPE_TIME:
                        message = "Добавление нового времени...";
                        break;
                    default:
                        break;
                }
                Toast toast = Toast.makeText(getApplicationContext(),
                        message, Toast.LENGTH_SHORT);
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
        addingNewRecord.setParametersToAdd(addRecordFragment.field_id_ToAdd,addRecordFragment.name_ToAdd);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addingNewRecord.getTypeContent()==ContentView.TYPE_RECORD ||
                    addingNewRecord.getTypeContent()==ContentView.TYPE_DATE ||
                    addingNewRecord.getTypeContent()==ContentView.TYPE_TIME ||
                    addingNewRecord.getTypeContent()==ContentView.TYPE_PHOTO)

                    addingNewRecord.setParametersToAdd(addRecordFragment.field_id_ToAdd,addRecordFragment.name_ToAdd);

                else
                    addingNewRecord.setParametersToAdd(addTextFragment.field_id_ToAdd,addTextFragment.name_ToAdd);

                addingNewRecord.Save();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.activity_add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (item.getItemId()) {
            case android.R.id.home:
                goHome(true);
                return true;
            case R.id.record:
                if(addingNewRecord.getTypeContent()!=ContentView.TYPE_RECORD){
                    //fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    addRecordFragment = new AddRecordFragment(ContentView.TYPE_RECORD);
                    fragmentTransaction.replace(R.id.container_add,addRecordFragment);
                    fragmentTransaction.commit();
                    addingNewRecord.setTypeContent(ContentView.TYPE_RECORD);
                    actionBar.setSubtitle("Добавление записи");
                    saveButton.setEnabled(false);
                }
                return true;
            case R.id.text:
                if(addingNewRecord.getTypeContent()!=ContentView.TYPE_TEXT){
                    addTextFragment = new AddTextFragment();
                    //fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.container_add,addTextFragment);
                    fragmentTransaction.commit();
                    addingNewRecord.setTypeContent(ContentView.TYPE_TEXT);
                    actionBar.setSubtitle("Добавление текста");
                    saveButton.setEnabled(false);
                    if(addRecordFragment!=null) {
                        addRecordFragment.onDestroy();
                        addRecordFragment = null;
                    }
                }
                return true;
            case R.id.date:
                if(addingNewRecord.getTypeContent()!=ContentView.TYPE_DATE){
                    if(addTextFragment!=null)
                        addTextFragment = null;
                    //fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    addRecordFragment = new AddDateFragment(ContentView.TYPE_DATE);
                    fragmentTransaction.replace(R.id.container_add,addRecordFragment);
                    fragmentTransaction.commit();
                    addingNewRecord.setTypeContent(ContentView.TYPE_DATE);
                    actionBar.setSubtitle("Добавление даты");
                    saveButton.setEnabled(false);
                }
                return true;
            case R.id.time:
                if(addingNewRecord.getTypeContent()!=ContentView.TYPE_TIME){
                    if(addTextFragment!=null)
                        addTextFragment = null;
                    //fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    addRecordFragment = new AddTimeFragment(ContentView.TYPE_TIME);
                    fragmentTransaction.replace(R.id.container_add,addRecordFragment);
                    fragmentTransaction.commit();
                    addingNewRecord.setTypeContent(ContentView.TYPE_TIME);
                    actionBar.setSubtitle("Добавление времени");
                    saveButton.setEnabled(false);
                }
                return true;
            case R.id.photo:
                if(addingNewRecord.getTypeContent()!=ContentView.TYPE_PHOTO){
                    if(addTextFragment!=null)
                        addTextFragment = null;
                    //fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    addRecordFragment = new AddPhotoFragment(ContentView.TYPE_PHOTO);
                    fragmentTransaction.replace(R.id.container_add,addRecordFragment);
                    fragmentTransaction.commit();
                    addingNewRecord.setTypeContent(ContentView.TYPE_PHOTO);
                    actionBar.setSubtitle("Добавление фото");
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

    @Override
    public void callPopupMenuContainer(View view) {

    }
}