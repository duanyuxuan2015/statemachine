package com.example;

import org.example.statemachine.StateMachine;

import java.util.HashMap;
import java.util.Map;

public class StateMachineContainer {
    private static Map<String, StateMachine> stateMachineMap=new HashMap<>();
    public static StateMachine getStateMachine(String machineId){
        return stateMachineMap.get(machineId);
    }
    public static void addStateMachine(String machineId,StateMachine stateMachine){
        stateMachineMap.put(machineId,stateMachine);
    }
}
