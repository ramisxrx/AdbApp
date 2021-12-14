package com.example.adbapp;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class RecordDiffUtilCallback extends DiffUtil.Callback{

    private final List<Record> oldList;
    private final List<Record> newList;

    public RecordDiffUtilCallback(List<Record> oldList, List<Record> newList){
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getRecord_id()==newList.get(newItemPosition).getRecord_id();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getName().equals(newList.get(newItemPosition).getName())
                && oldList.get(oldItemPosition).getTime()==newList.get(newItemPosition).getTime();
                //&& oldList.get(oldItemPosition).getLevel()==newList.get(newItemPosition).getLevel();
    }
}
