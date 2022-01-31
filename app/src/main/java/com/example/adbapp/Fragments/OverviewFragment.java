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

import com.example.adbapp.Container.ParentRecordBase;
import com.example.adbapp.FillingOfList.AssociativeListFilling;
import com.example.adbapp.FillingOfList.OverviewListFilling;
import com.example.adbapp.R;
import com.example.adbapp.RecordList.Record;
import com.example.adbapp.RecordList.RecordAdapter;
import com.example.adbapp.RecordList.RecordDecoration;
import com.example.adbapp.Threads.HandlerThreadOfFilling;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OverviewFragment extends Fragment {

    private static final String TAG = "**OverviewFragment**";

    public interface ActionsOfActivity{
        void CheckingAssociations(int record_id);
        void SwitchingToEditing(Record record);
    }

    private final ActionsOfActivity actionsOfActivity;

    Button buttonLevelBack;
    FrameLayout frameLayout;
    FloatingActionButton buttonFAB;

    HandlerThreadOfFilling BG_Thread;
    public RecordContainer recordContainer;
    public ParentRecordBase parentRecordBase;
    RecordAdapter recordAdapter;
    public OverviewListFilling overviewList;

    public OverviewFragment(ActionsOfActivity actionsOfActivity) {
        this.actionsOfActivity = actionsOfActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BG_Thread = new HandlerThreadOfFilling("BG_OverviewFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        frameLayout = (FrameLayout) view.findViewById(R.id.container_parent);
        buttonLevelBack = view.findViewById(R.id.buttonLevelBack);
        RecyclerView recordList = (RecyclerView) view.findViewById(R.id.list);
        buttonFAB = (FloatingActionButton) getActivity().findViewById(R.id.floatingActionButton);
        recordContainer = new RecordContainer(getContext(),frameLayout);

        parentRecordBase = new ParentRecordBase(getContext(),frameLayout);

        RecordAdapter.OnRecordClickListener recordClickListener = (record, position) -> {
            //addRecord(record.getRecord_id());
        };

        RecordAdapter.OnRecordLongClickListener recordLongClickListener = new RecordAdapter.OnRecordLongClickListener() {
            @Override
            public void onRecordLongClick(int position) {
                //actionsOfActivity.CheckingAssociations(overviewList.records.get(position).getRecord_id());
                actionsOfActivity.SwitchingToEditing(overviewList.records.get(position));
            }
        };

        OverviewListFilling.NotifyViews_after notifyViews_after = new OverviewListFilling.NotifyViews_after() {
            @Override
            public void ActionDown() {
                recordContainer.FillingContainer(overviewList.parentRecordByLevel.get(overviewList.cur_level),1);
                recordAdapter.notifyDataSetChanged();
                buttonLevelBack.setVisibility(View.VISIBLE);
            }

            @Override
            public void ToPreviousLevel() {
                recordContainer.FillingContainer(overviewList.parentRecordByLevel.get(overviewList.cur_level),1);
                //recordList.smoothScrollToPosition(overviewList.selItemCurLevel);
                recordAdapter.notifyDataSetChanged();
                recordList.scrollToPosition(overviewList.selItemCurLevel);
                if(overviewList.cur_level==0)
                    buttonLevelBack.setVisibility(View.GONE);
            }

            @Override
            public void UpdateAfterAddNewRecords() {
                recordAdapter.notifyItemInserted(overviewList.records.size());
            }
        };

        overviewList = new OverviewListFilling(getContext(),BG_Thread,notifyViews_after);

        recordContainer.FillingContainer(overviewList.parentRecordByLevel.get(overviewList.cur_level),0);
        recordAdapter = new RecordAdapter(getContext(), overviewList.records,1, recordClickListener,recordLongClickListener);
        recordList.setAdapter(recordAdapter);

        LinearLayoutManager layoutmanager = new LinearLayoutManager(getContext());
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        recordList.setLayoutManager(layoutmanager);

        recordList.addItemDecoration(new RecordDecoration(overviewList.records));


        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public int getMovementFlags (RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int swipeFlags = ItemTouchHelper.LEFT;
                int dragFlags = 0;

                return makeMovementFlags(dragFlags,swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Log.d(TAG, "onSwiped: ");

                overviewList.ActionDown(viewHolder.getAdapterPosition());

                //recordAdapter.notifyDataSetChanged();
            }
        };

        ItemTouchHelper touchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        touchHelper.attachToRecyclerView(recordList);

        buttonLevelBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overviewList.ToPreviousLevel();
                overviewList.bdView();
            }
        });

        buttonFAB.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        overviewList.Destroy();
    }
}