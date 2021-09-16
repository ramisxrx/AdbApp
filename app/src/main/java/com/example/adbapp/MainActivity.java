package com.example.adbapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView recordList;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor objectCursor, recordCursor;

    ArrayList<String> records = new ArrayList<>();
    ArrayAdapter<String> recordAdapter;
    ArrayList<Long> levels = new ArrayList<>();
    ArrayList<Long> record_id = new ArrayList<>();

    int cur_level=0, count_rec=0, i=0;
    String indent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recordList = (ListView) findViewById(R.id.list);
        databaseHelper = new DatabaseHelper(getApplicationContext());

        recordAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,records);
        recordList.setAdapter(recordAdapter);



    }

    @Override
    public void onResume() {
        super.onResume();
        // открываем подключение
        db = databaseHelper.getReadableDatabase();

        recordAdapter.clear();
        records.clear();
        levels.clear();
        record_id.clear();

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

            records.add(recordCursor.getString(3));
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


                if(recordCursor.getLong(1)==levels.get(cur_level)){
                    record_id.add(recordCursor.getLong(0));

                    levels.add(recordCursor.getLong(0));
                    cur_level = cur_level+1;

                    indent = "";
                    for(i=cur_level; i>0; i--)
                        indent = indent + "...";

                    records.add(indent + recordCursor.getString(3));

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

            recordAdapter.notifyDataSetChanged();

        }

     //   recordAdapter.notifyDataSetChanged();







        recordList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                intent.putExtra("id", record_id.get(position-1));
                startActivity(intent);



            }
        });

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