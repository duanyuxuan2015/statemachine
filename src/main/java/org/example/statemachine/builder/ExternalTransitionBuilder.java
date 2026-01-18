package  org.example.statemachine.builder;

/**
 * 外部转换构建器接口
 * <p>用于构建状态之间的外部转换（会触发进入和退出动作）</p>
 *
 * @param <S> 状态类型
 * @param <E> 事件类型
 * @param <C> 上下文类型
 */
public interface ExternalTransitionBuilder<S extends Enum<S>, E extends Enum<E>, C> {

    /**
     * 指定转换的源状态
     *
     * @param stateId 源状态ID
     * @return 源状态构建器
     */
    From<S, E, C> from(S stateId);

}
