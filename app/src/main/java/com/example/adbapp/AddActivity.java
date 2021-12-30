package com.example.adbapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adbapp.FillingOfList.AddingNewRecord;
import com.example.adbapp.FillingOfList.FoundListFilling;
import com.example.adbapp.RecordList.Record;
import com.example.adbapp.RecordList.RecordAdapter;
import com.example.adbapp.RecordList.RecordDecoration;

public class AddActivity extends AppCompatActivity {

    TextView parentRec;
    EditText nameBox;
    Button saveButton;
    Button buttonLevelUp;

    RecordAdapter recordAdapter;
    RecordAdapter.OnRecordClickListener recordClickListener;
    //HorizontalScrollView HScroll;

    private static final String TAG = "**AddActivity**";

    AddingNewRecord addingNewRecord;
    FoundListFilling foundList;

    int recordId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        parentRec = (TextView) findViewById(R.id.parentRec);
        nameBox = (EditText) findViewById(R.id.name);
        saveButton = (Button) findViewById(R.id.saveButton);
        buttonLevelUp = findViewById(R.id.buttonLevelUp);
        RecyclerView recordList = (RecyclerView) findViewById(R.id.list);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            recordId = extras.getInt("id");
        else
            recordId = 0;

        AddingNewRecord.NotifyViews_before notifyViews_before = new AddingNewRecord.NotifyViews_before() {
            @Override
            public void Save() {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Добавление новой записи...", Toast.LENGTH_SHORT);
                toast.show();
            }
        };

        AddingNewRecord.NotifyViews_after notifyViews_after = new AddingNewRecord.NotifyViews_after() {
            @Override
            public void Save() {
                goHome(!addingNewRecord.successAddingNewRecord);
            }
        };

        addingNewRecord = new AddingNewRecord(getApplicationContext(),recordId,1,notifyViews_before,notifyViews_after);

        parentRec.setText(addingNewRecord.parentName);

        FoundListFilling.NotifyViews_after FoundList_notifyViews_after = new FoundListFilling.NotifyViews_after() {
            @Override
            public void ActionOfSearch() {
                recordAdapter.typeView = 0;
                recordAdapter.notifyDataSetChanged();
            }

            @Override
            public void ActionDown() {
                recordAdapter.typeView = 1;
                recordAdapter.notifyDataSetChanged();
                buttonLevelUp.setVisibility(View.VISIBLE);
            }

            @Override
            public void ActionUp() {
                recordAdapter.typeView = 1;
                recordAdapter.notifyDataSetChanged();
                buttonLevelUp.setVisibility(View.VISIBLE);
            }

            @Override
            public void ToPreviousLevel() {
                if(foundList.cur_level==-1)
                    recordAdapter.typeView = 0;
                recordAdapter.notifyDataSetChanged();
                if(foundList.cur_level==-1)
                    buttonLevelUp.setVisibility(View.GONE);
            }
        };

        foundList = new FoundListFilling(getApplicationContext(),FoundList_notifyViews_after);

        nameBox.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                foundList.ActionOfSearch(s);
                addingNewRecord._name = nameBox.getText().toString();

                saveButton.setEnabled(addingNewRecord._name.length() > 0);
            }
        });

        RecordAdapter.OnRecordLongClickListener recordLongClickListener = new RecordAdapter.OnRecordLongClickListener() {
            @Override
            public void onRecordLongClick(int position) {
            }
        };

        recordClickListener = new RecordAdapter.OnRecordClickListener() {
            @Override
            public void onRecordClick(Record record, int position) {
                if(foundList.cur_level==-1) {
                    int oldPosSelItem;

                    if (recordAdapter.posSelItem >= 0) {
                        if (recordAdapter.posSelItem == position) {
                            recordAdapter.posSelItem = -1;
                            addingNewRecord.field_id = 0;
                        } else {
                            oldPosSelItem = recordAdapter.posSelItem;
                            recordAdapter.posSelItem = position;
                            recordAdapter.notifyItemChanged(oldPosSelItem);
                            addingNewRecord.field_id = foundList.records.get(position).getRecord_id();
                        }
                    } else {
                        recordAdapter.posSelItem = position;
                        addingNewRecord.field_id = foundList.records.get(position).getRecord_id();

                    }

                    if (addingNewRecord.field_id == 0)
                        saveButton.setText("Добавить новый запись");
                    else
                        saveButton.setText("Добавить ассоциацию");

                    recordAdapter.notifyItemChanged(position);
                }
            }
        };

        recordAdapter = new RecordAdapter(this, foundList.records,1, recordClickListener,recordLongClickListener);
        recordList.setAdapter(recordAdapter);

        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        recordList.setLayoutManager(layoutmanager);
        recordList.addItemDecoration(new RecordDecoration(foundList.records));

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {

            @Override
            public int getMovementFlags (RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int swipeFlags;
                int dragFlags = 0;

                if(foundList.cur_level>-1){
                    if(foundList.selDirection)
                        swipeFlags = ItemTouchHelper.RIGHT;
                    else
                        swipeFlags = ItemTouchHelper.LEFT;
                }else
                    swipeFlags = ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;

                return makeMovementFlags(dragFlags,swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if(foundList.cur_level==-1) {
                    recordAdapter.posSelItem = viewHolder.getAdapterPosition();
                    addingNewRecord.field_id = foundList.records.get(viewHolder.getAdapterPosition()).getRecord_id();
                    saveButton.setText("Добавить ассоциацию");
                }

                if(direction==4)
                    foundList.ActionDown(viewHolder.getAdapterPosition());
                else
                    foundList.ActionUp(viewHolder.getAdapterPosition());
            }
        };

        ItemTouchHelper touchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        touchHelper.attachToRecyclerView(recordList);
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    public void LevelUp(View view){

        Log.d(TAG, "LevelUp: cur_level="+String.valueOf(foundList.cur_level));

        foundList.ToPreviousLevel();
    }


    public void save(View view){

        addingNewRecord.Save();
    }

    public void back(View view){
        goHome(true);
    }

    private void goHome(boolean cancel){

        Intent data = new Intent();
        data.putExtra("id of added record",recordId);
        if(cancel) {
            setResult(RESULT_CANCELED,data);
        }else {
            setResult(RESULT_OK, data);
        }
        foundList.Destroy();
        addingNewRecord.Destroy();
        finish();
    }
}