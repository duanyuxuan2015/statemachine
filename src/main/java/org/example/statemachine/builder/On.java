package  org.example.statemachine.builder;

import org.example.statemachine.Condition;

/**
 * 事件接口
 * <p>用于定义状态转换时触发的事件</p>
 *
 * @param <S> 状态类型
 * @param <E> 事件类型
 * @param <C> 上下文类型
 */
public interface On<S extends Enum<S>, E extends Enum<E>, C> extends When<S, E, C>{

    /**
     * 设置守卫条件
     * <p>只有满足条件时才会执行状态转换</p>
     *
     * @param condition 守卫条件
     * @return 条件构建器
     */
    When<S, E, C> when(Condition<C> condition);
}
