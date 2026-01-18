package org.example.statemachine.builder;

import java.util.List;

/**
 * 批量外部转换构建器接口
 * <p>用于构建多个外部转换，当前仅支持多个源状态到单个目标状态的转换</p>
 *
 * @param <S> 状态类型
 * @param <E> 事件类型
 * @param <C> 上下文类型
 *
 * @author Frank Zhang
 * @date 2020-02-08 7:41 PM
 */
public interface ExternalTransitionsBuilder<S extends Enum<S>, E extends Enum<E>, C> {
    /**
     * 从多个源状态中选择
     *
     * @param stateIds 状态ID数组
     * @return 源状态构建器
     */
    From<S, E, C> fromAmong(S... stateIds);

    /**
     * 从多个源状态中选择
     *
     * @param stateIdList 状态ID列表
     * @return 源状态构建器
     */
    From<S, E, C> fromAmong(List<S> stateIdList);
}
