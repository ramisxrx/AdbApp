package com.example.adbapp.Fragments;

import android.content.Context;
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
import android.widget.Toast;

import com.example.adbapp.Container.ContainerRecord;
import com.example.adbapp.Container.FactoryParentRecord;
import com.example.adbapp.FillingOfList.AssociativeListFilling;
import com.example.adbapp.Interfaces.Associations_ActionsOfActivity;
import com.example.adbapp.PopupMenuOfRecord.ActionsPopupMenu;
import com.example.adbapp.PopupMenuOfRecord.CallPopupMenuContainer;
import com.example.adbapp.PopupMenuOfRecord.RecordPopupMenuAssociations;
import com.example.adbapp.R;
import com.example.adbapp.RecordList.OnScrollListenerRecyclerView;
import com.example.adbapp.RecordList.Record;
import com.example.adbapp.RecordList.RecordAdapter;
import com.example.adbapp.RecordList.RecordDecoration;
import com.example.adbapp.Threads.HandlerThreadOfFilling;
import com.example.adbapp.ToPreviousLevel.ActionsClickFAB;
import com.example.adbapp.ToPreviousLevel.FAB_ToPreviousLevel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AssociationsFragment extends Fragment implements CallPopupMenuContainer, ActionsClickFAB {

    private static final String TAG = "**AssociationsFragment*";


    private final Associations_ActionsOfActivity actionsOfActivity;
    private final ActionsPopupMenu actionsPopupMenu;

    FrameLayout frameLayout;
    RecyclerView recordList;

    HandlerThreadOfFilling BG_Thread;
    public ContainerRecord parentContainer;
    public FactoryParentRecord factoryParentRecord;
    RecordAdapter recordAdapter;
    public AssociativeListFilling associativeList;
    private final FAB_ToPreviousLevel fab_toPreviousLevel;
    private OnScrollListenerRecyclerView onScrollListenerRecyclerView;

    private Context context;
    boolean viewCreated=false;

    public AssociationsFragment(Context context, Associations_ActionsOfActivity actionsOfActivity, ActionsPopupMenu actionsPopupMenu, FAB_ToPreviousLevel fab_toPreviousLevel) {
        this.context = context;
        this.actionsOfActivity = actionsOfActivity;
        this.fab_toPreviousLevel = fab_toPreviousLevel;

        AssociativeListFilling.NotifyViews_after associativeList_notifyViews_after = new AssociativeListFilling.NotifyViews_after() {
            @Override
            public void ActionOfInitialization() {
                if(associativeList.hasAssociations) {
                    Toast toast = Toast.makeText(context,
                            "Ассоциации найдены! AssociationFragment", Toast.LENGTH_SHORT);
                    toast.show();
                    if(viewCreated) {
                        Log.d(TAG, "ActionOfInitialization: viewCreated= "+String.valueOf(viewCreated));
                        parentContainer = factoryParentRecord.recreateContainer(associativeList.getCurrentParentRecord(),parentContainer);
                        parentContainer.setVisibleImageButton(false);
                        recordAdapter.setAllowShowingPopupMenu(false);
                        fab_toPreviousLevel.actionsAfterInitialization();
                        recordAdapter.posSelItem = -1;
                        recordAdapter.notifyDataSetChanged();
                    }
                    else
                        actionsOfActivity.SwitchingToAssociationsMode();
                }else{
                    Toast toast = Toast.makeText(context,
                            "Ассоциации НЕ найдены! AssociationFragment", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void ActionDown() {
                parentContainer = factoryParentRecord.recreateContainer(associativeList.getCurrentParentRecord(),parentContainer);
                parentContainer.setVisibleImageButton(true);
                recordAdapter.setAllowShowingPopupMenu(true);
                fab_toPreviousLevel.actionsAfterActionsDown();
                recordAdapter.posSelItem = -1;
                recordAdapter.notifyDataSetChanged();
            }

            @Override
            public void ActionUp() {
                parentContainer = factoryParentRecord.recreateContainer(associativeList.getCurrentParentRecord(),parentContainer);
                if(associativeList.getCurrentParentRecord().getRecord_id()==0)
                    parentContainer.setVisibleImageButton(false);
                else
                    parentContainer.setVisibleImageButton(true);
                recordAdapter.setAllowShowingPopupMenu(true);
                fab_toPreviousLevel.actionsAfterActionsUp();
                recordAdapter.posSelItem = -1;
                recordAdapter.notifyDataSetChanged();
            }

            @Override
            public void ToPreviousLevel() {
                parentContainer = factoryParentRecord.recreateContainer(associativeList.getCurrentParentRecord(),parentContainer);
                if(associativeList.cur_level==0) {
                    parentContainer.setVisibleImageButton(false);
                    recordAdapter.setAllowShowingPopupMenu(false);
                    fab_toPreviousLevel.actionsAfterToPreviousLevel(true);
                }
                recordAdapter.posSelItem = associativeList.selItemCurLevel;
                recordAdapter.notifyDataSetChanged();
                recordList.scrollToPosition(associativeList.selItemCurLevel);
            }
        };

        BG_Thread = new HandlerThreadOfFilling("BG_AssociationsFragment");
        associativeList = new AssociativeListFilling(this.context,BG_Thread,associativeList_notifyViews_after);
        this.actionsPopupMenu = new ActionsPopupMenu() {
            @Override
            public void SwitchingToAddRecord() {

            }

            @Override
            public void CheckingAssociations(Record record) {
                associativeList.ActionOfInitialization(record.getRecord_id());
            }

            @Override
            public void SwitchingToEditing(Record record) {
                actionsPopupMenu.SwitchingToEditing(record);
            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        fab_toPreviousLevel.setActionsOnClick(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // использую макет overview так как одинаково
        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        frameLayout = (FrameLayout) view.findViewById(R.id.container_parent);
        recordList = (RecyclerView) view.findViewById(R.id.list);
        factoryParentRecord = new FactoryParentRecord(getContext(),frameLayout,this);
        factoryParentRecord.setVisibleImageButton(false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fab_toPreviousLevel.actionsAfterInitialization();

        RecordAdapter.OnRecordClickListener recordClickListener = (record, position) -> {
            //addRecord(record.getRecord_id());
        };

        RecordAdapter.OnRecordLongClickListener recordLongClickListener = new RecordAdapter.OnRecordLongClickListener() {
            @Override
            public void onRecordLongClick(int position) {
            }
        };
        parentContainer = factoryParentRecord.createInitialContainer(associativeList.getCurrentParentRecord());
        recordAdapter = new RecordAdapter(getContext(), associativeList.records,1, recordClickListener,recordLongClickListener);
        recordAdapter.setActionsPopupMenu(actionsPopupMenu);
        recordList.setAdapter(recordAdapter);

        onScrollListenerRecyclerView = new OnScrollListenerRecyclerView(fab_toPreviousLevel);
        recordList.addOnScrollListener(onScrollListenerRecyclerView);

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

        recordAdapter.setAllowShowingPopupMenu(false);
        recordAdapter.notifyDataSetChanged();

        viewCreated = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: ");
        viewCreated = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: "+context.getPackageResourcePath());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        associativeList.Destroy();
    }

    @Override
    public void callPopupMenuContainer(View view) {
        new RecordPopupMenuAssociations(getContext(), view, parentContainer.getRecord(), actionsPopupMenu);
    }

    @Override
    public void ToPreviousLevel() {
        associativeList.ToPreviousLevel();
    }
}