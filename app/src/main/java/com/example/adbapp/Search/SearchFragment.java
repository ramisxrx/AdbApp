package com.example.adbapp.Search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
        recordAdapter = new RecordAdapter(getContext(), searchList.records,1, recordClickListener,recordLongClickListener);

        recordList.setAdapter(recordAdapter);

        LinearLayoutManager layoutmanager = new LinearLayoutManager(getContext());
        layoutmanager.setOrientation(LinearLayoutManager.VERTICAL);
        recordList.setLayoutManager(layoutmanager);

        recordList.addItemDecoration(new RecordDecoration(searchList.records));

        searchList.ActionOfInitialization(_type);

        parentContainer = factoryParentRecord.createInitialContainer(searchList.getCurrentParentRecord());
    }

    public void filter(int _type){
        parentContainer = factoryParentRecord.createInitialContainer(new Record(0,"РЕЗУЛЬТАТ ФИЛЬТРАЦИИ:",0,0));
    }
}