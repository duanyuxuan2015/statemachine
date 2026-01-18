package com.example;

import org.example.statemachine.Action;
import org.springframework.stereotype.Component;

@Component
public class Action1 implements Action<States,Events,Context> {
    @Override
    public void execute(States from, States to, Events event, Context context) {
        System.out.println("Action1");
    }
}
