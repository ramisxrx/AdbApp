package com.example.adbapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.adbapp.FillingOfList.FoundListFilling;
import com.example.adbapp.R;
import com.example.adbapp.RecordList.Record;
import com.example.adbapp.RecordList.RecordAdapter;
import com.example.adbapp.RecordList.RecordDecoration;

public class AddTextFragment extends Fragment {

    private static final String TAG = "*AddTextFragment*";
    
    EditText nameBox;
    RecyclerView recordList;
    Button buttonLevelBack;
    Button saveButton;
    RecordAdapter recordAdapter;
    RecordAdapter.OnRecordClickListener recordClickListener;

    FoundListFilling foundList;

    public String name_ToAdd;
    public int field_id_ToAdd;

    public AddTextFragment() {
        // Required empty public constructor
        Log.d(TAG, "AddTextFragment: ");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        
        View view = inflater.inflate(R.layout.fragment_add_text, container, false);
        
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");
        
        nameBox = (EditText) view.findViewById(R.id.name);

        saveButton = (Button) getActivity().findViewById(R.id.saveButton);
        saveButton.setText("Добавить новый текст");

        nameBox.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                name_ToAdd = nameBox.getText().toString();

                saveButton.setEnabled(name_ToAdd.length()>0);
            }
        });

        name_ToAdd = nameBox.getText().toString();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }
}