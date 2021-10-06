package com.example.adbapp;

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


    ArrayList<Record> records = new ArrayList<>();
    ArrayList<Long> levels = new ArrayList<>();
    ArrayList<Long> record_id = new ArrayList<>();

    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<Instrumentation.ActivityResult>() {
                @Override
                public void onActivityResult(Instrumentation.ActivityResult result) {

                    TextView textView = (TextView) findViewById(R.id.textView);
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent intent = result.getData();
                        String accessMessage = intent.getStringExtra(ACCESS_MESSAGE);
                        textView.setText(accessMessage);
                    }
                    else{
                        textView.setText("Ошибка доступа");
                    }
                }
            });


    boolean reqToFillRec;

    int cur_level=0, count_rec=0, i,j, curRecId=0;
    String indent;
    long count_childRec=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = (Button) findViewById(R.id.addButton);
        RecyclerView recordList = (RecyclerView) findViewById(R.id.list);

        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        recordClickListener = new RecordAdapter.OnRecordClickListener() {
            @Override
            public void onRecordClick(Record record, int position) {
                addRecord(record.getRecord_id());
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
                    if(recordCursor.getInt(1) == levels.get(cur_level)) {

                        if(!records.isEmpty()) {

                            for (j = 0; j < records.size(); j++) {

                                if (records.get(j).getRecord_id() == recordCursor.getInt(0)) {
                                    break;
                                }
                            }
                        }
                        if(j==records.size() || records.isEmpty()) {
                            levels.add(recordCursor.getLong(0));
                            cur_level = cur_level + 1;

                            //records.add(new Record(recordCursor.getInt(0), recordCursor.getString(2), recordCursor.getInt(3), cur_level-1));
                            records.add(new Record(recordCursor.getInt(0), recordCursor.getString(2), recordCursor.getInt(1), cur_level-1));
                            curRecId = curRecId + 1;

                            for (i = 0; i < recordCursor.getCount(); i++) {
                                recordCursor.moveToPosition(i);
                                if (recordCursor.getInt(1) == records.get(curRecId).getRecord_id()) {
                                    records.get(curRecId).setHasChildRec(true);
                                    break;
                                }
                            }
                            count_rec = count_rec - 1;
                            break;
                        }
                    }
                }
                if(i==recordCursor.getCount()){
                    levels.remove(cur_level);
                    cur_level = cur_level-1;
                }

            }
        }

    }

    public void addRecord(int idRec){
        Intent intent = new Intent(getApplicationContext(), AddActivity.class);
        intent.putExtra("id", idRec);
        mStartForResult.launch(intent);
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