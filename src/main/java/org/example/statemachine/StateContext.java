package org.example.statemachine;

/**
 * 状态上下文接口
 * <p>封装了状态转换过程中的上下文信息，包括转换对象和状态机实例</p>
 *
 * @author Frank Zhang
 * @date 2020-02-07 2:49 PM
 */
public interface StateContext<S extends Enum<S>, E extends Enum<E>, C> {
    /**
     * 获取转换对象
     *
     * @return 转换对象
     */
    Transition<S, E, C> getTransition();

    /**
     * 获取状态机实例
     *
     * @return 状态机实例
     */
    StateMachine<S, E, C> getStateMachine();
}
