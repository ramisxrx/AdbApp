package com.example.adbapp.GoodDesign;

public class InitList implements ActionInit{
    private RecordList recordList;
    private StrategyListFilling strategyListFilling;

    public InitList(RecordList recordList, StrategyListFilling strategyListFilling){
        this.recordList = recordList;
        this.strategyListFilling = strategyListFilling;
    }

    public void Initialization(){
        recordList = strategyListFilling.getFillList();
    }
}
