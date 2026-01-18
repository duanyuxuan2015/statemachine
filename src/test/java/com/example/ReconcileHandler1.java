package com.example;

import org.example.statemachine.ReconcileHandler;
import org.example.statemachine.StateMachine;
import org.springframework.stereotype.Component;

@Component
public class ReconcileHandler1 implements ReconcileHandler<States,Events,Context> {
    @Override
    public void handle(States state, Context context, StateMachine<States,Events,Context> stateMachine) {

        System.out.println("ReconcileHandler "+state);
        if(state.equals(States.STATE3)) stateMachine.fireEvent(state,Events.EVENT3,context);
    }
}
