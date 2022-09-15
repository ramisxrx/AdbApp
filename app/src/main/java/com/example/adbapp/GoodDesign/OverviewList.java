package com.example.adbapp.GoodDesign;

public class OverviewList implements ActionInit{
    private FillInitList fillInitList;

    public OverviewList(FillInitList fillInitList){
        this.fillInitList = fillInitList;
    }

    public void Initialization(){
        fillInitList.getInitList();
    }
}
