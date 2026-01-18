package org.example.statemachine.impl;

/**
 * 调试器
 * <p>用于解耦日志框架依赖，提供简单的调试输出功能</p>
 *
 * @author Frank Zhang
 * @date 2020-02-11 11:08 AM
 */
public class Debugger {

    /**
     * 是否开启调试模式
     */
    private static boolean isDebugOn = false;

    /**
     * 输出调试信息
     *
     * @param message 调试消息
     */
    public static void debug(String message){
        if(isDebugOn){
            System.out.println(message);
        }
    }

    /**
     * 启用调试模式
     */
    public static void enableDebug(){
        isDebugOn = true;
    }
}
