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
import android.widget.FrameLayout;

import com.example.adbapp.Container.ContainerRecord;
import com.example.adbapp.Container.FactoryParentRecord;
import com.example.adbapp.FillingOfList.OverviewListFilling;
import com.example.adbapp.PopupMenuOfRecord.ActionsPopupMenu;
import com.example.adbapp.PopupMenuOfRecord.CallPopupMenuContainer;
import com.example.adbapp.PopupMenuOfRecord.RecordPopupMenu;
import com.example.adbapp.R;
import com.example.adbapp.RecordList.OnScrollListenerRecyclerView;
import com.example.adbapp.RecordList.Record;
import com.example.adbapp.RecordList.RecordAdapter;
import com.example.adbapp.RecordList.RecordDecoration;
import com.example.adbapp.Threads.HandlerThreadOfFilling;
import com.example.adbapp.ToPreviousLevel.ActionsClickFAB;
import com.example.adbapp.ToPreviousLevel.FAB_ToPreviousLevel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class OverviewFragment extends Fragment implements CallPopupMenuContainer, ActionsClickFAB {

    private static final String TAG = "**OverviewFragment**";

    @Override
    public void callPopupMenuContainer(View view) {
        RecordPopupMenu recordPopupMenu = new RecordPopupMenu(getContext(),view,parentContainer.getRecord(),actionsPopupMenu);
    }

    @Override
    public void ToPreviousLevel() {
        overviewList.ToPreviousLevel();
        overviewList.bdView();
    }

    public interface ActionsOfActivity{
        void CheckingAssociations(int record_id);
        void SwitchingToEditing(Record record);
    }

    private final ActionsOfActivity actionsOfActivity;

    FrameLayout frameLayout;

    HandlerThreadOfFilling BG_Thread;
    public FactoryParentRecord factoryParentRecord;
    public ContainerRecord parentContainer;
    RecordAdapter recordAdapter;
    public OverviewListFilling overviewList;
    private final ActionsPopupMenu actionsPopupMenu;
    private final FAB_ToPreviousLevel fab_toPreviousLevel;
    private OnScrollListenerRecyclerView onScrollListenerRecyclerView;

    public OverviewFragment(ActionsOfActivity actionsOfActivity,ActionsPopupMenu actionsPopupMenu, FAB_ToPreviousLevel fab_toPreviousLevel) {
        this.actionsOfActivity = actionsOfActivity;
        this.actionsPopupMenu = actionsPopupMenu;
        this.fab_toPreviousLevel = fab_toPreviousLevel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");

        BG_Thread = new HandlerThreadOfFilling("BG_OverviewFragment");
        fab_toPreviousLevel.setActionsOnClick(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        onScrollListenerRecyclerView = new OnScrollListenerRecyclerView(fab_toPreviousLevel);

        frameLayout = (FrameLayout) view.findViewById(R.id.container_parent);
        RecyclerView recordList = (RecyclerView) view.findViewById(R.id.list);

        factoryParentRecord = new FactoryParentRecord(getContext(),frameLayout,this);
        fab_toPreviousLevel.actionsAfterInitialization();

        RecordAdapter.OnRecordClickListener recordClickListener = (record, position) -> {
        };

        RecordAdapter.OnRecordLongClickListener recordLongClickListener = new RecordAdapter.OnRecordLongClickListener() {
            @Override
            public void onRecordLongClick(int position) {
            }
        };

        OverviewListFilling.NotifyViews_after notifyViews_after = new OverviewListFilling.NotifyViews_after() {
            @Override
            public void ActionDown() {
                parentContainer = factoryParentRecord.recreateContainer(overviewList.getCurrentParentRecord(),parentContainer);
                recordAdapter.posSelItem = -1;
                recordAdapter.notifyDataSetChanged();
                fab_toPreviousLevel.actionsAfterActionsDown();
            }

            @Override
            public void ToPreviousLevel() {
                parentContainer = factoryParentRecord.recreateContainer(overviewList.getCurrentParentRecord(),parentContainer);
                recordAdapter.posSelItem = overviewList.selItemCurLevel;
                recordAdapter.notifyDataSetChanged();
                recordList.scrollToPosition(overviewList.selItemCurLevel);
                fab_toPreviousLevel.actionsAfterToPreviousLevel(overviewList.cur_level==0);
            }

            @Override
            public void UpdateAfterAddNewRecords() {
                recordAdapter.notifyItemInserted(overviewList.records.size());
            }

            @Override
            public void UpdateAfterUpdateDeleteRecords() {
                parentContainer = factoryParentRecord.recreateContainer(overviewList.getCurrentParentRecord(),parentContainer);
                recordAdapter.notifyDataSetChanged();
            }
        };

        overviewList = new OverviewListFilling(getContext(),BG_Thread,notifyViews_after);

        //factoryParentRecord.FillingContainer(overviewList.parentRecordByLevel.get(overviewList.cur_level));
        parentContainer = factoryParentRecord.createInitialContainer(overviewList.getCurrentParentRecord());

        recordAdapter = new RecordAdapter(getContext(), overviewList.records,1, recordClickListener,recordLongClickListener);
        recordAdapter.setActionsPopupMenu(actionsPopupMenu);
        recordList.setAdapter(recordAdapter);

        recordList.addOnScrollListener(onScrollListenerRecyclerView);

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
            }
        };

        ItemTouchHelper touchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        touchHelper.attachToRecyclerView(recordList);

        return view;
    }

    public void UpdateAfterUpdating(){
        overviewList.UpdateAfterUpdateDeleteRecords(true);
    }

    public void UpdateAfterDeleting(){
        overviewList.UpdateAfterUpdateDeleteRecords(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        overviewList.Destroy();
    }
}