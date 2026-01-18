package  org.example.statemachine.builder;

import org.example.statemachine.ReconcileTask;
import org.example.statemachine.StateMachine;
import org.example.statemachine.StateMachineListener;
import org.example.statemachine.TaskPersister;

/**
 * 状态机构建器接口
 * <p>使用建造者模式构建状态机实例</p>
 *
 * @param <S> 状态类型
 * @param <E> 事件类型
 * @param <C> 上下文类型
 */
public interface StateMachineBuilder<S extends Enum<S>, E extends Enum<E>, C> {

    /**
     * 创建外部转换构建器
     * <p>外部转换会触发状态的进入和退出动作</p>
     *
     * @return 外部转换构建器
     */
    ExternalTransitionBuilder<S, E, C> externalTransition();

    /**
     * 创建批量外部转换构建器
     * <p>用于批量定义多个外部转换</p>
     *
     * @return 批量外部转换构建器
     */
    ExternalTransitionsBuilder<S, E, C> externalTransitions();

    /**
     * 创建内部转换构建器
     * <p>内部转换不触发状态的进入和退出动作</p>
     *
     * @return 内部转换构建器
     */
    InternalTransitionBuilder<S, E, C> internalTransition();

    /**
     * 添加状态机监听器
     *
     * @param stateMachineListener 状态机监听器
     */
    void addListener(StateMachineListener<S, E, C> stateMachineListener);

    /**
     * 设置任务持久化器
     *
     * @param taskPersister 任务持久化器
     */
    void setTaskPersister(TaskPersister<S,E,C> taskPersister);

    /**
     * 添加对账任务
     *
     * @param state 状态
     * @param reconcileTask 对账任务
     */
    void addReconcileTask(S state, ReconcileTask<S,E,C> reconcileTask);

    /**
     * 构建状态机实例
     *
     * @param machineId 状态机ID
     * @return 构建好的状态机实例
     */
    StateMachine<S, E, C> build(String machineId);

}
