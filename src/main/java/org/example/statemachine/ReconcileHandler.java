package org.example.statemachine;

/**
 * 对账处理器接口
 * <p>用于处理状态机对账任务的执行逻辑</p>
 *
 * @param <S> 状态类型
 * @param <E> 事件类型
 * @param <C> 上下文类型
 */
public interface ReconcileHandler<S extends Enum<S>, E extends Enum<E>, C>  {
    /**
     * 处理对账任务
     *
     * @param state 状态
     * @param context 上下文
     * @param stateMachine 状态机实例
     */
    void handle(S state, C context,StateMachine<S,E,C> stateMachine) ;
}
