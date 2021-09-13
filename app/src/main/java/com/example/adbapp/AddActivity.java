package com.example.adbapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class AddActivity extends AppCompatActivity {

    TextView parentRec;
    EditText nameBox;
    Button saveButton;
    ListView fieldList;

    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    long recordId=0, objectId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
    }

    @Override
    public void onResume() {
        super.onResume();

        parentRec = (TextView) findViewById(R.id.parentRec);
        nameBox = (EditText) findViewById(R.id.name);
        fieldList = (ListView) findViewById(R.id.fieldList);
        saveButton = (Button) findViewById(R.id.saveButton);

        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.getWritableDatabase();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            recordId = extras.getLong("id");
        }
        // если 0, то добавление
        if (recordId > 0) {
            // получаем элемент по id из бд
            userCursor = db.rawQuery("select object_id, name_ FROM " +
                    "record_clusters INNER JOIN field_clusters ON record_clusters.field_id=field_clusters._id " +
                    "INNER JOIN name_clusters ON field_clusters.name_id=name_clusters._id " +
                    "WHERE  record_clusters._id=?", new String[]{String.valueOf(recordId)});
            userCursor.moveToFirst();
            objectId = userCursor.getInt(0);
            parentRec.setText(userCursor.getString(1));
            userCursor.close();
        } else {

        }


    }

    public void save(View view){

        goHome();
    }

    private void goHome(){
        // закрываем подключение
        db.close();
        // переход к главной activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}