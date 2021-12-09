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

                        recordCursor2 = db.rawQuery("select record_clusters._id, parent_id, _name, _time,field_id,object_id FROM " +
                                "record_clusters INNER JOIN field_clusters ON record_clusters.field_id=field_clusters._id " +
                                "INNER JOIN name_clusters ON field_clusters.name_id=name_clusters._id " +
                                "WHERE parent_id=?", new String[]{String.valueOf(parentIdByLevels.get(cur_level))});

                        recordCursor2.moveToLast();

                        records.add(new Record(recordCursor2.getInt(0), recordCursor2.getString(2), recordCursor2.getInt(1)));

                        if(cur_level==0)
                            objIdList.add(recordCursor2.getInt(5));

                        recordAdapter.notifyItemInserted(records.size());

                        recordCursor2.close();
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
                addRecord(parentIdByLevels.get(cur_level));
            }
        });

        RecordAdapter.OnRecordClickListener recordClickListener = new RecordAdapter.OnRecordClickListener() {
            @Override
            public void onRecordClick(Record record, int position) {
                //addRecord(record.getRecord_id());
            }
        };

        recordAdapter = new RecordAdapter(this, records,1, recordClickListener);
        recordList.setAdapter(recordAdapter);

        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        recordList.setLayoutManager(layoutmanager);

        recordList.addItemDecoration(new RecordDecoration(records));


        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Log.d(TAG, "onSwiped: ");

                if(selObjId==0){
                    selObjId = objIdList.get(viewHolder.getAdapterPosition());

                    recordCursor = db.rawQuery("select record_clusters._id, parent_id, _name, _time,field_id FROM " +
                            "record_clusters INNER JOIN field_clusters ON record_clusters.field_id=field_clusters._id " +
                            "INNER JOIN name_clusters ON field_clusters.name_id=name_clusters._id " +
                            "WHERE object_id=?", new String[]{String.valueOf(selObjId)});
                }

                cur_level++;
                parentIdByLevels.add(cur_level,records.get(viewHolder.getAdapterPosition()).getRecord_id());

                FillingOtherLevel(records.get(viewHolder.getAdapterPosition()).getRecord_id());

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
            FillingZeroLevel();
            reqToFillRec = false;
        }

        //HScroll.computeScroll();
    }

    public void FillingZeroLevel(){

        Cursor cursor = databaseHelper.getRecords(0);

        int i=0;

        selObjId = 0;

        records.clear();
        objIdList.clear();

        while (cursor.moveToNext()) {
            records.add(new Record(cursor.getInt(0), cursor.getString(2), cursor.getInt(1)));
            records.get(i).setParent_id(0);
            i++;

            objIdList.add(cursor.getInt(4));

            Log.d(TAG, "FillingZeroLevel: record_id="+cursor.getString(0));
            Log.d(TAG, "FillingZeroLevel: field_id="+cursor.getString(5));
        }

        Log.d(TAG, "FillingZeroLevel: cur_level="+String.valueOf(cur_level));

        cursor.close();

        buttonLevelUp.setVisibility(View.GONE);
    }

    public void FillingOtherLevel(int parentId){

        int k=0;

        Log.d(TAG, "FillingOtherLevel: parent_id="+String.valueOf(parentIdByLevels.get(cur_level)));

        records.clear();

        for(int i=0;i<recordCursor.getCount();i++){

            recordCursor.moveToPosition(i);

            if(recordCursor.getInt(1)==parentId) {
                records.add(new Record(recordCursor.getInt(0), recordCursor.getString(2), recordCursor.getInt(1)));
                records.get(k).setParent_id(parentId);
                k++;

                Log.d(TAG, "FillingOtherLevel: record_id="+recordCursor.getString(0));
                Log.d(TAG, "FillingOtherLevel: field_id="+recordCursor.getString(4));
            }
        }

        Log.d(TAG, "FillingOtherLevel: cur_level="+String.valueOf(cur_level));

        buttonLevelUp.setVisibility(View.VISIBLE);
    }


    public void addRecord(int idRec){
        Intent intent = new Intent(getApplicationContext(), AddActivity.class);
        intent.putExtra("id", idRec);
        mStartForResult.launch(intent);

    }

    public void PreviousLevel(){

        Log.d(TAG, "PreviousLevel: cur_level="+String.valueOf(cur_level));

        if(cur_level>0) {

            parentIdByLevels.remove(cur_level);
            cur_level--;

            Log.d(TAG, "PreviousLevel: cur_level="+String.valueOf(cur_level));

            if (cur_level == 0)
                FillingZeroLevel();
            else
                FillingOtherLevel(parentIdByLevels.get(cur_level));


            recordAdapter.notifyDataSetChanged();
        }


    }

    public void LevelUp(View view){
        PreviousLevel();
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        if(cur_level==0){
            finish();
        }else
            PreviousLevel();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        recordCursor.close();
        objectCursor.close();

    }

}