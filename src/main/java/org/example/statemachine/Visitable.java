package org.example.statemachine;

/**
 * 可访问接口
 * <p>实现此接口的对象可以被访问者访问</p>
 *
 * @author Frank Zhang
 * @date 2020-02-08 8:41 PM
 */
public interface Visitable {
    /**
     * 接受访问者访问
     *
     * @param visitor 访问者
     * @return 访问结果字符串
     */
    String accept(final Visitor visitor);
}
