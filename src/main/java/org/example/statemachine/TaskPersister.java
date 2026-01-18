package org.example.statemachine;

/**
 * 任务持久化接口
 * <p>用于持久化状态机中的对账任务</p>
 *
 * @param <S> 状态类型
 * @param <E> 事件类型
 * @param <C> 上下文类型
 */
public interface TaskPersister<S extends Enum<S>, E extends Enum<E>, C> {

    /**
     * 保存对账任务
     *
     * @param stateMachineId 状态机ID
     * @param state 状态
     * @param context 上下文
     * @param reconcileTask 对账任务
     */
    void saveTask(String stateMachineId,S state ,C context,ReconcileTask<S,E,C> reconcileTask);

    /**
     * 删除对账任务
     *
     * @param stateMachineId 状态机ID
     * @param state 状态
     * @param context 上下文
     */
    void deleteTask(String stateMachineId,S state,C context);
}
