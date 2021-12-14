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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.HorizontalScrollView;

import java.util.ArrayList;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adbapp.FillingOfList.ListFilling;
import com.example.adbapp.FillingOfList.OverviewListFilling;
import com.example.adbapp.ItemTouchHelper.ItemTouchHelperAdapter;
import com.example.adbapp.ItemTouchHelper.SimpleItemTouchHelperCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    Button addButton;
    Button buttonLevelUp;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor objectCursor, recordCursor, recordCursor2;
    RecordAdapter recordAdapter;
    FloatingActionButton buttonFAB;
    HorizontalScrollView HScroll;
    SimpleItemTouchHelperCallback simpleItemTouchHelperCallback;

    OverviewListFilling overviewList;

    private static final String TAG = "**MainActivity**";

    ArrayList<Record> records = new ArrayList<>();
    ArrayList<Record> records_2 = new ArrayList<>();
    ArrayList<Integer> objLinkRecList = new ArrayList<>();
    ArrayList<Long> record_id = new ArrayList<>();
    ArrayList<Integer> objIdList = new ArrayList<>();
    ArrayList<Integer> parentIdByLevels = new ArrayList<>();

    boolean reqToFillRec;

    int cur_level=0, count_rec=0, i,j, curRecId=0, PosRecClick=0, selObjId=0;
    String indent;
    long count_childRec=0;


    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if(result.getResultCode() == Activity.RESULT_OK){
                        //Intent intent = result.getData();
                        //String accessMessage = intent.getStringExtra("");
                        //records.add(PosRecClick,new Record(100, "zdoroy", PosRecClick, 0));
                        //recordAdapter.notifyItemInserted(PosRecClick);
                        /*
                        recordCursor2 = db.rawQuery("select record_clusters._id, parent_id, _name, _time,field_id,object_id FROM " +
                                "record_clusters INNER JOIN field_clusters ON record_clusters.field_id=field_clusters._id " +
                                "INNER JOIN name_clusters ON field_clusters.name_id=name_clusters._id " +
                                "WHERE parent_id=?", new String[]{String.valueOf(parentIdByLevels.get(cur_level))});

                        recordCursor2.moveToLast();

                        records.add(new Record(recordCursor2.getInt(0), recordCursor2.getString(2), recordCursor2.getInt(1),0));

                        if(cur_level==0)
                            objIdList.add(recordCursor2.getInt(5));
                        */

                        overviewList.UpdateAfterAddNewRecords();

                        recordAdapter.notifyItemInserted(records.size());

                    }
                    //records.add(PosRecClick,new Record(100, "zdoroy", PosRecClick, 0));
                    //recordAdapter.notifyItemInserted(PosRecClick);


                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        buttonFAB = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        buttonLevelUp = findViewById(R.id.buttonLevelUp);
        RecyclerView recordList = (RecyclerView) findViewById(R.id.list);
        //HScroll =(HorizontalScrollView) findViewById(R.id.hscroll);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        buttonFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRecord(overviewList.get_ParentIdOfCurrentLevel());
            }
        });

        RecordAdapter.OnRecordClickListener recordClickListener = new RecordAdapter.OnRecordClickListener() {
            @Override
            public void onRecordClick(Record record, int position) {
                //addRecord(record.getRecord_id());
            }
        };

        overviewList = new OverviewListFilling(getApplicationContext());

        records = overviewList.records;

        recordAdapter = new RecordAdapter(this, records,1, recordClickListener);
        recordList.setAdapter(recordAdapter);

        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        recordList.setLayoutManager(layoutmanager);

        recordList.addItemDecoration(new RecordDecoration(overviewList.records));


        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Log.d(TAG, "onSwiped: ");

                overviewList.ActionDown(viewHolder.getAdapterPosition());

                recordAdapter.notifyDataSetChanged();
            }
        };

        ItemTouchHelper touchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        touchHelper.attachToRecyclerView(recordList);

        //ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(recordAdapter,);
        //ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        //touchHelper.attachToRecyclerView(recordList);

        reqToFillRec = true;

        // открываем подключение
        db = databaseHelper.getReadableDatabase();
    }

    @Override
    public void onResume() {
        super.onResume();

        if(reqToFillRec) {
            //fillingOfRecords(0);
            parentIdByLevels.add(0,0);
            //FillingZeroLevel();
            reqToFillRec = false;
        }

    }


    public void addRecord(int idRec){
        Intent intent = new Intent(getApplicationContext(), AddActivity.class);
        intent.putExtra("id", idRec);
        mStartForResult.launch(intent);

    }

    public void LevelUp(View view){
        overviewList.ToPreviousLevel();
        recordAdapter.notifyDataSetChanged();
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
        // Закрываем подключение и курсор
        db.close();
        recordCursor.close();
        objectCursor.close();
        overviewList.records.clear();

    }

}