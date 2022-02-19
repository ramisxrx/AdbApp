package com.example.adbapp.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
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
import com.example.adbapp.PopupMenuOfRecord.RecordPopupMenu;
import com.example.adbapp.R;
import com.example.adbapp.RecordList.Record;
import com.example.adbapp.RecordList.RecordAdapter;
import com.example.adbapp.RecordList.RecordDecoration;
import com.example.adbapp.Threads.HandlerThreadOfFilling;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AssociationsFragment extends Fragment implements CallPopupMenuContainer {

    private static final String TAG = "**AssociationsFragment*";


    private final Associations_ActionsOfActivity actionsOfActivity;
    private final ActionsPopupMenu actionsPopupMenu;

    Button buttonLevelBack;
    FrameLayout frameLayout;
    RecyclerView recordList;
    FloatingActionButton buttonFAB;

    HandlerThreadOfFilling BG_Thread;
    public ContainerRecord parentContainer;
    public FactoryParentRecord factoryParentRecord;
    RecordAdapter recordAdapter;
    public AssociativeListFilling associativeList;

    private Context context;
    boolean viewCreated=false;

    public AssociationsFragment(Context context,Associations_ActionsOfActivity actionsOfActivity,ActionsPopupMenu actionsPopupMenu) {
        this.context = context;
        this.actionsOfActivity = actionsOfActivity;
        this.actionsPopupMenu = actionsPopupMenu;

        AssociativeListFilling.NotifyViews_after associativeList_notifyViews_after = new AssociativeListFilling.NotifyViews_after() {
            @Override
            public void ActionOfInitialization() {
                if(associativeList.hasAssociations) {
                    Toast toast = Toast.makeText(context,
                            "Ассоциации найдены! AssociationFragment", Toast.LENGTH_SHORT);
                    toast.show();
                    if(viewCreated) {
                        parentContainer = factoryParentRecord.recreateContainer(associativeList.getCurrentParentRecord(),parentContainer);
                        recordAdapter.notifyDataSetChanged();
                    }
                    else
                        actionsOfActivity.SwitchingToAssociationsMode();
                    //buttonFAB.setVisibility(View.GONE);
                }else{
                    Toast toast = Toast.makeText(context,
                            "Ассоциации НЕ найдены! AssociationFragment", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void ActionDown() {
                parentContainer = factoryParentRecord.recreateContainer(associativeList.getCurrentParentRecord(),parentContainer);
                recordAdapter.notifyDataSetChanged();
                buttonLevelBack.setVisibility(View.VISIBLE);
                buttonFAB.setVisibility(View.VISIBLE);
            }

            @Override
            public void ActionUp() {
                parentContainer = factoryParentRecord.recreateContainer(associativeList.getCurrentParentRecord(),parentContainer);
                recordAdapter.notifyDataSetChanged();
                buttonLevelBack.setVisibility(View.VISIBLE);
                buttonFAB.setVisibility(View.VISIBLE);
            }

            @Override
            public void ToPreviousLevel() {
                parentContainer = factoryParentRecord.recreateContainer(associativeList.getCurrentParentRecord(),parentContainer);
                recordAdapter.notifyDataSetChanged();
                recordList.scrollToPosition(associativeList.selItemCurLevel);
                if(associativeList.cur_level==0) {
                    buttonLevelBack.setVisibility(View.GONE);
                    buttonFAB.setVisibility(View.GONE);
                }
            }
        };

        BG_Thread = new HandlerThreadOfFilling("BG_AssociationsFragment");
        associativeList = new AssociativeListFilling(this.context,BG_Thread,associativeList_notifyViews_after);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
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
        factoryParentRecord = new FactoryParentRecord(getContext(),frameLayout,this);
        factoryParentRecord.setVisibleImageButton(false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecordAdapter.OnRecordClickListener recordClickListener = (record, position) -> {
            //addRecord(record.getRecord_id());
        };

        RecordAdapter.OnRecordLongClickListener recordLongClickListener = new RecordAdapter.OnRecordLongClickListener() {
            @Override
            public void onRecordLongClick(int position) {
                /*
                if(associativeList.cur_level!=0) {
                    Log.d(TAG, "onRecordLongClick: ");
                    Log.d(TAG, "onRecordLongClick: cur_level="+String.valueOf(associativeList.cur_level));
                    associativeList.ActionOfInitialization(associativeList.records.get(position).getRecord_id());
                }
                 */
                //actionsOfActivity.SwitchingToEditing(associativeList.records.get(position));
            }
        };
        parentContainer = factoryParentRecord.createInitialContainer(associativeList.getCurrentParentRecord());
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

        buttonFAB.setVisibility(View.GONE);

        recordAdapter.notifyDataSetChanged();

        buttonLevelBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                associativeList.ToPreviousLevel();
            }
        });

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecordPopupMenu recordPopupMenu = new RecordPopupMenu(getContext(),view,parentContainer.getRecord(),actionsPopupMenu);
            }
        });

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

    }
}