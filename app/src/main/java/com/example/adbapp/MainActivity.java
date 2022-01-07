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

    Button buttonLevelUp;
    ActionBar actionBar;
    HandlerThreadOfFilling BG_Thread;

    FrameLayout frameLayout;
    RecordContainer recordContainer;

    RecordAdapter recordAdapter;
    FloatingActionButton buttonFAB;

    OverviewListFilling overviewList;
    AssociativeListFilling associativeList;


    OverviewFragment overviewFragment;
    AssociationsFragment associationsFragment;
    FragmentTransaction fragmentTransaction;

    private static final String TAG = "**MainActivity**";

    boolean associationMode;
    ArrayList<Record> records = new ArrayList<>();

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if(result.getResultCode() == Activity.RESULT_OK){
                        overviewList.UpdateAfterAddNewRecords();
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

        OverviewFragment.ActionsOfActivity actionsOfActivity = new OverviewFragment.ActionsOfActivity() {
            @Override
            public void SwitchingToAssociationsMode() {
                SwitchingToAssociationsMode_2(true);
            }
        };

        overviewFragment = new OverviewFragment(actionsOfActivity);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.container_main,overviewFragment);
        fragmentTransaction.commit();

        /*
        frameLayout = (FrameLayout) findViewById(R.id.container_parent);
        buttonFAB = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        buttonLevelUp = findViewById(R.id.buttonLevelUp);
        RecyclerView recordList = (RecyclerView) findViewById(R.id.list);
        recordContainer = new RecordContainer(getApplicationContext(),frameLayout);

        buttonFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRecord(recordContainer.getRecord());
            }
        });

        BG_Thread = new HandlerThreadOfFilling("BG_MainActivity");

        RecordAdapter.OnRecordClickListener recordClickListener = (record, position) -> {
            //addRecord(record.getRecord_id());
        };
        RecordAdapter.OnRecordLongClickListener recordLongClickListener = new RecordAdapter.OnRecordLongClickListener() {
            @Override
            public void onRecordLongClick(int position) {
                if(associationMode) {
                    if(associativeList.cur_level!=0)
                        associativeList.ActionOfInitialization(associativeList.records.get(position).getRecord_id());
                }else
                    associativeList.ActionOfInitialization(overviewList.records.get(position).getRecord_id());
            }
        };

        AssociativeListFilling.NotifyViews_after associativeList_notifyViews_after = new AssociativeListFilling.NotifyViews_after() {
            @Override
            public void ActionOfInitialization() {
                if(associativeList.hasAssociations) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Ассоциации найдены!", Toast.LENGTH_SHORT);
                    toast.show();
                    SwitchingOfRecordList(true);
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Ассоциации НЕ найдены!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void ActionDown() {
                recordAdapter.notifyDataSetChanged();
                buttonLevelUp.setVisibility(View.VISIBLE);
            }

            @Override
            public void ActionUp() {
                recordAdapter.notifyDataSetChanged();
                buttonLevelUp.setVisibility(View.VISIBLE);
            }

            @Override
            public void ToPreviousLevel() {
                recordAdapter.notifyDataSetChanged();
                if(associativeList.cur_level==0)
                    buttonLevelUp.setVisibility(View.GONE);
            }
        };

        associativeList = new AssociativeListFilling(getApplicationContext(),BG_Thread,associativeList_notifyViews_after);

        OverviewListFilling.NotifyViews_after notifyViews_after = new OverviewListFilling.NotifyViews_after() {
            @Override
            public void ActionDown() {
                recordContainer.FillingContainer(overviewList.parentRecordByLevel.get(overviewList.cur_level),1);
                recordAdapter.notifyDataSetChanged();
                buttonLevelUp.setVisibility(View.VISIBLE);
            }

            @Override
            public void ToPreviousLevel() {
                recordContainer.FillingContainer(overviewList.parentRecordByLevel.get(overviewList.cur_level),1);
                recordAdapter.notifyDataSetChanged();
                if(overviewList.cur_level==0)
                    buttonLevelUp.setVisibility(View.GONE);
            }

            @Override
            public void UpdateAfterAddNewRecords() {
                recordAdapter.notifyItemInserted(records.size());
            }
        };

        overviewList = new OverviewListFilling(getApplicationContext(),BG_Thread,notifyViews_after);
        records = overviewList.records;
        recordContainer.FillingContainer(overviewList.parentRecordByLevel.get(overviewList.cur_level),0);
        recordAdapter = new RecordAdapter(this, records,1, recordClickListener,recordLongClickListener);
        recordList.setAdapter(recordAdapter);

        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        recordList.setLayoutManager(layoutmanager);

        recordList.addItemDecoration(new RecordDecoration(overviewList.records));


        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public int getMovementFlags (RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int swipeFlags = 0;
                int dragFlags = 0;
                if(associationMode){
                    if(associativeList.cur_level==0)
                        swipeFlags = ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;
                    else{
                        if(associativeList.selDirection)
                            swipeFlags = ItemTouchHelper.RIGHT;
                        else
                            swipeFlags = ItemTouchHelper.LEFT;
                    }
                }else
                    swipeFlags = ItemTouchHelper.LEFT;

                return makeMovementFlags(dragFlags,swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Log.d(TAG, "onSwiped: ");
                if(associationMode){
                    if(direction==4)
                        associativeList.ActionDown(viewHolder.getAdapterPosition());
                    else
                        associativeList.ActionUp(viewHolder.getAdapterPosition());
                }else
                    overviewList.ActionDown(viewHolder.getAdapterPosition());

                //recordAdapter.notifyDataSetChanged();
            }
        };

        ItemTouchHelper touchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        touchHelper.attachToRecyclerView(recordList);


         */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                SwitchingToAssociationsMode_2(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void SwitchingToAssociationsMode_2(boolean associationMode){
        this.associationMode = associationMode;
        if(associationMode) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setSubtitle("Режим ассоциаций");
            if(associationsFragment==null)
                associationsFragment = new AssociationsFragment();
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container_main,associationsFragment);
        }
        else {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setSubtitle("");
        }
        recordAdapter.notifyDataSetChanged();
    }

    public void addRecord(Record record){
        Intent intent = new Intent(getApplicationContext(), AddActivity.class);
        intent.putExtra("record_id",record.getRecord_id());
        intent.putExtra("name",record.getName());
        intent.putExtra("time",record.getTime());
        intent.putExtra("field_type",record.getField_type());
        intent.putExtra("object_id",overviewList.selObjId);
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
        if(overviewList.cur_level==0){
            finish();
        }else
            overviewList.ToPreviousLevel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        records.clear();
        overviewList.Destroy();
    }
}