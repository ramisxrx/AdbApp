package com.example.adbapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import java.util.ArrayList;


public class AddActivity extends AppCompatActivity {

    TextView parentRec;
    EditText nameBox;
    Button saveButton;
    ListView fieldList;

    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor recordCursor;
    ArrayAdapter<String> fieldAdapter;
    long recordId=0, objectId=0;
    ArrayList<String> fields = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        parentRec = (TextView) findViewById(R.id.parentRec);
        nameBox = (EditText) findViewById(R.id.name);
        fieldList = (ListView) findViewById(R.id.fieldList);
        saveButton = (Button) findViewById(R.id.saveButton);

        fieldAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,fields);
        fieldList.setAdapter(fieldAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.getWritableDatabase();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            recordId = extras.getLong("id");
        }
        // если 0, то добавление
        if (recordId > 0) {
            // получаем элемент по id из бд
            recordCursor = db.rawQuery("select object_id, _name FROM " +
                    "record_clusters INNER JOIN field_clusters ON record_clusters.field_id=field_clusters._id " +
                    "INNER JOIN name_clusters ON field_clusters.name_id=name_clusters._id " +
                    "WHERE  record_clusters._id in (select parent_id FROM record_clusters WHERE _id = ?)", new String[]{String.valueOf(recordId)});
            recordCursor.moveToFirst();
            objectId = recordCursor.getInt(0);
            parentRec.setText(recordCursor.getString(1));
            //parentRec.setText(String.valueOf(recordId));
            recordCursor.close();
        } else {

        }

        // если в текстовом поле есть текст, выполняем фильтрацию
        // данная проверка нужна при переходе от одной ориентации экрана к другой
//        if(!nameBox.getText().toString().isEmpty())
//            fieldAdapter.getFilter().filter(nameBox.getText().toString());

        // установка слушателя изменения текста
        nameBox.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) { }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            // при изменении текста выполняем фильтрацию
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                recordCursor = db.rawQuery("select record_clasters._id, parent_id, _type, _name, _time FROM " +
                        "record_clusters INNER JOIN field_clusters ON record_clusters.field_id=field_clusters._id " +
                        "INNER JOIN name_clusters ON field_clusters.name_id=name_clusters._id " +
                        "WHERE _name like ?", new String[]{"%" + s.toString() + "%"});

                while(recordCursor.moveToNext()){
                    fields.add(recordCursor.getString(3));
                }

            }


        });
        fieldAdapter.notifyDataSetChanged();


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