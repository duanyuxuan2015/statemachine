package org.example.statemachine.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.example.statemachine.*;

/**
 * 状态机实现类
 * <p>出于性能考虑，状态机被设计为"无状态"的。
 * 构建完成后，可以被多线程共享使用。</p>
 * <p>一个副作用是：由于状态机是无状态的，我们无法从状态机中获取当前状态。</p>
 *
 * @author Frank Zhang
 * @date 2020-02-07 5:40 PM
 */
public class StateMachineImpl<S extends Enum<S>, E extends Enum<E>, C> implements StateMachine<S, E, C> {

    /**
     * 状态机ID
     */
    private String machineId;

    /**
     * 状态映射表
     */
    private final Map<S, State<S, E, C>> stateMap;

    /**
     * 状态机是否已准备就绪
     */
    private boolean ready;

    /**
     * 状态机监听器
     */
    private StateMachineListener<S, E, C> stateMachineListener;

    /**
     * 任务持久化器
     */
    private TaskPersister<S,E,C> taskPersister;

    /**
     * 对账任务映射表
     */
    private Map<S, ReconcileTask<S,E,C>> reconcileTaskMap;

    /**
     * 构造函数
     *
     * @param stateMap 状态映射表
     */
    public StateMachineImpl(Map<S, State<S, E, C>> stateMap) {
        this.stateMap = stateMap;
    }

    @Override
    public boolean verify(S sourceStateId, E event) {
        isReady();

        State<S,E,C> sourceState = getState(sourceStateId);

        List<Transition<S, E, C>> transitions = sourceState.getEventTransitions(event);

        return transitions != null && !transitions.isEmpty();
    }

    @Override
    public S fireEvent(S sourceStateId, E event, C ctx) {
        S target = null ;
        try {
            isReady();
            Transition<S, E, C> transition = routeTransition(sourceStateId, event, ctx);

            if (transition == null) {
                Debugger.debug("There is no Transition for " + event);
                if (stateMachineListener != null) stateMachineListener.eventNotAccepted(sourceStateId, event, ctx);
                return sourceStateId;
            }
            target = transition.transit(ctx, false).getId();
            processReconcileTask(sourceStateId,target,ctx);
            if(stateMachineListener!=null) stateMachineListener.onTransition(sourceStateId,target,event,ctx);
        }catch (Throwable throwable){
            if (stateMachineListener != null) stateMachineListener.onException(sourceStateId, event, ctx,throwable);
        }

        return target;
    }

    /**
     * 处理对账任务
     *
     * @param source 源状态
     * @param target 目标状态
     * @param ctx 上下文
     */
    private void processReconcileTask(S source,S target, C ctx) {
        if(taskPersister == null || source.equals(target)) return;
        ReconcileTask<S,E,C> sourceReconcileTask = reconcileTaskMap.get(source);
        if(sourceReconcileTask!= null)  taskPersister.deleteTask(machineId,source,ctx);
        ReconcileTask<S,E,C> reconcileTask = reconcileTaskMap.get(target);
        if(reconcileTask != null)  taskPersister.saveTask(machineId,target,ctx,reconcileTask);;
    }


    @Override
    public void reconcile(S state, C ctx) {
        ReconcileTask<S,E,C> reconcileTask = reconcileTaskMap.get(state);
        if(reconcileTask == null) return;
        reconcileTask.getReconcileHandler().handle(state,ctx,this);
    }


    /**
     * 路由转换
     * <p>根据事件和上下文条件选择合适的转换</p>
     *
     * @param sourceStateId 源状态ID
     * @param event 事件
     * @param ctx 上下文
     * @return 匹配的转换，如果没有匹配则返回null
     */
    private Transition<S, E, C> routeTransition(S sourceStateId, E event, C ctx) {
        State<S, E, C> sourceState = getState(sourceStateId);

        List<Transition<S, E, C>> transitions = sourceState.getEventTransitions(event);

        if (transitions == null || transitions.isEmpty()) {
            return null;
        }

        Transition<S, E, C> transit = null;
        for (Transition<S, E, C> transition : transitions) {
            if (transition.getCondition() == null) {
                transit = transition;
            } else if (transition.getCondition().isSatisfied(ctx)) {
                transit = transition;
                break;
            }
        }

        return transit;
    }

    /**
     * 获取状态
     *
     * @param currentStateId 状态ID
     * @return 状态对象
     */
    private State<S,E,C> getState(S currentStateId) {
        return StateHelper.getState(stateMap, currentStateId);
    }

    /**
     * 检查状态机是否已准备就绪
     */
    private void isReady() {
        if (!ready) {
            throw new StateMachineException("State machine is not built yet, can not work");
        }
    }

    @Override
    public String accept(Visitor visitor) {
        StringBuilder sb = new StringBuilder();
        sb.append(visitor.visitOnEntry(this));
        for (State<S,E,C> state : stateMap.values()) {
            sb.append(state.accept(visitor));
        }
        sb.append(visitor.visitOnExit(this));
        return sb.toString();
    }

    @Override
    public void showStateMachine() {
        SysOutVisitor sysOutVisitor = new SysOutVisitor();
        accept(sysOutVisitor);
    }

    @Override
    public String generatePlantUML() {
        PlantUMLVisitor plantUMLVisitor = new PlantUMLVisitor();
        return accept(plantUMLVisitor);
    }

    @Override
    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public void addListener(StateMachineListener<S,E,C> stateMachineListener){
        this.stateMachineListener = stateMachineListener;
    }

    public void setTaskPersister(TaskPersister<S,E,C> taskPersister){
        this.taskPersister = taskPersister;
    }

    public void setReconcileTaskMap( Map<S, ReconcileTask<S,E,C>> reconcileTaskMap){
        this.reconcileTaskMap = reconcileTaskMap;
    }
}
