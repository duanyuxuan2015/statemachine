package org.example.statemachine;


import org.example.statemachine.impl.TransitionType;

import java.util.Collection;
import java.util.List;

/**
 * 状态接口
 * <p>状态是状态机中的核心概念，表示系统在某一时刻的状态</p>
 *
 * @param <S> 状态类型
 * @param <E> 事件类型
 * @param <C> 上下文类型
 *
 * @author Frank Zhang
 * @date 2020-02-07 2:12 PM
 */
public interface State<S extends Enum<S>,E extends Enum<E>,C> extends Visitable{

    /**
     * 获取状态标识符
     *
     * @return 状态ID
     */
    S getId();

    /**
     * 为状态添加转换关系
     * <p>当指定事件发生时，状态将从当前状态转换到目标状态</p>
     *
     * @param event 转换的事件
     * @param target 转换的目标状态
     * @param transitionType 转换类型（内部转换或外部转换）
     * @return 创建的转换对象
     */
    Transition<S,E,C> addTransition(E event, State<S, E, C> target, TransitionType transitionType);

    /**
     * 获取指定事件对应的所有转换
     *
     * @param event 事件
     * @return 转换列表
     */
    List<Transition<S,E,C>> getEventTransitions(E event);

    /**
     * 获取该状态的所有转换
     *
     * @return 所有转换的集合
     */
    Collection<Transition<S,E,C>> getAllTransitions();

}
