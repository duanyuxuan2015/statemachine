package org.example.statemachine.impl;

import org.example.statemachine.State;
import org.example.statemachine.Transition;
import org.example.statemachine.Visitor;
import java.util.Collection;
import java.util.List;

/**
 * 状态实现类
 *
 * @author Frank Zhang
 * @date 2020-02-07 11:19 PM
 */
public class StateImpl<S extends Enum<S>,E extends Enum<E>,C> implements State<S,E,C> {
    /**
     * 状态ID
     */
    protected final S stateId;
    /**
     * 事件转换映射表
     */
    private final EventTransitions<S,E,C> eventTransitions = new EventTransitions<>();

    /**
     * 构造函数
     *
     * @param stateId 状态ID
     */
    StateImpl(S stateId){
        this.stateId = stateId;
    }

    @Override
    public Transition<S, E, C> addTransition(E event, State<S,E,C> target, TransitionType transitionType) {
        Transition<S, E, C> newTransition = new TransitionImpl<>();
        newTransition.setSource(this);
        newTransition.setTarget(target);
        newTransition.setEvent(event);
        newTransition.setType(transitionType);

        Debugger.debug("Begin to add new transition: "+ newTransition);

        eventTransitions.put(event, newTransition);
        return newTransition;
    }

    @Override
    public List<Transition<S, E, C>> getEventTransitions(E event) {
        return eventTransitions.get(event);
    }

    @Override
    public Collection<Transition<S, E, C>> getAllTransitions() {
        return eventTransitions.allTransitions();
    }

    @Override
    public S getId() {
        return stateId;
    }

    @Override
    public String accept(Visitor visitor) {
        String entry = visitor.visitOnEntry(this);
        String exit = visitor.visitOnExit(this);
        return entry + exit;
    }

    @Override
    public boolean equals(Object anObject){
        if(anObject instanceof State){
            State other = (State)anObject;
            if(this.stateId.equals(other.getId()))
                return true;
        }
        return false;
    }

    @Override
    public String toString(){
        return stateId.toString();
    }
}
