package com.tscript.lang.runtime.tni.std;

import com.tscript.lang.runtime.core.Data;
import com.tscript.lang.runtime.core.TThread;
import com.tscript.lang.runtime.type.Callable;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class EventManager {

    private static EventManager instance;

    public static EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }

    private EventManager() {
    }

    private Callable timerHandler;
    private Callable mouseMoveHandler;
    private Callable keyDownHandler;

    private TThread handlerThread;
    private volatile Data returnValue;

    public void setMouseMoveHandler(Callable mouseMoveHandler) {
        this.mouseMoveHandler = mouseMoveHandler;
    }

    public void setKeyDownHandler(Callable keyDownHandler) {
        this.keyDownHandler = keyDownHandler;
    }

    public void setTimerHandler(Callable timerHandler) {
        this.timerHandler = timerHandler;
    }

    public Data enterEventMode(TThread thread){
        if (thread.getThreadID() != 0) throw new AssertionError();
        handlerThread = thread;
        while (returnValue == null && handlerThread.isRunning()){
            if (timerHandler != null)
                thread.call(timerHandler, new ArrayList<>());
        }
        handlerThread = null; // avoid potential memory leak
        return returnValue;
    }

    public synchronized void quitEventMode(Data returnValue){
        this.returnValue = returnValue;
    }

    public void fireMouseMoveEvent(){
        handlerThread.startNewThread(mouseMoveHandler, List.of(/* impl me later */));
    }

}
