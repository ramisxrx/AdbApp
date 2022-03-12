package com.example.adbapp.AddingRecord;

import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.adbapp.ContentView;
import com.example.adbapp.R;

import java.util.Calendar;
import java.util.Date;

public class AddTimeFragment extends AddRecordFragment{

    TimePicker timePicker;

    public AddTimeFragment(int _type) {
        super(_type);
    }

    @Override
    protected void setStringAddContent() {
        stringAddContent = "Добавить новое время";
    }

    @Override
    protected void initContainer() {
        timePicker = viewAddidable.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        setStringAddContent();
        saveButton.setText(stringAddContent);
        saveButton.setEnabled(true);
    }

    @Override
    protected void setListener() {
        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        foundList.ActionOfSearch(ContentView.getTimeForTimes(date));
        name_ToAdd = ContentView.getTimeForTimes(date);

        timePicker.setCurrentHour(c.get(c.HOUR_OF_DAY));
        timePicker.setCurrentMinute(c.get(c.MINUTE));

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(2000,01,01,hourOfDay,minute);
                Date date = calendar.getTime();

                foundList.ActionOfSearch(ContentView.getTimeForTimes(date));
                name_ToAdd = ContentView.getTimeForTimes(date);
            }
        });
    }
}
