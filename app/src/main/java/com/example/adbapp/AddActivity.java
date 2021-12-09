package com.example.adbapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;



public class AddActivity extends AppCompatActivity {

    TextView parentRec;
    EditText nameBox;
    Button saveButton;
    ListView fieldList;

    RecordAdapter recordAdapter;
    RecordAdapter.OnRecordClickListener recordClickListener;
    //HorizontalScrollView HScroll;

    private static final String TAG = "**AddActivity**";

    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor recordCursor, nameCursor, fieldCursor, objectCursor;
    ArrayAdapter<String> fieldAdapter;
    long  objectId=0, fieldIdForSave=0;
    int recordId=0, selObjId=0, cur_level=0, selFieldId=0, typeViewHolder=0;
    boolean selDirection=false;
    //ArrayList<String> fields = new ArrayList<>();
    ArrayList<Integer> field_id = new ArrayList<>();
    ArrayList<Integer> recIdList = new ArrayList<>();
    ArrayList<Integer> objIdList = new ArrayList<>();
    ArrayList<Record> records = new ArrayList<>();
    ArrayList<Record> fields = new ArrayList<>();
    ArrayList<Integer> parentIdByLevels = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        parentRec = (TextView) findViewById(R.id.parentRec);
        nameBox = (EditText) findViewById(R.id.name);
        saveButton = (Button) findViewById(R.id.saveButton);
        RecyclerView recordList = (RecyclerView) findViewById(R.id.list);
        //HScroll =(HorizontalScrollView) findViewById(R.id.hscroll);


        recordClickListener = new RecordAdapter.OnRecordClickListener() {
            @Override
            public void onRecordClick(Record record, int position) {

                int oldPosSelItem;

                if(recordAdapter.posSelItem>=0){
                    if(recordAdapter.posSelItem==position) {
                        recordAdapter.posSelItem = -1;
                        fieldIdForSave = 0;
                    }else{
                        oldPosSelItem = recordAdapter.posSelItem;
                        recordAdapter.posSelItem = position;
                        recordAdapter.notifyItemChanged(oldPosSelItem);
                        fieldIdForSave = field_id.get(position);
                    }
                }else{
                    recordAdapter.posSelItem = position;
                    fieldIdForSave = field_id.get(position);

                }

                if(fieldIdForSave==0)
                    saveButton.setText("Добавить новый запись");
                else
                    saveButton.setText("Добавить ассоциацию");

                recordAdapter.notifyItemChanged(position);
            }
        };

        recordAdapter = new RecordAdapter(this, records,typeViewHolder, recordClickListener);
        recordList.setAdapter(recordAdapter);

        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        recordList.setLayoutManager(layoutmanager);
        recordList.addItemDecoration(new RecordDecoration(records));



        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {

            @Override
            public int getMovementFlags (RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int swipeFlags = 0;
                int dragFlags = 0;

                swipeFlags = ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;

                if(selFieldId>0){
                    if(selDirection)
                        swipeFlags = ItemTouchHelper.RIGHT;
                    else
                        swipeFlags = ItemTouchHelper.LEFT;
                }


                return makeMovementFlags(dragFlags,swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                int oldPosSelItem;

                Log.d(TAG, "onSwiped: ");



                if(selFieldId==0) {

                    if(recordAdapter.posSelItem>=0){
                        oldPosSelItem = recordAdapter.posSelItem;
                        recordAdapter.posSelItem = viewHolder.getAdapterPosition();
                        recordAdapter.notifyItemChanged(oldPosSelItem);
                    }else
                        recordAdapter.posSelItem = viewHolder.getAdapterPosition();

                    recordAdapter.notifyItemChanged(viewHolder.getAdapterPosition());


                    selFieldId = field_id.get(viewHolder.getAdapterPosition());
                    fieldIdForSave = selFieldId;

                    if(fieldIdForSave==0)
                        saveButton.setText("Добавить новый запись");
                    else
                        saveButton.setText("Добавить ассоциацию");

                    Log.d(TAG, "onSwiped: fieldIdForSave="+String.valueOf(fieldIdForSave));

                    if (direction == 4) // Down
                        selDirection = false;
                    else                // Up
                        selDirection = true;

                    FillingZeroLevel(selFieldId, selDirection);
                }else {

                        if (selObjId == 0) {
                            selObjId = objIdList.get(viewHolder.getAdapterPosition());

                            recordCursor = db.rawQuery("select record_clusters._id, parent_id, _name, _time FROM " +
                                    "record_clusters INNER JOIN field_clusters ON record_clusters.field_id=field_clusters._id " +
                                    "INNER JOIN name_clusters ON field_clusters.name_id=name_clusters._id " +
                                    "WHERE object_id=?", new String[]{String.valueOf(selObjId)});
                        }

                        cur_level++;

                    if (direction == 4) {   // Down
                        parentIdByLevels.add(cur_level, records.get(viewHolder.getAdapterPosition()).getRecord_id());

                        FillingOtherLevel(records.get(viewHolder.getAdapterPosition()).getRecord_id(),selDirection);
                    }else{                  // Up
                        parentIdByLevels.add(cur_level, records.get(viewHolder.getAdapterPosition()).getParent_id());

                        FillingOtherLevel(records.get(viewHolder.getAdapterPosition()).getParent_id(),selDirection);
                    }
                }

                recordAdapter.notifyDataSetChanged();


            }
        };

        ItemTouchHelper touchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        touchHelper.attachToRecyclerView(recordList);

        parentIdByLevels.add(0,0);
    }

    @Override
    public void onResume() {
        super.onResume();

        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.getWritableDatabase();


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            recordId = extras.getInt("id");
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

        nameBox.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) { }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            // при изменении текста выполняем фильтрацию
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                fields.clear();
                field_id.clear();

                fieldIdForSave = 0;

                recIdList.clear();
                objIdList.clear();
                selFieldId=0;
                selObjId=0;
                cur_level=0;

                recordCursor = db.rawQuery("select field_clusters._id,_name FROM " +
                        "field_clusters INNER JOIN name_clusters ON field_clusters.name_id=name_clusters._id " +
                        "WHERE _name like ?", new String[]{"%" + s.toString() + "%"});

                while(recordCursor.moveToNext() && s.length()>0){

                    fields.add(new Record(0,recordCursor.getString(1),recordCursor.getInt(0)));

                    //recIdList.add(recordCursor.getInt(0));

                    field_id.add(recordCursor.getInt(0));
                }

                records.clear();
                for(int i=0;i<fields.size();i++)
                    records.add(i,fields.get(i));

                typeViewHolder = 0;

                recordAdapter.notifyDataSetChanged();
                recordCursor.close();

                //UncoverForEachBranch();
            }
        });

        //HScroll.computeScroll();
    }

    public void FillingZeroLevel(int fieldId, boolean direction){

        /* direction: false - Down, true - Up */

        Log.d(TAG, "FillingZeroLevel: fieldId="+String.valueOf(fieldId));

        int i=0;

        fieldCursor = db.rawQuery("select record_clusters._id, parent_id, _name, _time,object_id FROM " +
                "record_clusters INNER JOIN field_clusters ON record_clusters.field_id=field_clusters._id " +
                "INNER JOIN name_clusters ON field_clusters.name_id=name_clusters._id " +
                "WHERE field_id=?", new String[]{String.valueOf(fieldId)});


        selObjId = 0;

        records.clear();
        objIdList.clear();

        if(direction){  // Up
            while (fieldCursor.moveToNext()) {

                recordCursor = db.rawQuery("select record_clusters._id, parent_id, _name, _time,object_id FROM " +
                        "record_clusters INNER JOIN field_clusters ON record_clusters.field_id=field_clusters._id " +
                        "INNER JOIN name_clusters ON field_clusters.name_id=name_clusters._id " +
                        "WHERE record_clusters._id=?", new String[]{fieldCursor.getString(1)});

                while (recordCursor.moveToNext()) {

                    records.add(new Record(recordCursor.getInt(0), recordCursor.getString(2), recordCursor.getInt(1)));
                    records.get(i).setParent_id(recordCursor.getInt(1));
                    i++;

                    objIdList.add(recordCursor.getInt(4));
                }
            }
        }else {         // Down
            while (fieldCursor.moveToNext()) {

                recordCursor = db.rawQuery("select record_clusters._id, parent_id, _name, _time,object_id FROM " +
                        "record_clusters INNER JOIN field_clusters ON record_clusters.field_id=field_clusters._id " +
                        "INNER JOIN name_clusters ON field_clusters.name_id=name_clusters._id " +
                        "WHERE parent_id=?", new String[]{fieldCursor.getString(0)});

                while (recordCursor.moveToNext()) {

                    records.add(new Record(recordCursor.getInt(0), recordCursor.getString(2), recordCursor.getInt(1)));
                    records.get(i).setParent_id(recordCursor.getInt(1));
                    i++;

                    objIdList.add(recordCursor.getInt(4));
                }
            }
        }

        typeViewHolder = 1;

        Log.d(TAG, "FillingZeroLevel: cur_level="+String.valueOf(cur_level));

        recordCursor.close();
        fieldCursor.close();

    }


    public void FillingOtherLevel(int parentId,boolean direction){

        /* direction: false - Down, true - Up */

        int k=0;

        Log.d(TAG, "FillingOtherLevel: parent_id="+String.valueOf(parentId));
        Log.d(TAG, "FillingOtherLevel: parentIdByLevels.get(cur_level)="+String.valueOf(parentIdByLevels.get(cur_level)));

        records.clear();

        for(int i=0;i<recordCursor.getCount();i++){

            recordCursor.moveToPosition(i);

            if(direction) {     // Up
                if (recordCursor.getInt(0) == parentId) {
                    records.add(new Record(recordCursor.getInt(0), recordCursor.getString(2), recordCursor.getInt(1)));
                    records.get(k).setParent_id(recordCursor.getInt(1));
                    k++;
                }
            }else{              // Down
                if (recordCursor.getInt(1) == parentId) {
                    records.add(new Record(recordCursor.getInt(0), recordCursor.getString(2), recordCursor.getInt(1)));
                    records.get(k).setParent_id(parentId);
                    k++;
                }
            }
        }

        Log.d(TAG, "FillingOtherLevel: cur_level="+String.valueOf(cur_level));

    }

    public void LevelUp(View view){

        Log.d(TAG, "LevelUp: cur_level="+String.valueOf(cur_level));

        if(cur_level>0) {

            parentIdByLevels.remove(cur_level);
            cur_level--;

            Log.d(TAG, "LevelUp: cur_level="+String.valueOf(cur_level));

            if (cur_level == 0) {
                selObjId = 0;
                FillingZeroLevel(selFieldId, selDirection);
            }
            else
                FillingOtherLevel(parentIdByLevels.get(cur_level),selDirection);
        }else{
            selFieldId=0;
            records.clear();
            for (int i = 0; i < fields.size(); i++)
                records.add(i, fields.get(i));
        }

        recordAdapter.notifyDataSetChanged();

    }


    public void save(View view){

        ContentValues cv = new ContentValues();

        if (recordId == 0) {
            cv.put(DatabaseHelper.COLUMN_ID, objectId);
            db.insert(DatabaseHelper.TABLE_OBJECTS,null,cv);
        }

        if(fieldIdForSave>0){

            Log.d(TAG, "save: fieldIdForSave="+String.valueOf(fieldIdForSave));

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

            fieldCursor.moveToLast();

            cv.clear();
            cv.put(DatabaseHelper.COLUMN_OBJECT_ID, String.valueOf(objectId));
            cv.put(DatabaseHelper.COLUMN_PARENT_ID, String.valueOf(recordId));
            cv.put(DatabaseHelper.COLUMN_FIELD_ID, fieldCursor.getString(0));

            db.insert(DatabaseHelper.TABLE_RECORDS,null,cv);

            nameCursor.close();
            fieldCursor.close();
            cv.clear();
        }

        goHome(false);
    }

    public void back(View view){
        goHome(true);
    }

    private void goHome(boolean cancel){
        // закрываем подключение
        db.close();

        // переход к главной activity
        //Intent intent = new Intent(this, MainActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //startActivity(intent);

        Intent data = new Intent();
        data.putExtra("id of added record",recordId);
        if(cancel) {
            setResult(RESULT_CANCELED,data);
        }else {
            setResult(RESULT_OK, data);
        }
        finish();
    }
}