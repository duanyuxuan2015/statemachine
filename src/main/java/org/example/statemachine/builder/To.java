package  org.example.statemachine.builder;

/**
 * 目标状态接口
 * <p>用于定义状态转换的目标状态</p>
 *
 * @param <S> 状态类型
 * @param <E> 事件类型
 * @param <C> 上下文类型
 *
 * @author Frank Zhang
 * @date 2020-02-07 6:14 PM
 */
public interface To<S extends Enum<S>, E extends Enum<E>, C> {
    /**
     * 指定触发转换的事件
     *
     * @param event 转换事件
     * @return 事件构建器
     */
    On<S, E, C> on(E event);
}
