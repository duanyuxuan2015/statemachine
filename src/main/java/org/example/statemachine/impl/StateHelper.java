package org.example.statemachine.impl;


import org.example.statemachine.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 状态助手类
 * <p>提供状态相关的工具方法</p>
 *
 * @author Frank Zhang
 * @date 2020-02-08 4:23 PM
 */
public class StateHelper {
    /**
     * 获取状态
     * <p>如果状态不存在则创建新状态</p>
     *
     * @param <S> 状态类型
     * @param <E> 事件类型
     * @param <C> 上下文类型
     * @param stateMap 状态映射表
     * @param stateId 状态ID
     * @return 状态对象
     */
    public static <S extends Enum<S>, E extends Enum<E>, C> State<S, E, C> getState(Map<S, State<S, E, C>> stateMap, S stateId){
        State<S, E, C> state = stateMap.get(stateId);
        if (state == null) {
            state = new StateImpl<>(stateId);
            stateMap.put(stateId, state);
        }
        return state;
    }

    /**
     * 获取多个状态
     *
     * @param <S> 状态类型
     * @param <E> 事件类型
     * @param <C> 上下文类型
     * @param stateMap 状态映射表
     * @param stateIds 状态ID数组
     * @return 状态列表
     */
    public static <S extends Enum<S>, E extends Enum<E>, C>  List<State<S,E,C>> getStates(Map<S, State<S,E,C>> stateMap, S ... stateIds) {
        List<State<S, E, C>> result = new ArrayList<>();
        for (S stateId : stateIds) {
            State<S, E, C> state = getState(stateMap, stateId);
            result.add(state);
        }
        return result;
    }
}
