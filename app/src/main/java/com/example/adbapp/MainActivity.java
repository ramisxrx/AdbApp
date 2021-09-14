package com.example.adbapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleCursorAdapter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView userList;
    ListView recordList;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor, objectCursor, recordCursor;
    SimpleCursorAdapter userAdapter;

    ArrayList<String> records = new ArrayList<String>();
    ArrayAdapter<String> recordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recordList = (ListView) findViewById(R.id.list);
        databaseHelper = new DatabaseHelper(getApplicationContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        // открываем подключение
        db = databaseHelper.getReadableDatabase();

        recordAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,records);
        recordList.setAdapter(recordAdapter);

        objectCursor = db.rawQuery("select list_objects.id FROM list_objects", null);
        objectCursor.moveToFirst();
        while(!objectCursor.isAfterLast()) {
            recordCursor = db.rawQuery("select record_claster._id, parent_id, _type, _name, _time FROM " +
                    "record_clusters INNER JOIN field_clusters ON record_clusters.field_id=field_clusters._id " +
                    "INNER JOIN name_clusters ON field_clusters.name_id=name_clusters._id " +
                    "WHERE object_id=?", new String[]{objectCursor.getString(0)});

            recordCursor.moveToFirst();
            while(!recordCursor.isAfterLast() & recordCursor.getInt(1)!=0){
                recordCursor.moveToNext();
            }


            objectCursor.moveToNext();
        }






        recordList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });






        //получаем данные из бд в виде курсора
        userCursor = db.rawQuery("select record_clusters._id, name_ FROM " +
                                     "record_clusters INNER JOIN field_clusters ON record_clusters.field_id=field_clusters._id " +
                                     "INNER JOIN name_clusters ON field_clusters.name_id=name_clusters._id", null);
        // определяем, какие столбцы из курсора будут выводиться в ListView
        String[] headers = new String[]{"main_clusters._id", DatabaseHelper.COLUMN_NAME};
        // создаем адаптер, передаем в него курсор
        userAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
                userCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);
        userList.setAdapter(userAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        userCursor.close();
    }

}