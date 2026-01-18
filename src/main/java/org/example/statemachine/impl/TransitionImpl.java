package org.example.statemachine.impl;


import org.example.statemachine.Action;
import org.example.statemachine.Condition;
import org.example.statemachine.State;
import org.example.statemachine.Transition;

/**
 * 转换实现类
 * <p>该类应设计为不可变的，以避免线程安全问题</p>
 *
 * @author Frank Zhang
 * @date 2020-02-07 10:32 PM
 */
public class TransitionImpl<S extends Enum<S>,E extends Enum<E>,C> implements Transition<S,E,C> {

    /**
     * 源状态
     */
    private State<S, E, C> source;

    /**
     * 目标状态
     */
    private State<S, E, C> target;

    /**
     * 触发事件
     */
    private E event;

    /**
     * 守卫条件
     */
    private Condition<C> condition;

    /**
     * 执行动作
     */
    private Action<S,E,C> action;

    /**
     * 转换类型
     */
    private TransitionType type = TransitionType.EXTERNAL;

    @Override
    public State<S, E, C> getSource() {
        return source;
    }

    @Override
    public void setSource(State<S, E, C> state) {
        this.source = state;
    }

    @Override
    public E getEvent() {
        return this.event;
    }

    @Override
    public void setEvent(E event) {
        this.event = event;
    }

    @Override
    public void setType(TransitionType type) {
        this.type = type;
    }

    @Override
    public State<S, E, C> getTarget() {
        return this.target;
    }

    @Override
    public void setTarget(State<S, E, C> target) {
        this.target = target;
    }

    @Override
    public Condition<C> getCondition() {
        return this.condition;
    }

    @Override
    public void setCondition(Condition<C> condition) {
        this.condition = condition;
    }

    @Override
    public Action<S, E, C> getAction() {
        return this.action;
    }

    @Override
    public void setAction(Action<S, E, C> action) {
        this.action = action;
    }

    @Override
    public State<S, E, C> transit(C ctx, boolean checkCondition) {
        Debugger.debug("Do transition: "+this);
        this.verify();
        if (!checkCondition || condition == null || condition.isSatisfied(ctx)) {
            if(action != null){
                action.execute(source.getId(), target.getId(), event, ctx);
            }
            return target;
        }

        Debugger.debug("Condition is not satisfied, stay at the "+source+" state ");
        return source;
    }

    @Override
    public final String toString() {
        return source + "-[" + event.toString() +", "+type+"]->" + target;
    }

    @Override
    public boolean equals(Object anObject){
        if(anObject instanceof Transition){
            Transition other = (Transition)anObject;
            if(this.event.equals(other.getEvent())
                    && this.source.equals(other.getSource())
                    && this.target.equals(other.getTarget())){
                return true;
            }
        }
        return false;
    }

    @Override
    public void verify() {
        if(type== TransitionType.INTERNAL && source != target) {
            throw new StateMachineException(String.format("Internal transition source state '%s' " +
                    "and target state '%s' must be same.", source, target));
        }
    }
}
