package org.example.statemachine;

/**
 * 状态机监听器接口
 * <p>用于监听状态机的事件和转换，可以在特定事件发生时执行自定义逻辑</p>
 *
 * @param <S> 状态类型
 * @param <E> 事件类型
 * @param <C> 上下文类型
 */
public interface StateMachineListener<S extends Enum<S>, E extends Enum<E>, C> {

    /**
     * 事件不被接受时触发
     * <p>当状态机无法处理某个事件时调用此方法</p>
     *
     * @param state 当前状态
     * @param event 被拒绝的事件
     * @param context 上下文
     */
    void eventNotAccepted(S state , E event, C context);

    /**
     * 状态转换时触发
     * <p>当状态机成功执行状态转换时调用此方法</p>
     *
     * @param source 源状态
     * @param target 目标状态
     * @param event 触发转换的事件
     * @param context 上下文
     */
    void onTransition(S source,S target, E event, C context);

    /**
     * 发生异常时触发
     * <p>当状态机执行过程中发生异常时调用此方法</p>
     *
     * @param state 当前状态
     * @param event 触发事件
     * @param context 上下文
     * @param throwable 抛出的异常
     */
    void onException(S state,E event, C context, Throwable throwable);
}
