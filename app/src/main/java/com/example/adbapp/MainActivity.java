package com.example.adbapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adbapp.FillingOfList.AssociativeListFilling;
import com.example.adbapp.FillingOfList.OverviewListFilling;
import com.example.adbapp.Fragments.AddTextFragment;
import com.example.adbapp.Fragments.AssociationsFragment;
import com.example.adbapp.Fragments.OverviewFragment;
import com.example.adbapp.Fragments.RecordContainer;
import com.example.adbapp.Interfaces.Associations_ActionsOfActivity;
import com.example.adbapp.ItemTouchHelper.SimpleItemTouchHelperCallback;
import com.example.adbapp.PopupMenuOfRecord.ActionsPopupMenu;
import com.example.adbapp.RecordList.Record;
import com.example.adbapp.RecordList.RecordAdapter;
import com.example.adbapp.RecordList.RecordDecoration;
import com.example.adbapp.Search.SearchFragment;
import com.example.adbapp.Threads.HandlerThreadOfFilling;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements ActionsPopupMenu, Associations_ActionsOfActivity {

    ActionBar actionBar;

    FloatingActionButton buttonFAB;

    OverviewFragment overviewFragment;
    AssociationsFragment associationsFragment;
    SearchFragment searchFragment;
    FragmentTransaction fragmentTransaction;

    private MenuItem menuItemSearch, menuItemRecord,menuItemText,menuItemHome;
    private SearchView searchView;
    private int searchContent;

    private static final String TAG = "**MainActivity**";

    boolean associationMode=false, searchMode=false;

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        String activity = intent.getStringExtra("Activity");
                        if(activity.equals("Add")) {
                            if (!associationMode)
                                overviewFragment.overviewList.UpdateAfterAddNewRecords();
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonFAB = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        OverviewFragment.ActionsOfActivity overview_ActionsOfActivity = new OverviewFragment.ActionsOfActivity() {
            @Override
            public void CheckingAssociations(int record_id) {
                //Toast toast = Toast.makeText(getApplicationContext(),
                //        "Callback: associationsFragment.associativeList.ActionOfInitialization(record_id)", Toast.LENGTH_SHORT);
                //toast.show();
                //associationsFragment = new AssociationsFragment(getApplicationContext(),associations_ActionsOfActivity);
                //associationsFragment.associativeList.ActionOfInitialization(record_id);
            }

            @Override
            public void SwitchingToEditing(Record record) {
                //editRecord(record,0);
            }
        };

        overviewFragment = new OverviewFragment(overview_ActionsOfActivity,buttonFAB,this);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.container_main,overviewFragment);
        fragmentTransaction.commit();

        buttonFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(associationMode){
                    addRecord(associationsFragment.parentContainer.getRecord(),associationsFragment.associativeList.selObjId);
                }else{
                    addRecord(overviewFragment.parentContainer.getRecord(),overviewFragment.overviewList.selObjId);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_menu,menu);

        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                Log.d(TAG, "onMenuItemActionExpand: ");
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                menuItemSearch.setVisible(true);
                menuItemRecord.setVisible(true);
                menuItemText.setVisible(true);

                Log.d(TAG, "onMenuItemActionCollapse: ");
                return true;
            }
        };

        menu.findItem(R.id.search).setOnActionExpandListener(onActionExpandListener);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        menuItemHome = menu.findItem(R.id.home);
        menuItemSearch = menu.findItem(R.id.search);
        menuItemRecord = menu.findItem(R.id.record);
        menuItemText = menu.findItem(R.id.text);

        menuItemSearch.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(associationMode)
                    SwitchingMode(false,searchMode);
                else
                    SwitchingMode(false,false);

                Log.d(TAG, "onOptionsItemSelected: R.id.home");
                return true;

            case R.id.search:
                //menuItemSearch.setVisible(false);
                menuItemRecord.setVisible(false);
                menuItemText.setVisible(false);
                Log.d(TAG, "onOptionsItemSelected: R.id.search");
                return true;

            case R.id.record:
                searchContent = ContentView.TYPE_RECORD;
                SwitchingMode(false,true);
                return true;
            case R.id.text:
                searchContent = ContentView.TYPE_TEXT;
                SwitchingMode(false,true);
                Log.d(TAG, "onOptionsItemSelected: R.id.text");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void SwitchingMode(boolean associationMode, boolean searchMode){
        int stateMode=0;

        if(!associationMode && searchMode)
            stateMode = 1;
        if(associationMode && !searchMode)
            stateMode = 2;
        if(associationMode && searchMode)
            stateMode = 3;

        Log.d(TAG, "SwitchingMode: stateMode="+String.valueOf(stateMode));

        switch (stateMode){
            case 0: // overview mode
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setSubtitle("");
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container_main,overviewFragment);
                fragmentTransaction.commit();
                menuItemSearch.setVisible(false);
                menuItemRecord.setVisible(true);
                menuItemText.setVisible(true);
                if(this.associationMode){
                    associationsFragment.onDestroy();
                    associationsFragment = null;
                }
                break;
            case 1: // search mode
                actionBar.setDisplayHomeAsUpEnabled(true);
                searchFragment = new SearchFragment();
                switch (searchContent){
                    case ContentView.TYPE_RECORD:
                        actionBar.setSubtitle("Выборка записей");
                        menuItemSearch.setVisible(true);
                        searchView.setQueryHint("Введите запись");
                        break;
                    case ContentView.TYPE_TEXT:
                        actionBar.setSubtitle("Выборка текстов");
                        menuItemSearch.setVisible(false);
                        break;
                }

                if(this.associationMode){
                    menuItemSearch.setVisible(true);
                    menuItemRecord.setVisible(true);
                    menuItemText.setVisible(true);
                }
                break;
            default:
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setSubtitle("Режим ассоциаций");
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container_main,associationsFragment);
                fragmentTransaction.commit();

                menuItemSearch.setVisible(false);
                menuItemRecord.setVisible(false);
                menuItemText.setVisible(false);
                break;
        }

        //fragmentTransaction.commit();

        this.associationMode = associationMode;
        this.searchMode = searchMode;
    }


    public void addRecord(Record record, int object_id){
        Intent intent = new Intent(getApplicationContext(), AddActivity.class);
        intent.putExtra("record_id",record.getRecord_id());
        intent.putExtra("name",record.getName());
        intent.putExtra("time",record.getTime());
        intent.putExtra("field_type",record.getField_type());
        intent.putExtra("object_id",object_id);
        mStartForResult.launch(intent);
    }

    public void editRecord(Record record, int object_id){
        Intent intent = new Intent(getApplicationContext(), EditActivity.class);
        intent.putExtra("record_id",record.getRecord_id());
        intent.putExtra("name",record.getName());
        intent.putExtra("time",record.getTime());
        intent.putExtra("field_type",record.getField_type());
        intent.putExtra("object_id",object_id);
        mStartForResult.launch(intent);
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        //if(overviewList.cur_level==0){
        //    finish();
        //}else
        //    overviewList.ToPreviousLevel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //records.clear();
        //overviewList.Destroy();
    }

    @Override
    public void CheckingAssociations(Record record) {
        associationsFragment = new AssociationsFragment(getApplicationContext(),this,this);
        associationsFragment.associativeList.ActionOfInitialization(record.getRecord_id());
    }

    @Override
    public void SwitchingToAssociationsMode() {
        if(!associationMode) {
            SwitchingMode(true,searchMode);
            Log.d(TAG, "AssociationsFragment.ActionsOfActivity: ");
        }
    }

    @Override
    public void SwitchingToEditing(Record record) {
        editRecord(record,0);
    }
}