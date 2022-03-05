package com.example.adbapp.AddingRecord;

public class AddDateFragment extends AddRecordFragment{

    public AddDateFragment(int _type) {
        super(_type);
    }

    @Override
    protected void setStringAddContent() {
        stringAddContent = "Добавить новую дату";
    }
}
