package com.example;

import org.example.statemachine.Condition;
import org.springframework.stereotype.Component;

@Component
public class Condition1 implements Condition<Context> {
    @Override
    public boolean isSatisfied(Context context) {
        return true;
    }
}
