package org.example.statemachine;

/**
 * 访问者接口
 * <p>采用访问者模式遍历状态机结构，用于生成状态机展示或输出</p>
 *
 * @author Frank Zhang
 * @date 2020-02-08 8:41 PM
 */
public interface Visitor {

    /**
     * 换行符常量
     */
    char LF = '\n';

    /**
     * 访问状态机入口
     *
     * @param visitable 要访问的状态机元素
     * @return 访问结果字符串
     */
    String visitOnEntry(StateMachine<?, ?, ?> visitable);

    /**
     * 访问状态机出口
     *
     * @param visitable 要访问的状态机元素
     * @return 访问结果字符串
     */
    String visitOnExit(StateMachine<?, ?, ?> visitable);

    /**
     * 访问状态入口
     *
     * @param visitable 要访问的状态元素
     * @return 访问结果字符串
     */
    String visitOnEntry(State<?, ?, ?> visitable);

    /**
     * 访问状态出口
     *
     * @param visitable 要访问的状态元素
     * @return 访问结果字符串
     */
    String visitOnExit(State<?, ?, ?> visitable);
}
