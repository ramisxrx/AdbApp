package com.example.adbapp.Search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.example.adbapp.Container.ContainerRecord;
import com.example.adbapp.Container.FactoryParentRecord;
import com.example.adbapp.FillingOfList.AssociativeListFilling;
import com.example.adbapp.FillingOfList.SearchListFilling;
import com.example.adbapp.R;
import com.example.adbapp.RecordList.Record;
import com.example.adbapp.RecordList.RecordAdapter;
import com.example.adbapp.RecordList.RecordDecoration;
import com.example.adbapp.Threads.HandlerThreadOfFilling;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SearchFragment extends Fragment {

    private static final String TAG = "**SearchFragment*";

    Button buttonLevelBack;
    FrameLayout frameLayout;
    RecyclerView recordList;
    FloatingActionButton buttonFAB;

    HandlerThreadOfFilling BG_Thread;
    SearchListFilling searchList;

    RecordAdapter recordAdapter;

    public FactoryParentRecord factoryParentRecord;
    public ContainerRecord parentContainer;

    int _type;

    public SearchFragment(int _type) {
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
        // использую макет overview так как одинаково
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        frameLayout = (FrameLayout) view.findViewById(R.id.container_parent);
        buttonLevelBack = view.findViewById(R.id.buttonLevelBack);
        recordList = (RecyclerView) view.findViewById(R.id.list);
        buttonFAB = (FloatingActionButton) getActivity().findViewById(R.id.floatingActionButton);
        factoryParentRecord = new FactoryParentRecord(getContext(),frameLayout);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BG_Thread = new HandlerThreadOfFilling("BG_AssociationsFragment");

        AssociativeListFilling.NotifyViews_after notifyViews_after = new AssociativeListFilling.NotifyViews_after() {
            @Override
            public void ActionOfInitialization() {
                parentContainer = factoryParentRecord.createInitialContainer(searchList.getCurrentParentRecord());
            }

            @Override
            public void ActionDown() {
                parentContainer = factoryParentRecord.recreateContainer(searchList.getCurrentParentRecord(),parentContainer);
                recordAdapter.notifyDataSetChanged();
                buttonLevelBack.setVisibility(View.VISIBLE);
                buttonFAB.setVisibility(View.VISIBLE);
            }

            @Override
            public void ActionUp() {
                parentContainer = factoryParentRecord.recreateContainer(searchList.getCurrentParentRecord(),parentContainer);
                recordAdapter.notifyDataSetChanged();
                buttonLevelBack.setVisibility(View.VISIBLE);
                buttonFAB.setVisibility(View.VISIBLE);
            }

            @Override
            public void ToPreviousLevel() {
                parentContainer = factoryParentRecord.recreateContainer(searchList.getCurrentParentRecord(),parentContainer);
                recordAdapter.notifyDataSetChanged();
                recordList.scrollToPosition(searchList.selItemCurLevel);
                if(searchList.cur_level==0) {
                    buttonLevelBack.setVisibility(View.GONE);
                    buttonFAB.setVisibility(View.GONE);
                }
            }
        };

        searchList = new SearchListFilling(getContext(),BG_Thread,notifyViews_after);
        searchList.ActionOfInitialization(_type);

        RecordAdapter.OnRecordClickListener recordClickListener = (record, position) -> {
            //addRecord(record.getRecord_id());
        };

        RecordAdapter.OnRecordLongClickListener recordLongClickListener = new RecordAdapter.OnRecordLongClickListener() {
            @Override
            public void onRecordLongClick(int position) {

            }
        };
        recordAdapter = new RecordAdapter(getContext(), searchList.records,1, recordClickListener,recordLongClickListener);

        recordList.setAdapter(recordAdapter);

        LinearLayoutManager layoutmanager = new LinearLayoutManager(getContext());
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        recordList.setLayoutManager(layoutmanager);

        recordList.addItemDecoration(new RecordDecoration(searchList.records));

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public int getMovementFlags (RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int swipeFlags = 0;
                int dragFlags = 0;

                if(searchList.cur_level==0)
                    swipeFlags = ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;
                else{
                    if(searchList.selDirection)
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
                    searchList.ActionDown(viewHolder.getAdapterPosition());
                else
                    searchList.ActionUp(viewHolder.getAdapterPosition());

                //recordAdapter.notifyDataSetChanged();
            }
        };

        ItemTouchHelper touchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        touchHelper.attachToRecyclerView(recordList);

        buttonLevelBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchList.ToPreviousLevel();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        searchList.Destroy();
    }

    public void filter(int _type){
        parentContainer = factoryParentRecord.createInitialContainer(new Record(0,"РЕЗУЛЬТАТ ФИЛЬТРАЦИИ:",0,0));
    }
}