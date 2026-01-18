package  org.example.statemachine.builder;

/**
 * 状态机构建器工厂类
 * <p>用于创建状态机构建器实例</p>
 *
 * @author Frank Zhang
 * @date 2020-02-08 12:33 PM
 */
public class StateMachineBuilderFactory {
    /**
     * 创建状态机构建器
     *
     * @param <S> 状态类型
     * @param <E> 事件类型
     * @param <C> 上下文类型
     * @return 状态机构建器实例
     */
    public static <S extends Enum<S>, E extends Enum<E>, C> StateMachineBuilder<S, E, C> create(){
        return new StateMachineBuilderImpl<>();
    }
}
