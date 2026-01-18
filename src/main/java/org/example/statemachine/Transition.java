package org.example.statemachine;

import org.example.statemachine.impl.TransitionType;

/**
 * 转换接口
 * <p>转换定义了状态机中从一个状态到另一个状态的转移规则</p>
 * <p>每个转换包含：源状态、目标状态、触发事件、守卫条件和执行动作</p>
 *
 * @author Frank Zhang
 *
 * @param <S> 状态类型
 * @param <E> 事件类型
 * @param <C> 用户自定义上下文类型，用于保存应用数据
 *
 * @date 2020-02-07 2:20 PM
 */
public interface Transition<S extends Enum<S>, E extends Enum<E>, C>{
    /**
     * 获取转换的源状态
     *
     * @return 源状态
     */
    State<S,E,C> getSource();

    /**
     * 设置转换的源状态
     *
     * @param state 源状态
     */
    void setSource(State<S, E, C> state);

    /**
     * 获取触发转换的事件
     *
     * @return 事件
     */
    E getEvent();

    /**
     * 设置触发转换的事件
     *
     * @param event 事件
     */
    void setEvent(E event);

    /**
     * 设置转换类型
     *
     * @param type 转换类型
     */
    void setType(TransitionType type);

    /**
     * 获取转换的目标状态
     *
     * @return 目标状态
     */
    State<S,E,C> getTarget();

    /**
     * 设置转换的目标状态
     *
     * @param state 目标状态
     */
    void setTarget(State<S, E, C> state);

    /**
     * 获取转换的守卫条件
     * <p>守卫条件用于控制转换是否可以执行</p>
     *
     * @return 守卫条件
     */
    Condition<C> getCondition();

    /**
     * 设置转换的守卫条件
     *
     * @param condition 守卫条件
     */
    void setCondition(Condition<C> condition);

    /**
     * 获取转换的执行动作
     *
     * @return 执行动作
     */
    Action<S,E ,C> getAction();

    /**
     * 设置转换的执行动作
     *
     * @param action 执行动作
     */
    void setAction(Action<S, E , C> action);

    /**
     * 执行从源状态到目标状态的转换
     *
     * @param ctx 上下文
     * @param checkCondition 是否检查守卫条件
     * @return 目标状态
     */
    State<S, E , C> transit(C ctx, boolean checkCondition);

    /**
     * 验证转换的正确性
     */
    void verify();
}
