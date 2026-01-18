package org.example.statemachine.impl;

/**
 * 转换类型枚举
 *
 * @author Frank Zhang
 * @date 2020-02-07 10:23 PM
 */
public enum TransitionType {
    /**
     * 内部转换
     * <p>表示如果触发该转换，将不会退出或进入源状态（即不会导致状态改变）。
     * 这意味着不会调用源状态的进入或退出条件。
     * 即使状态机处于一个或多个嵌套在相关状态中的区域，也可以执行内部转换。</p>
     */
    INTERNAL,
    /**
     * 外部转换
     * <p>表示如果触发该转换，将退出组合（源）状态。</p>
     */
    EXTERNAL
}
