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
    ArrayList<Long> levels = new ArrayList<>();
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
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = (Button) findViewById(R.id.addButton);
        RecyclerView recordList = (RecyclerView) findViewById(R.id.list);
        HScroll =(HorizontalScrollView) findViewById(R.id.hscroll);

        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        recordClickListener = new RecordAdapter.OnRecordClickListener() {
            @Override
            public void onRecordClick(Record record, int position) {
                //PosRecClick = record.getRecord_id();
                PosRecClick = position;
                addRecord(PosRecClick);
            }
        };
        recordAdapter = new RecordAdapter(this, records, recordClickListener);
        recordList.setAdapter(recordAdapter);

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

        objectCursor = db.rawQuery("select _id FROM list_objects", null);
        while(objectCursor.moveToNext()) {
            recordCursor = db.rawQuery("select record_clusters._id, parent_id, _name, _time FROM " +
                    "record_clusters INNER JOIN field_clusters ON record_clusters.field_id=field_clusters._id " +
                    "INNER JOIN name_clusters ON field_clusters.name_id=name_clusters._id " +
                    "WHERE object_id=?", new String[]{objectCursor.getString(0)});

            count_rec = recordCursor.getCount();

            curRecId = -1;

            levels.clear();
            cur_level=0;
            levels.add(Long.valueOf(0));

            while (cur_level>-1){

                for(i=0;i<recordCursor.getCount();i++){
                    recordCursor.moveToPosition(i);
                    if(recordCursor.getInt(1) == levels.get(cur_level) && RecIdMatchesNotFound(records,recordCursor.getInt(0))) {

                        levels.add(recordCursor.getLong(0));
                        cur_level = cur_level + 1;

                        //records.add(new Record(recordCursor.getInt(0), recordCursor.getString(2), recordCursor.getInt(3), cur_level-1));
                        records.add(new Record(recordCursor.getInt(0), recordCursor.getString(2), recordCursor.getInt(1), cur_level-1));
                        curRecId = curRecId + 1;

                        records.get(curRecId).setHasChildRec(RecHasChildRec(recordCursor,records.get(curRecId).getRecord_id(),1));
                        count_rec = count_rec - 1;
                        break;
                    }
                }
                if(i==recordCursor.getCount()){
                    levels.remove(cur_level);
                    cur_level = cur_level-1;
                }

            }
        }

    }

    public boolean RecIdMatchesNotFound (ArrayList<Record> records, int idToCheck){
        for(int i=0; i<records.size();i++){
            if (records.get(i).getRecord_id() == idToCheck)
                return false;
        }
        return true;
    }

    public boolean RecHasChildRec(Cursor cursor, int RecId, int columnIndex){
        while(cursor.moveToNext()){
            if(cursor.getInt(columnIndex)==RecId)
                return true;
        }
        return false;
    }

    public void addRecord(int idRec){
     //   Intent intent = new Intent(getApplicationContext(), AddActivity.class);
      //  intent.putExtra("id", idRec);
      //  mStartForResult.launch(intent);


        records.add(PosRecClick+1,new Record(100, "zdoroy", PosRecClick, records.get(PosRecClick).getLevel()+1));
        recordAdapter.notifyItemInserted(PosRecClick+1);
        HScroll.computeScroll();
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