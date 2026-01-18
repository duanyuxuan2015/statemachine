package org.example.statemachine;

/**
 * 动作接口
 * <p>状态机用于响应事件的通用策略接口，通过执行{@code Action}来处理状态转换时的业务逻辑</p>
 *
 * @author Frank Zhang
 * @date 2020-02-07 2:51 PM
 */
public interface Action<S extends Enum<S>, E extends Enum<E>, C> {

    /**
     * 执行动作
     * <p>在状态转换过程中执行的业务逻辑</p>
     *
     * @param from 源状态
     * @param to 目标状态
     * @param event 触发的事件
     * @param context 上下文
     */
    void execute(S from, S to, E event, C context);

}
