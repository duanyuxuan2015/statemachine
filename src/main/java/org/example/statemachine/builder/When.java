package  org.example.statemachine.builder;


import org.example.statemachine.Action;

/**
 * 条件接口
 * <p>用于定义状态转换时执行的动作</p>
 *
 * @param <S> 状态类型
 * @param <E> 事件类型
 * @param <C> 上下文类型
 *
 * @author Frank Zhang
 * @date 2020-02-07 9:33 PM
 */
public interface When<S extends Enum<S>, E extends Enum<E>, C>{
    /**
     * 定义状态转换时执行的动作
     *
     * @param action 要执行的动作
     */
    void perform(Action<S, E, C> action);
}
