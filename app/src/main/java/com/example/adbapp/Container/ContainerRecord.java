package com.example.adbapp.Container;

import com.example.adbapp.Interfaces.Fillable;

public abstract class ContainerRecord {
    Fillable fillable;

    protected void setFillable(Fillable fillable){
        this.fillable = fillable;
    }

    public void FillingContainer(){
        //fillable.fill();
    }
}
