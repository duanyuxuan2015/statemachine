package org.example.statemachine;

/**
 * 对账任务接口
 * <p>用于定义状态机中对账任务的抽象</p>
 *
 * @param <S> 状态类型
 * @param <E> 事件类型
 * @param <C> 上下文类型
 */
public interface ReconcileTask<S extends Enum<S>, E extends Enum<E>, C>  {
    /**
     * 获取对账任务的执行周期（秒）
     *
     * @return 执行周期
     */
    Integer getPeriod();

    /**
     * 获取Cron表达式
     * <p>用于定义复杂的定时任务执行规则</p>
     *
     * @return Cron表达式
     */
    String getCronExpression();

    /**
     * 获取对账处理器
     *
     * @return 对账处理器
     */
    ReconcileHandler<S,E,C> getReconcileHandler();
}
