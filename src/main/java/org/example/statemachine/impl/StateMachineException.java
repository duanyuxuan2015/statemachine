package org.example.statemachine.impl;

/**
 * 状态机异常类
 * <p>用于表示状态机运行过程中的异常情况</p>
 *
 * @author Frank Zhang
 * @date 2020-02-08 5:28 PM
 */
public class StateMachineException extends RuntimeException{
    /**
     * 构造函数
     *
     * @param message 异常消息
     */
    public StateMachineException(String message){
        super(message);
    }
}
