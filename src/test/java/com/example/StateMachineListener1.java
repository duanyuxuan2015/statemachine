package com.example;

import org.example.statemachine.StateMachineListener;

public class StateMachineListener1 implements StateMachineListener<States,Events,Context> {
    @Override
    public void eventNotAccepted(States state, Events event, Context context) {
        System.out.printf("eventNotAccepted , state : %s , event %s %n",state,event);
    }

    @Override
    public void onTransition(States source, States target, Events event, Context context) {
        System.out.printf("transit from  state : %s  to  state %s on event %s %n",source,target,event);
    }

    @Override
    public void onException(States state, Events event, Context context, Throwable throwable) {
        System.out.printf("exception %s  state : %s  on event %s %n",throwable,state,event);
    }
}
