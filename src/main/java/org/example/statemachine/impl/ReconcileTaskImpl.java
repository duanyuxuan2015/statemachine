package org.example.statemachine.impl;

import org.example.statemachine.ReconcileHandler;
import org.example.statemachine.ReconcileTask;

/**
 * 对账任务实现类
 *
 * @param <S> 状态类型
 * @param <E> 事件类型
 * @param <C> 上下文类型
 */
public class ReconcileTaskImpl<S extends Enum<S>, E extends Enum<E>, C> implements ReconcileTask<S,E,C> {

    /**
     * 执行周期（秒）
     */
    private final Integer period;

    /**
     * Cron表达式
     */
    private final String cronExpression;

    /**
     * 对账处理器
     */
    private final ReconcileHandler<S,E,C> reconcileHandler;

    /**
     * 构造函数
     *
     * @param period 执行周期
     * @param cronExpression Cron表达式
     * @param reconcileHandler 对账处理器
     */
    public ReconcileTaskImpl(Integer period,String cronExpression,ReconcileHandler<S,E,C> reconcileHandler){
        this.period = period;
        this.reconcileHandler = reconcileHandler;
        this.cronExpression = cronExpression;
    }

    @Override
    public Integer getPeriod() {
        return this.period;
    }

    @Override
    public String getCronExpression() {
        return this.cronExpression;
    }

    @Override
    public ReconcileHandler<S,E, C> getReconcileHandler() {
        return this.reconcileHandler;
    }
}
