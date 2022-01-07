package com.example.adbapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.adbapp.FillingOfList.AssociativeListFilling;
import com.example.adbapp.R;
import com.example.adbapp.RecordList.RecordAdapter;
import com.example.adbapp.RecordList.RecordDecoration;
import com.example.adbapp.Threads.HandlerThreadOfFilling;

public class AssociationsFragment extends Fragment {

    private static final String TAG = "**AssociationsFragment*";

    Button buttonLevelBack;
    FrameLayout frameLayout;

    HandlerThreadOfFilling BG_Thread;
    RecordContainer recordContainer;
    RecordAdapter recordAdapter;
    AssociativeListFilling associativeList;

    public AssociationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BG_Thread = new HandlerThreadOfFilling("BG_AssociationsFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_associations, container, false);

        frameLayout = (FrameLayout) view.findViewById(R.id.container_parent);
        buttonLevelBack = view.findViewById(R.id.buttonLevelBack);
        RecyclerView recordList = (RecyclerView) view.findViewById(R.id.list);
        recordContainer = new RecordContainer(getContext(),frameLayout);

        RecordAdapter.OnRecordClickListener recordClickListener = (record, position) -> {
            //addRecord(record.getRecord_id());
        };

        RecordAdapter.OnRecordLongClickListener recordLongClickListener = new RecordAdapter.OnRecordLongClickListener() {
            @Override
            public void onRecordLongClick(int position) {
                if(associativeList.cur_level!=0)
                    associativeList.ActionOfInitialization(associativeList.records.get(position).getRecord_id());
            }
        };

        AssociativeListFilling.NotifyViews_after associativeList_notifyViews_after = new AssociativeListFilling.NotifyViews_after() {
            @Override
            public void ActionOfInitialization() {
                if(associativeList.hasAssociations) {
                    Toast toast = Toast.makeText(getContext(),
                            "Ассоциации найдены!", Toast.LENGTH_SHORT);
                    toast.show();
                    SwitchingOfRecordList(true);
                }else{
                    Toast toast = Toast.makeText(getContext(),
                            "Ассоциации НЕ найдены!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void ActionDown() {
                recordAdapter.notifyDataSetChanged();
                buttonLevelBack.setVisibility(View.VISIBLE);
            }

            @Override
            public void ActionUp() {
                recordAdapter.notifyDataSetChanged();
                buttonLevelBack.setVisibility(View.VISIBLE);
            }

            @Override
            public void ToPreviousLevel() {
                recordAdapter.notifyDataSetChanged();
                if(associativeList.cur_level==0)
                    buttonLevelBack.setVisibility(View.GONE);
            }
        };

        associativeList = new AssociativeListFilling(getContext(),BG_Thread,associativeList_notifyViews_after);

        recordContainer.FillingContainer(associativeList.parentRecordByLevel.get(overviewList.cur_level),0);
        recordAdapter = new RecordAdapter(getContext(), associativeList.records,1, recordClickListener,recordLongClickListener);
        recordList.setAdapter(recordAdapter);

        LinearLayoutManager layoutmanager = new LinearLayoutManager(getContext());
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        recordList.setLayoutManager(layoutmanager);

        recordList.addItemDecoration(new RecordDecoration(associativeList.records));


        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public int getMovementFlags (RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int swipeFlags = 0;
                int dragFlags = 0;

                if(associativeList.cur_level==0)
                    swipeFlags = ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;
                else{
                    if(associativeList.selDirection)
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
                Log.d(TAG, "onSwiped: ");
                if(direction==4)
                    associativeList.ActionDown(viewHolder.getAdapterPosition());
                else
                    associativeList.ActionUp(viewHolder.getAdapterPosition());

                //recordAdapter.notifyDataSetChanged();
            }
        };

        ItemTouchHelper touchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        touchHelper.attachToRecyclerView(recordList);

        return view;
    }
}