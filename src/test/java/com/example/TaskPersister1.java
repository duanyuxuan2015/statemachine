package com.example;

import com.alibaba.fastjson2.JSON;
import org.example.statemachine.ReconcileTask;
import org.example.statemachine.State;
import org.example.statemachine.StateMachine;
import org.example.statemachine.TaskPersister;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TaskPersister1 implements TaskPersister<States,Events,Context> {

    private String stateMachineId;

    private final Map<States,ReconcileTask> reconcileTaskMap = new ConcurrentHashMap<>();


    private final Map<States,Timer> statesTimerConcurrentHashMap = new ConcurrentHashMap<>();
    @Override
    public void saveTask(String stateMachineId,States state, Context context, ReconcileTask<States,Events,Context> reconcileTask) {
        this.stateMachineId = stateMachineId;
        reconcileTaskMap.put(state,reconcileTask);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(reconcileTaskMap.containsKey(state)){
                    StateMachine stateMachine = StateMachineContainer.getStateMachine(stateMachineId);
                    stateMachine.reconcile(state,new Context());
                }
            }
        },reconcileTask.getPeriod()*1000,reconcileTask.getPeriod()*1000);
        statesTimerConcurrentHashMap.put(state,timer);
        System.out.printf("save task %s \n" , state);
    }

    @Override
    public void deleteTask(String stateMachineId,States state, Context context) {

        Timer timer = statesTimerConcurrentHashMap.get(state);
        if(timer!=null) timer.cancel();
        reconcileTaskMap.remove(state);
        statesTimerConcurrentHashMap.remove(state);
        System.out.printf("delete task %s \n" , state);

    }


}
