package com.example.adbapp.EditingRecord;

import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.adbapp.ContentView;
import com.example.adbapp.R;

import java.util.Calendar;
import java.util.Date;

public class EditableTime extends Editable{
    private TextView timeView;
    private TimePicker timePicker;

    public EditableTime(){
    }

    @Override
    void fillView() {
        timeView.setText("Последнее изменение: "+ContentView.getDateTime(record.getTime()));

        Calendar c = Calendar.getInstance();
        c.setTime(ContentView.getTimeForTimes(record.getName()));

        timePicker.setCurrentHour(c.get(c.HOUR_OF_DAY));
        timePicker.setCurrentMinute(c.get(c.MINUTE));

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(2000,01,01,hourOfDay,minute);
                Date date = calendar.getTime();

                changing(ContentView.getTimeForTimes(date),record.getName());
            }
        });

    }

    @Override
    void findingViews() {
        timePicker = view.findViewById(R.id.timePicker);
        timeView = (TextView) view.findViewById(R.id.time);
    }

}
