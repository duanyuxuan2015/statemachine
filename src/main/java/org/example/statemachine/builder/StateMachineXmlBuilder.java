package org.example.statemachine.builder;

import org.example.statemachine.StateMachine;
import org.example.statemachine.exception.StateMachineDefintionException;

import java.net.URI;

/**
 * XML状态机构建器接口
 * <p>用于从XML配置文件构建状态机</p>
 *
 * @param <S> 状态类型
 * @param <E> 事件类型
 * @param <C> 上下文类型
 *
 * @author Frank Zhang
 * @date 2020-02-07 5:32 PM
 */
public interface StateMachineXmlBuilder<S extends Enum<S>, E extends Enum<E>, C> {

    /**
     * 从XML文件构建状态机
     *
     * @param xmlPath XML文件路径
     * @param stateClass 状态枚举类
     * @param eventClass 事件枚举类
     * @return 构建好的状态机实例
     * @throws StateMachineDefintionException 如果状态机定义不正确则抛出异常
     */
    StateMachine<S, E, C> build(String xmlPath,Class<S> stateClass,Class<E> eventClass) throws StateMachineDefintionException;

}
