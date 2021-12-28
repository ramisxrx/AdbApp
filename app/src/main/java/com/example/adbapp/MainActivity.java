package com.example.adbapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adbapp.FillingOfList.AssociativeListFilling;
import com.example.adbapp.FillingOfList.OverviewListFilling;
import com.example.adbapp.ItemTouchHelper.SimpleItemTouchHelperCallback;
import com.example.adbapp.RecordList.Record;
import com.example.adbapp.RecordList.RecordAdapter;
import com.example.adbapp.RecordList.RecordDecoration;
import com.example.adbapp.Threads.HandlerThreadOfFilling;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    TextView parentRec;
    Button buttonLevelUp;

    HandlerThreadOfFilling BG_Thread;

    RecordAdapter recordAdapter;
    FloatingActionButton buttonFAB;
    SimpleItemTouchHelperCallback simpleItemTouchHelperCallback;

    OverviewListFilling overviewList;
    AssociativeListFilling associativeList;

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

        parentRec = (TextView) findViewById(R.id.parentRec);
        buttonFAB = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        buttonLevelUp = findViewById(R.id.buttonLevelUp);
        RecyclerView recordList = (RecyclerView) findViewById(R.id.list);

        buttonFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRecord(overviewList.get_ParentIdOfCurrentLevel());
            }
        });

        BG_Thread = new HandlerThreadOfFilling("BG_MainActivity");

        RecordAdapter.OnRecordClickListener recordClickListener = (record, position) -> {
            //addRecord(record.getRecord_id());
        };
        RecordAdapter.OnRecordLongClickListener recordLongClickListener = new RecordAdapter.OnRecordLongClickListener() {
            @Override
            public void onRecordLongClick(int position) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Поиск ассоциаций...", Toast.LENGTH_SHORT);
                toast.show();
                associativeList.ActionOfInitialization(records.get(position).getRecord_id());
            }
        };

        AssociativeListFilling.NotifyViews_after associativeList_notifyViews_after = new AssociativeListFilling.NotifyViews_after() {
            @Override
            public void ActionOfInitialization() {
                if(associativeList.hasAssociations) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Ассоциации найдены!", Toast.LENGTH_SHORT);
                    toast.show();
                    associationMode = true;
                    SwitchingOfRecordList();
                    buttonLevelUp.setVisibility(View.GONE);
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
                parentRec.setText(overviewList.parentRecordsByLevel.get(overviewList.cur_level).getName());
                recordAdapter.notifyDataSetChanged();
                buttonLevelUp.setVisibility(View.VISIBLE);
            }

            @Override
            public void ToPreviousLevel() {
                parentRec.setText(overviewList.parentRecordsByLevel.get(overviewList.cur_level).getName());
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
        parentRec.setText(overviewList.parentRecordsByLevel.get(overviewList.cur_level).getName());
        records = overviewList.records;

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

        //ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(recordAdapter,);
        //ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        //touchHelper.attachToRecyclerView(recordList);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void SwitchingOfRecordList(){
        if(associationMode)
            recordAdapter.records = associativeList.records;
        else
            recordAdapter.records = overviewList.records;
        recordAdapter.notifyDataSetChanged();
    }

    public void addRecord(int idRec){
        Intent intent = new Intent(getApplicationContext(), AddActivity.class);
        intent.putExtra("id",idRec);
        mStartForResult.launch(intent);

    }

    public void LevelUp(View view){
        if(associationMode)
            associativeList.ToPreviousLevel();
        else
            overviewList.ToPreviousLevel();
        overviewList.bdView();
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