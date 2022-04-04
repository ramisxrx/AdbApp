package com.example.adbapp.AddingRecord;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adbapp.ContentView;
import com.example.adbapp.FillingOfList.FoundListFilling;
import com.example.adbapp.R;
import com.example.adbapp.RecordList.OnScrollListenerRecyclerView;
import com.example.adbapp.RecordList.Record;
import com.example.adbapp.RecordList.RecordAdapter;
import com.example.adbapp.RecordList.RecordDecoration;
import com.example.adbapp.ToPreviousLevel.ActionsClickFAB;
import com.example.adbapp.ToPreviousLevel.FAB_ToPreviousLevel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddRecordFragment extends Fragment implements ActionsClickFAB {

    protected static final String TAG = "**AddRecordFragment**";

    FrameLayout frameLayout;
    EditText nameBox;
    RecyclerView recordList;
    Button saveButton;
    FloatingActionButton buttonFAB;
    RecordAdapter recordAdapter;
    RecordAdapter.OnRecordClickListener recordClickListener;

    View viewAddidable;

    FoundListFilling foundList;

    public String name_ToAdd;
    public int field_id_ToAdd;

    private FAB_ToPreviousLevel fab_toPreviousLevel;
    private OnScrollListenerRecyclerView onScrollListenerRecyclerView;
    private final int _type;
    protected String stringAddContent;

    public AddRecordFragment(int _type){
        this._type = _type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_record, container, false);

        frameLayout = (FrameLayout) view.findViewById(R.id.container_addidable);
        recordList = (RecyclerView) view.findViewById(R.id.list);
        saveButton = (Button) getActivity().findViewById(R.id.saveButton);
        buttonFAB = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);

        viewAddidable = ContentView.getViewEditableRecord(inflater,frameLayout,_type);
        TextView textView = viewAddidable.findViewById(R.id.time);
        textView.setVisibility(View.GONE);
        frameLayout.addView(viewAddidable);
        fab_toPreviousLevel = new FAB_ToPreviousLevel(buttonFAB);
        fab_toPreviousLevel.setActionsOnClick(this);
        fab_toPreviousLevel.actionsAfterInitialization();

        onScrollListenerRecyclerView = new OnScrollListenerRecyclerView(fab_toPreviousLevel);

        FoundListFilling.NotifyViews_after FoundList_notifyViews_after = new FoundListFilling.NotifyViews_after() {
            @Override
            public void ActionOfSearch() {
                fab_toPreviousLevel.actionsAfterInitialization();
                recordAdapter.typeView = 0;
                recordAdapter.posSelItem = -1;
                recordAdapter.notifyDataSetChanged();
            }

            @Override
            public void ActionDown() {
                fab_toPreviousLevel.actionsAfterActionsDown();
                recordAdapter.typeView = 1;
                recordAdapter.posSelItem = -1;
                recordAdapter.notifyDataSetChanged();
            }

            @Override
            public void ActionUp() {
                fab_toPreviousLevel.actionsAfterActionsUp();
                recordAdapter.typeView = 1;
                recordAdapter.posSelItem = -1;
                recordAdapter.notifyDataSetChanged();
            }

            @Override
            public void ToPreviousLevel() {
                if(foundList.cur_level==-1) {
                    fab_toPreviousLevel.actionsAfterToPreviousLevel(true);
                    recordAdapter.typeView = 0;
                }
                recordAdapter.posSelItem = foundList.selItemCurLevel;
                recordAdapter.notifyDataSetChanged();
                recordList.scrollToPosition(foundList.selItemCurLevel);
            }
        };

        foundList = new FoundListFilling(this.getContext(),FoundList_notifyViews_after,_type);

        initContainer();
        setListener();

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
                            field_id_ToAdd = 0;
                        } else {
                            oldPosSelItem = recordAdapter.posSelItem;
                            recordAdapter.posSelItem = position;
                            recordAdapter.notifyItemChanged(oldPosSelItem);
                            field_id_ToAdd = foundList.records.get(position).getRecord_id();
                        }
                    } else {
                        recordAdapter.posSelItem = position;
                        field_id_ToAdd = foundList.records.get(position).getRecord_id();

                    }

                    if (field_id_ToAdd == 0)
                        saveButton.setText(stringAddContent);
                    else
                        saveButton.setText("Добавить ассоциацию");

                    recordAdapter.notifyItemChanged(position);
                }
            }
        };

        recordAdapter = new RecordAdapter(this.getContext(), foundList.records,1, recordClickListener,recordLongClickListener);
        recordAdapter.setImageButtonUsed(false);
        recordAdapter.setAllowShowingPopupMenu(false);
        recordList.setAdapter(recordAdapter);
        recordList.addOnScrollListener(onScrollListenerRecyclerView);

        LinearLayoutManager layoutmanager = new LinearLayoutManager(this.getContext());
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
                    //recordAdapter.posSelItem = viewHolder.getAdapterPosition();
                    field_id_ToAdd = foundList.records.get(viewHolder.getAdapterPosition()).getRecord_id();
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

        return view;
    }

    protected void setStringAddContent(){
        stringAddContent = "Добавить новую запись";
    }

    protected void initContainer(){
        nameBox = viewAddidable.findViewById(R.id.name);
        setStringAddContent();
        saveButton.setText(stringAddContent);
    }

    protected void setListener(){
        nameBox.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                foundList.ActionOfSearch(s.toString());

                name_ToAdd = nameBox.getText().toString();

                if(name_ToAdd.length()>0)
                    saveButton.setEnabled(true);
                else
                    resetSelection();
            }
        });
    }

    private void resetSelection(){
        field_id_ToAdd = 0;
        recordAdapter.posSelItem = -1;
        saveButton.setText(stringAddContent);
        saveButton.setEnabled(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        foundList.Destroy();
    }

    @Override
    public void ToPreviousLevel() {
        foundList.ToPreviousLevel();
    }
}