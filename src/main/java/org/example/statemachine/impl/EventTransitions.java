package org.example.statemachine.impl;


import org.example.statemachine.Transition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 事件转换映射类
 * <p>同一个事件可以触发多个转换，详见 https://github.com/alibaba/COLA/pull/158</p>
 *
 * @param <S> 状态类型
 * @param <E> 事件类型
 * @param <C> 上下文类型
 *
 * @author Frank Zhang
 * @date 2021-05-28 5:17 PM
 */
public class EventTransitions<S extends Enum<S>,E extends Enum<E>,C> {
    /**
     * 事件到转换列表的映射表
     */
    private final HashMap<E, List<Transition<S,E,C>>> eventTransitions;

    /**
     * 构造函数
     */
    public EventTransitions(){
        eventTransitions = new HashMap<>();
    }

    /**
     * 添加转换
     *
     * @param event 事件
     * @param transition 转换
     */
    public void put(E event, Transition<S, E, C> transition){
        if(eventTransitions.get(event) == null){
            List<Transition<S,E,C>> transitions = new ArrayList<>();
            transitions.add(transition);
            eventTransitions.put(event, transitions);
        }
        else{
            List<Transition<S, E, C>> existingTransitions = eventTransitions.get(event);
            verify(existingTransitions, transition);
            existingTransitions.add(transition);
        }
    }

    /**
     * 验证转换
     * <p>对于同一对源状态和目标状态，只允许有一个转换</p>
     *
     * @param existingTransitions 已存在的转换列表
     * @param newTransition 新增的转换
     */
    private void verify(List<Transition<S,E,C>> existingTransitions, Transition<S,E,C> newTransition) {
        for (Transition<S,E,C> transition : existingTransitions) {
            if (transition.equals(newTransition)) {
                throw new StateMachineException(transition + " already Exist, you can not add another one");
            }
        }
    }

    /**
     * 获取指定事件对应的转换列表
     *
     * @param event 事件
     * @return 转换列表
     */
    public List<Transition<S,E,C>> get(E event){
        return eventTransitions.get(event);
    }

    /**
     * 获取所有转换
     *
     * @return 所有转换的列表
     */
    public List<Transition<S,E,C>> allTransitions(){
        List<Transition<S,E,C>> allTransitions = new ArrayList<>();
        for(List<Transition<S,E,C>> transitions : eventTransitions.values()){
            allTransitions.addAll(transitions);
        }
        return allTransitions;
    }
}
