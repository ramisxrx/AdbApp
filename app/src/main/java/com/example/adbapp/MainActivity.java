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
import android.view.MenuItem;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adbapp.FillingOfList.AssociativeListFilling;
import com.example.adbapp.FillingOfList.OverviewListFilling;
import com.example.adbapp.Fragments.AssociationsFragment;
import com.example.adbapp.Fragments.OverviewFragment;
import com.example.adbapp.Fragments.RecordContainer;
import com.example.adbapp.ItemTouchHelper.SimpleItemTouchHelperCallback;
import com.example.adbapp.RecordList.Record;
import com.example.adbapp.RecordList.RecordAdapter;
import com.example.adbapp.RecordList.RecordDecoration;
import com.example.adbapp.Threads.HandlerThreadOfFilling;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    ActionBar actionBar;

    FloatingActionButton buttonFAB;

    OverviewFragment overviewFragment;
    AssociationsFragment associationsFragment;
    FragmentTransaction fragmentTransaction;

    private static final String TAG = "**MainActivity**";

    boolean associationMode=false;

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if(result.getResultCode() == Activity.RESULT_OK){
                        if(!associationMode)
                            overviewFragment.overviewList.UpdateAfterAddNewRecords();
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Новая запись добавлена", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        AssociationsFragment.ActionsOfActivity associations_ActionsOfActivity = new AssociationsFragment.ActionsOfActivity() {
            @Override
            public void SwitchingToAssociationsMode() {
                if(!associationMode) {
                    SwitchingMode(true);
                    Log.d(TAG, "AssociationsFragment.ActionsOfActivity: ");
                }
            }
        };

        OverviewFragment.ActionsOfActivity overview_ActionsOfActivity = new OverviewFragment.ActionsOfActivity() {
            @Override
            public void CheckingAssociations(int record_id) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Callback: associationsFragment.associativeList.ActionOfInitialization(record_id)", Toast.LENGTH_SHORT);
                toast.show();
                associationsFragment = new AssociationsFragment(getApplicationContext(),associations_ActionsOfActivity);
                associationsFragment.associativeList.ActionOfInitialization(record_id);
            }
        };

        overviewFragment = new OverviewFragment(overview_ActionsOfActivity);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.container_main,overviewFragment);
        fragmentTransaction.commit();

        buttonFAB = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        buttonFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(associationMode){
                    addRecord(associationsFragment.recordContainer.getRecord(),associationsFragment.associativeList.selObjId);
                }else{
                    addRecord(overviewFragment.recordContainer.getRecord(),overviewFragment.overviewList.selObjId);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                SwitchingMode(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void SwitchingMode(boolean associationMode){
        this.associationMode = associationMode;
        if(associationMode) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setSubtitle("Режим ассоциаций");
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container_main,associationsFragment);
        }
        else {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setSubtitle("");
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container_main,overviewFragment);
            associationsFragment.onDestroy();
            associationsFragment = null;
        }
        fragmentTransaction.commit();
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

    public void LevelUp(View view){
        //if(associationMode)
        //    associativeList.ToPreviousLevel();
        //else
        //    overviewList.ToPreviousLevel();
        //overviewList.bdView();
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
}