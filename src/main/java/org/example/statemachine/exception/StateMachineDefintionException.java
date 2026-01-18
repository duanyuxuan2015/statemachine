/**
 * 状态机定义异常
 * <p>当状态机定义不正确时抛出此异常</p>
 *
 * Created on  13-09-09 18:58
 */
package org.example.statemachine.exception;


public class StateMachineDefintionException extends Exception {
    private static final long serialVersionUID = -9066209768410752667L;

    /**
     * 构造函数
     *
     * @param message 异常消息
     */
    public StateMachineDefintionException(String message) {
        super(message);
    }

    /**
     * 构造函数
     *
     * @param message 异常消息
     * @param cause 异常原因
     */
    public StateMachineDefintionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造函数
     *
     * @param cause 异常原因
     */
    public StateMachineDefintionException(Throwable cause) {
        super(cause);
    }
}
