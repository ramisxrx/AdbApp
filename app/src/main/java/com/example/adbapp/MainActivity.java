package com.example.adbapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.HorizontalScrollView;

import java.util.ArrayList;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    Button addButton;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor objectCursor, recordCursor;
    RecordAdapter recordAdapter;
    HorizontalScrollView HScroll;


    ArrayList<Record> records = new ArrayList<>();
    ArrayList<Record> records_2 = new ArrayList<>();
    ArrayList<Integer> objLinkRecList = new ArrayList<>();
    ArrayList<Long> record_id = new ArrayList<>();

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
                    }
                    //records.add(PosRecClick,new Record(100, "zdoroy", PosRecClick, 0));
                    //recordAdapter.notifyItemInserted(PosRecClick);
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = (Button) findViewById(R.id.addButton);
        RecyclerView recordList = (RecyclerView) findViewById(R.id.list);
        //HScroll =(HorizontalScrollView) findViewById(R.id.hscroll);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        RecordAdapter.OnRecordClickListener recordClickListener = new RecordAdapter.OnRecordClickListener() {
            @Override
            public void onRecordClick(Record record, int position) {

                //PosRecClick = record.getRecord_id();
               // PosRecClick = position;
                //addRecord(PosRecClick);
                /*
                if(position==0 && record.getParent_id()!=0){
                    records.clear();

                    if(record.getParent_id()==0){
                        fillingOfRecords(0);
                    }else
                        UncoverBranch(recordCursor,0, 0, record.getParent_id());
                }else {

                    if (record.getHasChildRec()) {
                        if (!objLinkRecList.isEmpty()) {
                            fillingOfRecords(objLinkRecList.get(position));
                        }

                        int parentIdRec = records.get(position).getRecord_id();

                        //records_2 = records;

                        records.clear();

                        UncoverBranch(recordCursor, 0, 0, parentIdRec);

                        //RecordDiffUtilCallback recordDiffUtilCallback = new RecordDiffUtilCallback(records_2,records);
                        //DiffUtil.DiffResult recordDiffResult = DiffUtil.calculateDiff(recordDiffUtilCallback);

                        //recordAdapter.setData(records);
                        //recordDiffResult.dispatchUpdatesTo(recordAdapter);

                    }
                }

                 */
                if(selObjId)

                FillingOfRec(record.getRecord_id());

                recordAdapter.notifyDataSetChanged();
            }
        };

        RecordAdapter.OnRecordCBindListener recordBindListener = new RecordAdapter.OnRecordCBindListener(){
            @Override
            public void onRecordBind(int position){
               //recordAdapter.notifyItemChanged(position-1);
            }
        };

        recordAdapter = new RecordAdapter(this, records, recordClickListener,recordBindListener);
        recordList.setAdapter(recordAdapter);

        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        recordList.setLayoutManager(layoutmanager);

        recordList.addItemDecoration(new RecordDecoration(records));

        reqToFillRec = true;

        // открываем подключение
        db = databaseHelper.getReadableDatabase();

    }

    @Override
    public void onResume() {
        super.onResume();

        if(reqToFillRec) {
            //fillingOfRecords(0);

            FillingOfRec(0);
            reqToFillRec = false;
        }

        //HScroll.computeScroll();
    }

    public void FillingFirstLevel(){

        int i=0;

        objectCursor = db.rawQuery("select record_clusters._id, parent_id, _name, _time FROM " +
            "record_clusters INNER JOIN field_clusters ON record_clusters.field_id=field_clusters._id " +
            "INNER JOIN name_clusters ON field_clusters.name_id=name_clusters._id " +
            "WHERE parent_id=?", new String[]{String.valueOf(0});


        records.clear();

        while (objectCursor.moveToNext()) {
            records.add(new Record(objectCursor.getInt(0), objectCursor.getString(2), objectCursor.getInt(1),0));
            records.get(i).setParent_id(0);
            i++;
        }

        objectCursor.close();
    }

    public void FillingOtherLevel(int parentId){

        int k=0;

        if(recordCursor.isClosed()) {
            recordCursor = db.rawQuery("select record_clusters._id, parent_id, _name, _time FROM " +
                    "record_clusters INNER JOIN field_clusters ON record_clusters.field_id=field_clusters._id " +
                    "INNER JOIN name_clusters ON field_clusters.name_id=name_clusters._id " +
                    "WHERE parent_id=?", new String[]{String.valueOf(parentId)});
        }

        records.clear();

        for(int i=0;i<recordCursor.getCount();i++){

            recordCursor.moveToPosition(i);

            if(recordCursor.getInt(1)==parentId)
                records.add(new Record(recordCursor.getInt(0), recordCursor.getString(2), recordCursor.getInt(1),0));
                records.get(k).setParent_id(parentId);
                k++;
        }

    }

    public void fillingOfRecords(int idOfSelectedObj){

        int curRecListId=0;

        if(idOfSelectedObj==0) {
            objectCursor = db.rawQuery("select _id FROM list_objects", null);
            while (objectCursor.moveToNext()) {
                recordCursor = db.rawQuery("select record_clusters._id, parent_id, _name, _time FROM " +
                        "record_clusters INNER JOIN field_clusters ON record_clusters.field_id=field_clusters._id " +
                        "INNER JOIN name_clusters ON field_clusters.name_id=name_clusters._id " +
                        "WHERE object_id=?", new String[]{objectCursor.getString(0)});

                curRecListId = UncoverBranch(recordCursor,curRecListId, objectCursor.getInt(0), 0);
            }
            objectCursor.close();
            recordCursor.close();
        }else{
            if(recordCursor.isClosed()) {
                recordCursor = db.rawQuery("select record_clusters._id, parent_id, _name, _time FROM " +
                        "record_clusters INNER JOIN field_clusters ON record_clusters.field_id=field_clusters._id " +
                        "INNER JOIN name_clusters ON field_clusters.name_id=name_clusters._id " +
                        "WHERE object_id=?", new String[]{String.valueOf(idOfSelectedObj)});

                objLinkRecList.clear();
            }
        }
    }

    public int UncoverBranch(Cursor cursor,int curRecListId,int objIdCurRec,int parentRecId){

        ArrayList<Integer> levels = new ArrayList<>();
        int cur_level=0, indent=0;
        levels.add(cur_level, parentRecId);

        if(parentRecId!=0){

            Record.CursorMatchFound_2(cursor,0,parentRecId);
            records.add(curRecListId,new Record(cursor.getInt(0), cursor.getString(2), cursor.getInt(1),0));
            records.get(curRecListId).setParent_id(cursor.getInt(1));
            curRecListId = curRecListId + 1;

            indent=1;
        }

        while (cur_level>-1){

            if(Record.CursorMatchFound_1(cursor,1,0,records,levels.get(cur_level))){

                //records.add(new Record(cursor.getInt(0), cursor.getString(2), cursor.getInt(1), cur_level));
                records.add(curRecListId,new Record(cursor.getInt(0), cursor.getString(2), cursor.getInt(1), cur_level+indent));

                if(objIdCurRec>0)
                    objLinkRecList.add(objIdCurRec);

                if(cur_level<1) {
                    cur_level = cur_level + 1;
                    levels.add(cur_level, cursor.getInt(0));
                }

                records.get(curRecListId).setHasChildRec(Record.CursorMatchFound_2(cursor,1,records.get(curRecListId).getRecord_id()));
                records.get(curRecListId).setParent_id(parentRecId);

                curRecListId = curRecListId + 1;

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
    //    addRecord(0);
       // HScroll.computeScroll();
    }

    public void LevelUp(View view){

        if(records.get(0).getParent_id()-1>=0) {

            FillingOfRec(records.get(0).getParent_id() - 1);

            recordAdapter.notifyDataSetChanged();
        }
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