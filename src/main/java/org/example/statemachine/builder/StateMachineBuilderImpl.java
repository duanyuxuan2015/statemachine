package  org.example.statemachine.builder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.example.statemachine.*;
import org.example.statemachine.impl.StateMachineImpl;
import org.example.statemachine.impl.TransitionType;

/**
 * StateMachineBuilderImpl
 *
 * @author Frank Zhang
 * @date 2020-02-07 9:40 PM
 */
public class StateMachineBuilderImpl<S extends Enum<S>, E extends Enum<E>, C> implements StateMachineBuilder<S, E, C> {

    /**
     * StateMap is the same with stateMachine, as the core of state machine is holding reference to states.
     */
    private final Map<S, State<S, E, C>> stateMap = new ConcurrentHashMap<>();
    private final StateMachineImpl<S, E, C> stateMachine = new StateMachineImpl<>(stateMap);
    private StateMachineListener<S, E, C> stateMachineListener;
    private TaskPersister<S,E,C> taskPersister;
    private final Map<S, ReconcileTask<S,E,C>> reconcileTaskMap = new HashMap<>();

    @Override
    public ExternalTransitionBuilder<S, E, C> externalTransition() {
        return new TransitionBuilderImpl<>(stateMap, TransitionType.EXTERNAL);
    }

    @Override
    public ExternalTransitionsBuilder<S, E, C> externalTransitions() {
        return new TransitionsBuilderImpl<>(stateMap, TransitionType.EXTERNAL);
    }

    @Override
    public InternalTransitionBuilder<S, E, C> internalTransition() {
        return new TransitionBuilderImpl<>(stateMap, TransitionType.INTERNAL);
    }


    @Override
    public void addListener(StateMachineListener<S,E,C> stateMachineListener) {
        this.stateMachineListener = stateMachineListener;
    }

    @Override
    public void setTaskPersister(TaskPersister<S,E,C> taskPersister) {
        this.taskPersister = taskPersister;
    }

    @Override
    public void addReconcileTask(S state, ReconcileTask<S,E,C> reconcileTask) {
        this.reconcileTaskMap.put(state,reconcileTask);
    }


    @Override
    public StateMachine<S,E,C> build(String machineId) {
        stateMachine.setMachineId(machineId);
        stateMachine.setReady(true);
        stateMachine.addListener(stateMachineListener);
        stateMachine.setTaskPersister(taskPersister);
        stateMachine.setReconcileTaskMap(reconcileTaskMap);
        StateMachineFactory.register(stateMachine);
        return stateMachine;
    }

}
