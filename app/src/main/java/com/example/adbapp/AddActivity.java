package com.example.adbapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    HorizontalScrollView HScroll;

    DatabaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor recordCursor, nameCursor, fieldCursor, objectCursor;
    ArrayAdapter<String> fieldAdapter;
    long  objectId=0, fieldIdForSave=0;
    int recordId=0;
    //ArrayList<String> fields = new ArrayList<>();
    ArrayList<Long> field_id = new ArrayList<>();
    ArrayList<Integer> recIdList = new ArrayList<>();
    ArrayList<Integer> recObjIdList = new ArrayList<>();
    ArrayList<Record> records = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        parentRec = (TextView) findViewById(R.id.parentRec);
        nameBox = (EditText) findViewById(R.id.name);
        saveButton = (Button) findViewById(R.id.saveButton);
        RecyclerView recordList = (RecyclerView) findViewById(R.id.list);
        HScroll =(HorizontalScrollView) findViewById(R.id.hscroll);


        recordClickListener = new RecordAdapter.OnRecordClickListener() {
            @Override
            public void onRecordClick(Record record, int position) {
                fieldIdForSave = 0;

                fieldIdForSave = field_id.get(position);
            }
        };
        recordAdapter = new RecordAdapter(this, records, recordClickListener);
        recordList.setAdapter(recordAdapter);

        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        recordList.setLayoutManager(layoutmanager);
        recordList.addItemDecoration(new RecordDecoration(records));

    }

    @Override
    public void onResume() {
        super.onResume();

        sqlHelper = new DatabaseHelper(this);
        db = sqlHelper.getWritableDatabase();

        fieldIdForSave = 0;

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

                records.clear();
                field_id.clear();

                recIdList.clear();
                recObjIdList.clear();

                recordCursor = db.rawQuery("select record_clusters._id,parent_id,field_id,_type,_name,_time,object_id FROM " +
                        "record_clusters INNER JOIN field_clusters ON record_clusters.field_id=field_clusters._id " +
                        "INNER JOIN name_clusters ON field_clusters.name_id=name_clusters._id " +
                        "WHERE _name like ?", new String[]{"%" + s.toString() + "%"});

                while(recordCursor.moveToNext() && s.length()>0){

                    records.add(new Record(recordCursor.getInt(0),recordCursor.getString(4),recordCursor.getInt(1),0));

                    records.get(records.size()-1).setParent_id(recordCursor.getInt(1));

                    recIdList.add(recordCursor.getInt(0));
                    recObjIdList.add(recordCursor.getInt(6));

                    field_id.add(recordCursor.getLong(2));
                }

                recordAdapter.notifyDataSetChanged();
                recordCursor.close();

                UncoverForEachBranch();
            }
        });

        HScroll.computeScroll();
    }

    public void UncoverForEachBranch(){
        boolean makeReq;

        for(int i=0;i<recIdList.size();i++){
            if(i==0)
                makeReq=true;
            else{
                if(recObjIdList.get(i)==recObjIdList.get(i-1))
                    makeReq=false;
                else
                    makeReq=true;
            }

            if(makeReq){
                recordCursor = db.rawQuery("select record_clusters._id, parent_id, _name, _time FROM " +
                        "record_clusters INNER JOIN field_clusters ON record_clusters.field_id=field_clusters._id " +
                        "INNER JOIN name_clusters ON field_clusters.name_id=name_clusters._id " +
                        "WHERE object_id=?", new String[]{String.valueOf(recObjIdList.get(i))});
            }

            UncoverBranchUp(recordCursor,Record.posInListById(records,recIdList.get(i)));

        }
    }

    public void UncoverBranchUp(Cursor cursor,int curRecListPos){
        ArrayList<Integer> levels = new ArrayList<>();

        while (records.get(curRecListPos).getParent_id()>0){

            if(Record.CursorMatchFound_2(cursor,0,records.get(curRecListPos).getParent_id())) {

                RecLevelShift(curRecListPos);
                records.add(curRecListPos, new Record(cursor.getInt(0), cursor.getString(2), cursor.getInt(1),0));
                records.get(curRecListPos).setParent_id(cursor.getInt(1));
                records.get(curRecListPos).setHasChildRec(Record.CursorMatchFound_2(cursor, 1, records.get(curRecListPos).getRecord_id()));

                recordAdapter.notifyItemInserted(curRecListPos);
                //HScroll.computeScroll();
            }
        }
    }

    public void RecLevelShift(int curRecListPos){
        do{
            records.get(curRecListPos).setLevel(records.get(curRecListPos).getLevel()+1);
            recordAdapter.notifyItemChanged(curRecListPos);
            if(curRecListPos<records.size()-1)
                curRecListPos++;
            else
                break;
        }while (records.get(curRecListPos).getParent_id()==records.get(curRecListPos-1).getRecord_id());
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

        goHome(false);
    }

    public void back(View view){
        goHome(true);
    }

    private void goHome(boolean cancel){
        // закрываем подключение
        db.close();

        // переход к главной activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);

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