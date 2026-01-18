package org.example.statemachine.builder;

/**
 * 内部转换构建器接口
 * <p>用于构建状态内部的转换（不触发进入和退出动作）</p>
 *
 * @param <S> 状态类型
 * @param <E> 事件类型
 * @param <C> 上下文类型
 */
public interface InternalTransitionBuilder<S extends Enum<S>, E extends Enum<E>, C> {

    /**
     * 指定内部转换所属的状态
     * <p>内部转换不会导致状态改变，只是在当前状态下执行某些动作</p>
     *
     * @param stateId 状态ID
     * @return 目标状态构建器
     */
    To<S, E, C> within(S stateId);
}
