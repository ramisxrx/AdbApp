package com.example.adbapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import java.util.ArrayList;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    Button addButton;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor objectCursor, recordCursor;
    RecordAdapter recordAdapter;

    ArrayList<Record> records = new ArrayList<>();
    ArrayList<Long> levels = new ArrayList<>();
    ArrayList<Long> record_id = new ArrayList<>();

    int cur_level=0, count_rec=0, i=0, curRecId=0;
    String indent;
    long count_childRec=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = (Button) findViewById(R.id.addButton);
        RecyclerView recordList = (RecyclerView) findViewById(R.id.list);
        databaseHelper = new DatabaseHelper(getApplicationContext());

        RecordAdapter.OnRecordClickListener recordClickListener = new RecordAdapter.OnRecordClickListener() {
            @Override
            public void onRecordClick(Record record, int position) {
                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                //intent.putExtra("id", record_id.get(position));
                intent.putExtra("id", records.get(position).getRecord_id());
                startActivity(intent);
            }
        };
        RecordAdapter recordAdapter = new RecordAdapter(this, records, recordClickListener);
        recordList.setAdapter(recordAdapter);



    }

    @Override
    public void onResume() {
        super.onResume();
        // открываем подключение
        db = databaseHelper.getReadableDatabase();

        records.clear();
    //    levels.clear();
        record_id.clear();
    /*
        objectCursor = db.rawQuery("select _id FROM list_objects", null);
        while(objectCursor.moveToNext()) {
            recordCursor = db.rawQuery("select record_clusters._id, parent_id, _type, _name, _time FROM " +
                    "record_clusters INNER JOIN field_clusters ON record_clusters.field_id=field_clusters._id " +
                    "INNER JOIN name_clusters ON field_clusters.name_id=name_clusters._id " +
                    "WHERE object_id=?", new String[]{objectCursor.getString(0)});

            count_rec = recordCursor.getCount();

            recordCursor.moveToFirst();
            i=0;
            while(i<(recordCursor.getCount()-1) && recordCursor.getInt(1)!=0){
                i=i+1;
                recordCursor.moveToPosition(i);
            }

            records.add(new Record(recordCursor.getString(3),1));
           // recordAdapter.notifyItemRangeInserted(0,records.size());
            record_id.add(recordCursor.getLong(0));
        //    records.add(String.valueOf(count_rec));
            count_rec = count_rec-1;

            cur_level = 0;
            levels.add(recordCursor.getLong(0));

            while(count_rec!=0){

                recordCursor.moveToFirst();
                i=0;
             //   records.add(String.valueOf(levels.get(cur_level)));
             //   records.add(String.valueOf(cur_level));
                while(i<(recordCursor.getCount()-1) && (recordCursor.getLong(1)!=levels.get(cur_level) || record_id.contains(recordCursor.getLong(0)))){
                    i=i+1;
                    recordCursor.moveToPosition(i);
                }


                if(recordCursor.getLong(1)==levels.get(cur_level) && !record_id.contains(recordCursor.getLong(0))){
                    record_id.add(recordCursor.getLong(0));

                    levels.add(recordCursor.getLong(0));
                    cur_level = cur_level+1;

                    indent = "";
                    for(i=cur_level; i>0; i--)
                        indent = indent + "... ";

                    records.add(new Record(indent + objectCursor.getString(0)+recordCursor.getString(3),1));
                 //   recordAdapter.notifyItemRangeChanged(0,records.size());
                //    records.add(String.valueOf(cur_level));

                    count_rec = count_rec-1;
                }
                else{
                    if(i>=(recordCursor.getCount()-1)){
                        levels.remove(cur_level);
                        cur_level = cur_level-1;
                    }
                }
            }

            levels.clear();

        //    recordAdapter.notifyDataSetChanged();

        }
    */



        curRecId = -1;
        objectCursor = db.rawQuery("select _id FROM list_objects", null);
        while(objectCursor.moveToNext()) {
            recordCursor = db.rawQuery("select record_clusters._id, parent_id, _name, _time FROM " +
                    "record_clusters INNER JOIN field_clusters ON record_clusters.field_id=field_clusters._id " +
                    "INNER JOIN name_clusters ON field_clusters.name_id=name_clusters._id " +
                    "WHERE object_id=0", new String[]{objectCursor.getString(0)});

            count_rec = recordCursor.getCount();

            cur_level = 0;

            if(count_rec>0) {

                for(i=0;i<recordCursor.getCount();i++){
                    recordCursor.moveToPosition(i);
                    if(recordCursor.getInt(1) == 0) {
                        records.add(new Record(recordCursor.getInt(0), recordCursor.getString(2), recordCursor.getInt(3), cur_level));
                        curRecId = curRecId + 1;
                        count_rec = count_rec -1;
                        break;
                    }
                }

                for(i=0;i<recordCursor.getCount();i++){
                    recordCursor.moveToPosition(i);
                    if(recordCursor.getInt(1) == records.get(curRecId).getRecord_id()) {
                        records.get(curRecId).setHasChildRec(true);
                        break;
                    }
                }
            }

            cur_level=0;
            levels.add(recordCursor.getLong(0));
            while (cur_level>-1){

                for(i=0;i<recordCursor.getCount();i++){
                    recordCursor.moveToPosition(i);
                    if(recordCursor.getInt(1) == levels.get(cur_level)) {
                        records.add(new Record(recordCursor.getInt(0), recordCursor.getString(2), recordCursor.getInt(3), cur_level));
                        curRecId = curRecId + 1;
                        count_rec = count_rec -1;
                        levels.add(recordCursor.getLong(0));
                        cur_level = cur_level+1;
                        break;
                    }
                }
                if(i==recordCursor.getCount()){
                    levels.remove(cur_level);
                    cur_level = cur_level-1;
                }

            }

















            count_rec = recordCursor.getCount();
            curRecId = -1;
            cur_level = -1;

            while(count_rec>0){

                i = 0;
                while (i < (recordCursor.getCount() - 1) && recordCursor.getInt(1) != 0) {
                    i = i + 1;
                    recordCursor.moveToPosition(i);
                }

                curRecId = curRecId+1;
                records.add(new Record(recordCursor.getInt(0),recordCursor.getString(2),recordCursor.getInt(3),cur_level+1));

                i = 0;
                while (i < (recordCursor.getCount() - 1) && recordCursor.getInt(1) != records.get(curRecId).getRecord_id()) {
                    i = i + 1;
                    recordCursor.moveToPosition(i);
                }

                records.get(curRecId).setHasChildRec(recordCursor.getInt(1) != records.get(curRecId).getRecord_id());

                cur_level = cur_level+1;

            }

        }

    }


    public void addObject(View view){
        Intent intent = new Intent(getApplicationContext(), AddActivity.class);
        startActivity(intent);
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