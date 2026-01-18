package org.example.statemachine;


import org.example.statemachine.impl.StateMachineException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 状态机工厂类
 * <p>用于注册和获取状态机实例，采用单例模式管理状态机</p>
 *
 * @author Frank Zhang
 * @date 2020-02-08 10:21 PM
 */
public class StateMachineFactory {
    /**
     * 状态机映射表
     * <p>使用ConcurrentHashMap保证线程安全，key为状态机ID，value为状态机实例</p>
     */
    static Map<String /* machineId */, StateMachine> stateMachineMap = new ConcurrentHashMap<>();

    /**
     * 注册状态机实例
     * <p>将构建好的状态机注册到工厂中，以便后续使用</p>
     *
     * @param <S> 状态类型
     * @param <E> 事件类型
     * @param <C> 上下文类型
     * @param stateMachine 状态机实例
     * @throws StateMachineException 如果该ID的状态机已存在则抛出异常
     */
    public static <S extends Enum<S>, E extends Enum<E>, C> void register(StateMachine<S, E, C> stateMachine){
        String machineId = stateMachine.getMachineId();
        if(stateMachineMap.get(machineId) != null){
            throw new StateMachineException("The state machine with id ["+machineId+"] is already built, no need to build again");
        }
        stateMachineMap.put(stateMachine.getMachineId(), stateMachine);
    }

    /**
     * 获取状态机实例
     * <p>根据状态机ID获取已注册的状态机实例</p>
     *
     * @param <S> 状态类型
     * @param <E> 事件类型
     * @param <C> 上下文类型
     * @param machineId 状态机ID
     * @return 状态机实例
     * @throws StateMachineException 如果该ID的状态机不存在则抛出异常
     */
    public static <S extends Enum<S>, E extends Enum<E>, C> StateMachine<S, E, C> get(String machineId){
        StateMachine stateMachine = stateMachineMap.get(machineId);
        if(stateMachine == null){
            throw new StateMachineException("There is no stateMachine instance for "+machineId+", please build it first");
        }
        return stateMachine;
    }
}
