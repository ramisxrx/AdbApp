package com.example.adbapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.view.View;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.HorizontalScrollView;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    Button addButton;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor objectCursor, recordCursor;
    RecordAdapter recordAdapter;
    RecordAdapter.OnRecordClickListener recordClickListener;
    HorizontalScrollView HScroll;


    ArrayList<Record> records = new ArrayList<>();
    ArrayList<Integer> levels = new ArrayList<>();
    ArrayList<Long> record_id = new ArrayList<>();

    boolean reqToFillRec;

    int cur_level=0, count_rec=0, i,j, curRecId=0, PosRecClick=0;
    String indent;
    long count_childRec=0;


    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if(result.getResultCode() == Activity.RESULT_OK){
                        //Intent intent = result.getData();
                        //String accessMessage = intent.getStringExtra("");
                        records.add(PosRecClick,new Record(100, "zdoroy", PosRecClick, 0));
                        recordAdapter.notifyItemInserted(PosRecClick);
                    }
                    records.add(PosRecClick,new Record(100, "zdoroy", PosRecClick, 0));
                    recordAdapter.notifyItemInserted(PosRecClick);
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = (Button) findViewById(R.id.addButton);
        RecyclerView recordList = (RecyclerView) findViewById(R.id.list);
        HScroll =(HorizontalScrollView) findViewById(R.id.hscroll);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        recordClickListener = new RecordAdapter.OnRecordClickListener() {
            @Override
            public void onRecordClick(Record record, int position) {
                PosRecClick = record.getRecord_id();
               // PosRecClick = position;
                addRecord(PosRecClick);
            }
        };
        recordAdapter = new RecordAdapter(this, records, recordClickListener);
        recordList.setAdapter(recordAdapter);

        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        recordList.setLayoutManager(layoutmanager);

        recordList.addItemDecoration(new RecordDecoration(records));

        reqToFillRec = true;

    }

    @Override
    public void onResume() {
        super.onResume();

        if(reqToFillRec) {
            fillingOfRecords();
            reqToFillRec = false;
        }
    }

    public void fillingOfRecords(){

        // открываем подключение
        db = databaseHelper.getReadableDatabase();

        curRecId=-1;

        objectCursor = db.rawQuery("select _id FROM list_objects", null);
        while(objectCursor.moveToNext()) {
            recordCursor = db.rawQuery("select record_clusters._id, parent_id, _name, _time FROM " +
                    "record_clusters INNER JOIN field_clusters ON record_clusters.field_id=field_clusters._id " +
                    "INNER JOIN name_clusters ON field_clusters.name_id=name_clusters._id " +
                    "WHERE object_id=?", new String[]{objectCursor.getString(0)});

            curRecId=UncoverBranch(recordCursor,curRecId,0);
        }

    }

    public int UncoverBranch(Cursor cursor,int curRecListId,int parentRecId){
        ArrayList<Integer> levels = new ArrayList<>();
        int cur_level=0;
        levels.add(cur_level, parentRecId);

        while (cur_level>-1){

            if(Record.CursorMatchFound_1(cursor,1,0,records,levels.get(cur_level))){

                records.add(new Record(cursor.getInt(0), cursor.getString(2), cursor.getInt(1), cur_level));
                curRecListId = curRecListId + 1;

                cur_level = cur_level + 1;
                levels.add(cur_level,cursor.getInt(0));

                records.get(curRecListId).setHasChildRec(Record.CursorMatchFound_2(cursor,1,records.get(curRecListId).getRecord_id()));
                count_rec = count_rec - 1;
            }else{
                levels.remove(cur_level);
                cur_level = cur_level-1;
            }

        }
    return curRecListId;
    }

    public void addRecord(int idRec){
        Intent intent = new Intent(getApplicationContext(), AddActivity.class);
        intent.putExtra("id", idRec);
        mStartForResult.launch(intent);


       // records.add(PosRecClick+1,new Record(100, "zdoroy", PosRecClick, records.get(PosRecClick).getLevel()+1));
       // recordAdapter.notifyItemInserted(PosRecClick+1);
       // HScroll.computeScroll();
    }


    public void addObject(View view){
        addRecord(-1);
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