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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;


public class AddActivity extends AppCompatActivity {

    TextView parentRec;
    EditText nameBox;
    Button saveButton;
    ListView fieldList;

    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor recordCursor, nameCursor, fieldCursor, objectCursor;
    ArrayAdapter<String> fieldAdapter;
    long recordId=0, objectId=0, fieldIdForSave=0;
    ArrayList<String> fields = new ArrayList<>();
    ArrayList<Long> field_id = new ArrayList<>();


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

        fieldIdForSave = 0;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            recordId = extras.getLong("id");
        }

        if (recordId > 0) {
            // получаем элемент по id из бд
            recordCursor = db.rawQuery("select object_id, _name FROM " +
                    "record_clusters INNER JOIN field_clusters ON record_clusters.field_id=field_clusters._id " +
                    "INNER JOIN name_clusters ON field_clusters.name_id=name_clusters._id " +
                    "WHERE  record_clusters._id = ?", new String[]{String.valueOf(recordId)});
            recordCursor.moveToFirst();
            objectId = recordCursor.getInt(0);
            parentRec.setText(recordCursor.getString(1));
            //parentRec.setText(String.valueOf(recordId));
            recordCursor.close();
        } else {
            objectCursor = db.rawQuery("select _id FROM list_objects", null);

            if(objectCursor.moveToLast())
                objectId = objectCursor.getInt(0)+1;
             else
                objectId = 1;
            objectCursor.close();
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

                fields.clear();
                field_id.clear();

                recordCursor = db.rawQuery("select record_clusters._id, parent_id, field_id, _type, _name, _time FROM " +
                        "record_clusters INNER JOIN field_clusters ON record_clusters.field_id=field_clusters._id " +
                        "INNER JOIN name_clusters ON field_clusters.name_id=name_clusters._id " +
                        "WHERE _name like ?", new String[]{"%" + s.toString() + "%"});

                while(recordCursor.moveToNext()){
                    fields.add(recordCursor.getString(4));
                    field_id.add(recordCursor.getLong(2));
                }

                fieldAdapter.notifyDataSetChanged();
                recordCursor.close();
            }
        });

        fieldList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                fieldIdForSave = 0;

                fieldIdForSave = field_id.get(position);

            }
        });



    }

    public void save(View view){

        ContentValues cv = new ContentValues();

        if (recordId == 0) {
            cv.put(DatabaseHelper.COLUMN_ID, objectId);
            db.insert(DatabaseHelper.TABLE_OBJECTS,null,cv);
        }

        if(fieldIdForSave>0){

            cv.clear();
            cv.put(DatabaseHelper.COLUMN_OBJECT_ID, String.valueOf(objectId));
            cv.put(DatabaseHelper.COLUMN_PARENT_ID, String.valueOf(recordId));
            cv.put(DatabaseHelper.COLUMN_FIELD_ID, String.valueOf(fieldIdForSave));

            db.insert(DatabaseHelper.TABLE_RECORDS,null,cv);

  //          recordCursor = db.rawQuery("insert into record_clusters(object_id, parent_id, field_id)" +
     //               "VALUES(?);", new String[]{String.valueOf(objectId)+", "+String.valueOf(recordId)+", "+String.valueOf(fieldIdForSave)});
        } else {
            nameCursor = db.rawQuery("select _id FROM name_clusters " +
                    "WHERE  _name = ?", new String[]{nameBox.getText().toString()});

            if(!nameCursor.moveToFirst()){
                cv.clear();
                cv.put(DatabaseHelper.COLUMN_NAME, nameBox.getText().toString());
                db.insert(DatabaseHelper.TABLE_NAMES,null,cv);

                nameCursor = db.rawQuery("select _id FROM name_clusters " +
                        "WHERE  _name = ?", new String[]{nameBox.getText().toString()});
                nameCursor.moveToFirst();
            }

            cv.clear();
            cv.put(DatabaseHelper.COLUMN_TYPE, 0);
            cv.put(DatabaseHelper.COLUMN_NAME_ID, nameCursor.getInt(0));
            db.insert(DatabaseHelper.TABLE_FIELDS,null,cv);

            fieldCursor = db.rawQuery("select _id FROM field_clusters " +
                    "WHERE  name_id = ?", new String[]{nameCursor.getString(0)});

            fieldCursor.moveToFirst();

            cv.clear();
            cv.put(DatabaseHelper.COLUMN_OBJECT_ID, String.valueOf(objectId));
            cv.put(DatabaseHelper.COLUMN_PARENT_ID, String.valueOf(recordId));
            cv.put(DatabaseHelper.COLUMN_FIELD_ID, fieldCursor.getString(0));

            db.insert(DatabaseHelper.TABLE_RECORDS,null,cv);

            nameCursor.close();
            fieldCursor.close();
            cv.clear();
        }

        goHome();
    }

    public void back(View view){
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