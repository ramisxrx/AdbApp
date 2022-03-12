package com.example.adbapp.AddingRecord;

import android.os.Build;
import android.widget.DatePicker;


import com.example.adbapp.ContentView;
import com.example.adbapp.R;

import java.util.Calendar;
import java.util.Date;

public class AddDateFragment extends AddRecordFragment{

    DatePicker datePicker;

    public AddDateFragment(int _type) {
        super(_type);
    }

    @Override
    protected void setStringAddContent() {
        stringAddContent = "Добавить новую дату";
    }

    @Override
    protected void initContainer() {
        datePicker = viewAddidable.findViewById(R.id.datePicker);
        setStringAddContent();
        saveButton.setText(stringAddContent);
        saveButton.setEnabled(true);
    }

    @Override
    protected void setListener() {
        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        foundList.ActionOfSearch(ContentView.getDateForDates(date));
        name_ToAdd = ContentView.getDateForDates(date);
        datePicker.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(i,i1,i2);
                Date date = calendar.getTime();

                foundList.ActionOfSearch(ContentView.getDateForDates(date));
                name_ToAdd = ContentView.getDateForDates(date);
            }
        });
    }
}
