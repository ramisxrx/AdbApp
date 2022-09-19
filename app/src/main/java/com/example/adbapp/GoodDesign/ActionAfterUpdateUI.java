package com.example.adbapp.GoodDesign;

public class ActionAfterUpdateUI implements StrategyOfAction{
    private final HandlerThreadOfActions handlerThread;
    private ActionBG actionBG;
    private ActionUI actionUI;

    public ActionAfterUpdateUI(HandlerThreadOfActions handlerThread, ActionBG actionBG, ActionUI actionUI){
        this.handlerThread = handlerThread;
        this.actionBG = actionBG;
        this.actionUI = actionUI;
    }

    @Override
    public void doAction() {
        handlerThread.bg_operations(new Runnable() {
            @Override
            public void run() {
                actionBG.doActionBG();

                handlerThread.ui_operations(new Runnable() {
                    @Override
                    public void run() {
                        actionUI.updateUI();
                    }
                });
            }
        });
    }
}
