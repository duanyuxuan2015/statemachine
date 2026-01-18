package org.example.statemachine.builder;

/**
 * 源状态接口
 * <p>用于定义状态转换的起始状态</p>
 *
 * @param <S> 状态类型
 * @param <E> 事件类型
 * @param <C> 上下文类型
 */
public interface From<S extends Enum<S>, E extends Enum<E>, C> {

    /**
     * 指定目标状态
     *
     * @param stateId 目标状态ID
     * @return 目标状态构建器
     */
    To<S, E, C> to(S stateId);

}
