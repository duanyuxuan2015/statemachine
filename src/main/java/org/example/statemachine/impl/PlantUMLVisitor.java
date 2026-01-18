package org.example.statemachine.impl;


import org.example.statemachine.State;
import org.example.statemachine.StateMachine;
import org.example.statemachine.Transition;
import org.example.statemachine.Visitor;

/**
 * PlantUML访问者
 * <p>将状态机结构转换为PlantUML格式，用于生成状态图</p>
 *
 * @author Frank Zhang
 * @date 2020-02-09 7:47 PM
 */
public class PlantUMLVisitor implements Visitor {

    /**
     * 由于状态机是无状态的，没有初始状态。
     * <p>需要添加"[*] -> initialState"将其标记为状态机图，
     * 否则它将被识别为时序图。</p>
     *
     * @param visitable 要访问的元素
     * @return PlantUML起始标记
     */
    @Override
    public String visitOnEntry(StateMachine<?, ?, ?> visitable) {
        return "@startuml" + LF;
    }

    @Override
    public String visitOnExit(StateMachine<?, ?, ?> visitable) {
        return "@enduml";
    }

    @Override
    public String visitOnEntry(State<?, ?, ?> state) {
        StringBuilder sb = new StringBuilder();
        for(Transition transition: state.getAllTransitions()){
            sb.append(transition.getSource().getId())
                    .append(" --> ")
                    .append(transition.getTarget().getId())
                    .append(" : ")
                    .append(transition.getEvent())
                    .append(LF);
        }
        return sb.toString();
    }

    @Override
    public String visitOnExit(State<?, ?, ?> state) {
        return "";
    }
}
