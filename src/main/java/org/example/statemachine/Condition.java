package org.example.statemachine;

/**
 * 条件接口
 * <p>用于定义状态转换的守卫条件，只有满足条件时才能执行状态转换</p>
 *
 * @author Frank Zhang
 * @date 2020-02-07 2:50 PM
 */
public interface Condition<C> {

    /**
     * 判断上下文是否满足当前条件
     *
     * @param context 上下文对象
     * @return 如果上下文满足当前条件返回true，否则返回false
     */
    boolean isSatisfied(C context);

    /**
     * 获取条件名称
     * <p>默认返回类的简单名称</p>
     *
     * @return 条件名称
     */
    default String name(){
        return this.getClass().getSimpleName();
    }
}