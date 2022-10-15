package com.example.adbapp.GoodDesign.Threads;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

public class ChainOfRunnable {

    private final byte numberOfChains;
    private  final byte numberOfLinks;
    private final Handler handler;

    private List<Action> actionList;

    private final List<ThreadRunnable> listChain;

    public ChainOfRunnable(byte numberOfChains, byte numberOfLinks, Handler handler) {
        this.numberOfChains = numberOfChains;
        this.numberOfLinks = numberOfLinks;
        this.handler = handler;

        listChain = new ArrayList<>();

        for (int i=0;i<numberOfChains;i++){
            listChain.add(i,new ThreadRunnable(handler,"ChainOfRunnable_"+String.valueOf(i)));
        }
    }
}
