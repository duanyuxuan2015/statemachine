package org.example.statemachine;

/**
 * 状态机接口
 * <p>状态机是状态模式的核心抽象，定义了状态转换的基本行为</p>
 *
 * @author Frank Zhang
 *
 * @param <S> 状态类型
 * @param <E> 事件类型
 * @param <C> 用户自定义上下文
 * @date 2020-02-07 2:13 PM
 */
public interface StateMachine<S extends Enum<S>, E extends Enum<E>, C> extends Visitable{

    /**
     * 验证从当前状态{@code S}是否可以触发事件{@code E}
     *
     * @param sourceStateId 源状态ID
     * @param event 事件
     * @return 如果可以触发返回true，否则返回false
     */
    boolean verify(S sourceStateId, E event);

    /**
     * 向状态机发送事件{@code E}
     * <p>该方法会触发状态转换，并返回目标状态</p>
     *
     * @param sourceState 源状态
     * @param event 要发送的事件
     * @param ctx 用户自定义上下文
     * @return 目标状态
     */
     S fireEvent(S sourceState, E event, C ctx);

    /**
     * 状态机对账处理
     * <p>用于处理状态不一致的情况，将状态调整到指定状态</p>
     *
     * @param state 目标状态
     * @param ctx 用户上下文
     */
    void reconcile(S state, C ctx);

    /**
     * 获取状态机ID
     * <p>MachineId是状态机的唯一标识符</p>
     *
     * @return 状态机ID
     */
    String getMachineId();

    /**
     * 使用访问者模式显示状态机结构
     */
    void showStateMachine();

    /**
     * 生成PlantUML状态图
     *
     * @return PlantUML格式的状态图定义
     */
    String generatePlantUML();
}
