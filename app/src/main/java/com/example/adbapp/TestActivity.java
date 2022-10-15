package com.example.adbapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.adbapp.GoodDesign.RecordListAdapter;
import com.example.adbapp.GoodDesign.Threads.Action;
import com.example.adbapp.GoodDesign.Threads.HandlerThreadBG;
import com.example.adbapp.GoodDesign.Threads.ThreadRunnable;
import com.example.adbapp.Test.ActionForTest;

public class TestActivity extends AppCompatActivity {

    String TAG = getClass().getCanonicalName();

    RecyclerView recyclerView;
    RecordListAdapter recordListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutmanager);
        recordListAdapter = new RecordListAdapter(this);

        recyclerView.setAdapter(recordListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}