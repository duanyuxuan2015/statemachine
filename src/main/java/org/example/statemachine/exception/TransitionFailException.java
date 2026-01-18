package org.example.statemachine.exception;

/**
 * 转换失败异常
 * <p>当状态转换失败时抛出此异常</p>
 *
 * @author 龙也
 * @date 2022/9/15 12:08 PM
 */
public class TransitionFailException extends RuntimeException {

    /**
     * 构造函数
     *
     * @param errMsg 错误消息
     */
    public TransitionFailException(String errMsg) {
        super(errMsg);
    }
}
