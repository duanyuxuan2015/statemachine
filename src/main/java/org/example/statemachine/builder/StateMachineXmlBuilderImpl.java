package org.example.statemachine.builder;

import org.example.statemachine.*;
import org.example.statemachine.exception.StateMachineDefintionException;
import org.example.statemachine.impl.ReconcileTaskImpl;
import org.example.statemachine.impl.SpelCondition;
import org.springframework.context.ApplicationContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class StateMachineXmlBuilderImpl<S extends Enum<S>, E extends Enum<E>, C> implements StateMachineXmlBuilder<S, E, C> {

    private ApplicationContext applicationContext;

    public StateMachineXmlBuilderImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public StateMachineXmlBuilderImpl() {

    }

    @Override
    public StateMachine<S, E, C> build(String xmlPath, Class<S> stateClass, Class<E> eventClass) throws StateMachineDefintionException {
        validateXmlDefinition(xmlPath);
        StateMachineBuilder<S, E, C> builder = StateMachineBuilderFactory.create();
        String machineId = null;
        try {
            // 1. 创建解析工厂
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            // 2. 解析XML文件，生成Document对象（内存中的树）
            Document doc = documentBuilder.parse( StateMachineXmlBuilderImpl.class.getClassLoader().getResourceAsStream(xmlPath));
            doc.getDocumentElement().normalize(); // 标准化文档（去除空白节点等）
            Element root = doc.getDocumentElement();
            machineId = root.getAttribute("id");
            parseExternalTransition(root, builder, stateClass, eventClass, applicationContext);
            parseExternalTransitions(root, builder, stateClass, eventClass, applicationContext);
            parseInternalTransition(root, builder, stateClass, eventClass, applicationContext);
            parseListener(root, builder, applicationContext);
            parseReconcileTask(root, builder, stateClass, applicationContext);
            parseReconcilePersister(root,builder,applicationContext);
        } catch (StateMachineDefintionException stateMachineDefintionException) {
            throw stateMachineDefintionException;
        } catch (Throwable throwable) {
            throw new StateMachineDefintionException(throwable);
        }

        return builder.build(machineId);
    }

    private void validateXmlDefinition(String xmlFilePath) throws StateMachineDefintionException {
        try {
            // 1. 创建 SchemaFactory（指定 XSD 版本，使用 W3C 标准）
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            InputStream xsdStream = StateMachineXmlBuilderImpl.class.getClassLoader()
                    .getResourceAsStream("stateMachine.xsd");
            // 2. 加载 XSD 文件，生成 Schema 对象（代表 XSD 约束）
            Source xsdSource = new StreamSource(xsdStream);
            Schema schema = factory.newSchema(xsdSource);

            // 3. 创建校验器
            Validator validator = schema.newValidator();
            // 4. 加载待校验的 XML 文件
            Source xmlSource = new StreamSource(StateMachineXmlBuilderImpl.class.getClassLoader().getResourceAsStream(xmlFilePath));

            // 5. 执行校验（无异常则校验通过）
            validator.validate(xmlSource);
        } catch (Exception e) {
            // 校验失败，输出错误信息（包含具体不匹配的原因）
            System.err.println("XML 校验失败：" + e.getMessage());
            throw new StateMachineDefintionException(e);
        }
    }

    private void parseExternalTransition(Element root, StateMachineBuilder<S, E, C> builder, Class<S> stateClass, Class<E> eventClass, ApplicationContext applicationContext) throws StateMachineDefintionException {
        NodeList transitionList = root.getElementsByTagName("externalTransition");
        for (int i = 0; i < transitionList.getLength(); i++) {
            Element transitionElem = (Element) transitionList.item(i);
            String fromStateStr = transitionElem.getAttribute("from");
            String toStateStr = transitionElem.getAttribute("to");
            String onEventStr = transitionElem.getAttribute("on");
            String actionClass = transitionElem.getAttribute("actionClass");
            String actionBean = transitionElem.getAttribute("actionBean");
            String conditionClass = transitionElem.getAttribute("conditionClass");
            String conditionBean = transitionElem.getAttribute("conditionBean");
            NodeList expressionNodeList = transitionElem.getElementsByTagName("conditionExpression");
            Condition condition = null;
            String expression = null;
            if (expressionNodeList.getLength() > 0) {
                expression = expressionNodeList.item(0).getTextContent().trim();
            }
            if (isNotEmpty(conditionBean) && applicationContext != null) {
                if (applicationContext.containsBean(conditionBean)) {
                    condition = applicationContext.getBean(conditionBean, Condition.class);
                }
            } else if (isNotEmpty(conditionClass)) {
                try {
                    condition = (Condition) instantiateClass(conditionClass);
                } catch (Exception e) {
                    throw new StateMachineDefintionException("failed to instantiate condition class", e);
                }
            } else if (expression != null) {
                condition = new SpelCondition<C>(expression);
            }

            Action action = null;
            if (isNotEmpty(actionBean) && applicationContext != null) {
                if (applicationContext.containsBean(actionBean)) {
                    action = applicationContext.getBean(actionBean, Action.class);
                }
            } else if (isNotEmpty(actionClass)) {
                try {
                    action = (Action) instantiateClass(actionClass);
                } catch (Exception e) {
                    throw new StateMachineDefintionException("failed to instantiate condition class", e);
                }
            }
            S fromState = Enum.valueOf(stateClass, fromStateStr);
            S toState = Enum.valueOf(stateClass, toStateStr);
            E onEvent = Enum.valueOf(eventClass, onEventStr);
            builder.externalTransition().from(fromState).to(toState).on(onEvent).when(condition).perform(action);
        }
    }


    private void parseExternalTransitions(Element root, StateMachineBuilder<S, E, C> builder, Class<S> stateClass, Class<E> eventClass, ApplicationContext applicationContext) throws StateMachineDefintionException {
        NodeList transitionList = root.getElementsByTagName("externalTransitions");
        for (int i = 0; i < transitionList.getLength(); i++) {
            Element transitionElem = (Element) transitionList.item(i);
            String fromAmongStr = transitionElem.getAttribute("fromAmong");
            String toStateStr = transitionElem.getAttribute("to");
            String onEventStr = transitionElem.getAttribute("on");
            String actionClass = transitionElem.getAttribute("actionClass");
            String actionBean = transitionElem.getAttribute("actionBean");
            String conditionClass = transitionElem.getAttribute("conditionClass");
            String conditionBean = transitionElem.getAttribute("conditionBean");
            NodeList expressionNodeList = transitionElem.getElementsByTagName("conditionExpression");
            Condition condition = null;
            String expression = null;
            if (expressionNodeList.getLength() > 0) {
                expression = expressionNodeList.item(0).getTextContent().trim();
            }
            if (isNotEmpty(conditionBean) && applicationContext != null) {
                if (applicationContext.containsBean(conditionBean)) {
                    condition = applicationContext.getBean(conditionBean, Condition.class);
                }
            } else if (isNotEmpty(conditionClass)) {
                try {
                    condition = (Condition) instantiateClass(conditionClass);
                } catch (Exception e) {
                    throw new StateMachineDefintionException("failed to instantiate condition class", e);
                }
            } else if (expression != null) {
                condition = new SpelCondition<C>(expression);
            }

            Action action = null;
            if (isNotEmpty(actionBean) && applicationContext != null) {
                if (applicationContext.containsBean(actionBean)) {
                    action = applicationContext.getBean(actionBean, Action.class);
                }
            } else if (isNotEmpty(actionClass)) {
                try {
                    action = (Action) instantiateClass(actionClass);
                } catch (Exception e) {
                    throw new StateMachineDefintionException("failed to instantiate condition class", e);
                }
            }

            String[] fromStatesStr = fromAmongStr.split(",");
            List<S> fromStateList = new ArrayList<>();
            for (String string : fromStatesStr) {
                fromStateList.add(Enum.valueOf(stateClass, string));
            }
            S toState = Enum.valueOf(stateClass, toStateStr);
            E onEvent = Enum.valueOf(eventClass, onEventStr);
            builder.externalTransitions().fromAmong(fromStateList).to(toState).on(onEvent).when(condition).perform(action);
        }
    }

    private void parseInternalTransition(Element root, StateMachineBuilder<S, E, C> builder, Class<S> stateClass, Class<E> eventClass, ApplicationContext applicationContext) throws StateMachineDefintionException {
        NodeList transitionList = root.getElementsByTagName("internalTransition");
        for (int i = 0; i < transitionList.getLength(); i++) {
            Element transitionElem = (Element) transitionList.item(i);
            String stateStr = transitionElem.getAttribute("within");
            String onEventStr = transitionElem.getAttribute("on");
            String actionClass = transitionElem.getAttribute("actionClass");
            String actionBean = transitionElem.getAttribute("actionBean");
            String conditionClass = transitionElem.getAttribute("conditionClass");
            String conditionBean = transitionElem.getAttribute("conditionBean");
            NodeList expressionNodeList = transitionElem.getElementsByTagName("conditionExpression");
            Condition condition = null;
            String expression = null;
            if (expressionNodeList.getLength() > 0) {
                expression = expressionNodeList.item(0).getTextContent().trim();
            }
            if (isNotEmpty(conditionBean) && applicationContext != null) {
                if (applicationContext.containsBean(conditionBean)) {
                    condition = applicationContext.getBean(conditionBean, Condition.class);
                }
            } else if (isNotEmpty(conditionClass)) {
                try {
                    condition = (Condition) instantiateClass(conditionClass);
                } catch (Exception e) {
                    throw new StateMachineDefintionException("failed to instantiate condition class", e);
                }
            } else if (expression != null) {
                condition = new SpelCondition<C>(expression);
            }

            Action action = null;
            if (isNotEmpty(actionBean) && applicationContext != null) {
                if (applicationContext.containsBean(actionBean)) {
                    action = applicationContext.getBean(actionBean, Action.class);
                }
            } else if (isNotEmpty(actionClass)) {
                try {
                    action = (Action) instantiateClass(actionClass);
                } catch (Exception e) {
                    throw new StateMachineDefintionException("failed to instantiate condition class", e);
                }
            }
            S state = Enum.valueOf(stateClass, stateStr);
            E onEvent = Enum.valueOf(eventClass, onEventStr);
            builder.internalTransition().within(state).on(onEvent).when(condition).perform(action);
        }
    }

    private void parseListener(Element root, StateMachineBuilder<S, E, C> builder, ApplicationContext applicationContext) throws StateMachineDefintionException {
        StateMachineListener stateMachineListener = null;
        String listenerClass = root.getAttribute("listenerClass");
        String listenerBean = root.getAttribute("listenerBean");
        if (isNotEmpty(listenerBean) && applicationContext != null) {
            if (applicationContext.containsBean(listenerBean)) {
                stateMachineListener = applicationContext.getBean(listenerBean, StateMachineListener.class);
            }
        } else if (isNotEmpty(listenerClass)) {
            try {
                stateMachineListener = (StateMachineListener) instantiateClass(listenerClass);
            } catch (Exception e) {
                throw new StateMachineDefintionException("failed to instantiate stateMachineListener class", e);
            }
        }
        if (stateMachineListener != null) builder.addListener(stateMachineListener);
    }


    private void parseReconcileTask(Element root, StateMachineBuilder<S, E, C> builder, Class<S> stateClass, ApplicationContext applicationContext) throws StateMachineDefintionException {
        NodeList reconcileTaskList = root.getElementsByTagName("reconcileTask");
        for (int i = 0; i < reconcileTaskList.getLength(); i++) {
            Element reconcileTaskElem = (Element) reconcileTaskList.item(i);
            String stateStr = reconcileTaskElem.getAttribute("state");
            String periodStr = reconcileTaskElem.getAttribute("period");
            String cronExpression = reconcileTaskElem.getAttribute("cronExpression");
            String handlerClass = reconcileTaskElem.getAttribute("reconcileHandlerClass");
            String handlerBean = reconcileTaskElem.getAttribute("reconcileHandlerBean");
            ReconcileHandler reconcileHandler = null;
            if (isNotEmpty(handlerBean) && applicationContext != null) {
                if (applicationContext.containsBean(handlerBean)) {
                    reconcileHandler = applicationContext.getBean(handlerBean, ReconcileHandler.class);
                }
            } else if (isNotEmpty(handlerClass)) {
                try {
                    reconcileHandler = (ReconcileHandler) instantiateClass(handlerClass);
                } catch (Exception e) {
                    throw new StateMachineDefintionException("failed to instantiate Reconcile Handler class", e);
                }
            }
            if(reconcileHandler!= null) {
                S state = Enum.valueOf(stateClass, stateStr);
                if(!isNotEmpty(periodStr) && !isNotEmpty(cronExpression)){
                    throw new StateMachineDefintionException("the period and cronExpression is undefined");
                }
                Integer period = null;
                if(isNotEmpty(periodStr)) period = Integer.parseInt(periodStr);
                ReconcileTask<S,E,C> reconcileTask = new ReconcileTaskImpl<>(period,cronExpression, reconcileHandler);
                builder.addReconcileTask(state, reconcileTask);
            }

        }
    }


    private void parseReconcilePersister(Element root, StateMachineBuilder<S, E, C> builder, ApplicationContext applicationContext) throws StateMachineDefintionException {

        String persisterClass = root.getAttribute("persisterClass");
        String persisterBean = root.getAttribute("persisterBean");
        TaskPersister taskPersister = null;
        if (isNotEmpty(persisterBean) && applicationContext != null) {
            if (applicationContext.containsBean(persisterBean)) {
                taskPersister = applicationContext.getBean(persisterBean, TaskPersister.class);
            }
        } else if (isNotEmpty(persisterClass)) {
            try {
                taskPersister = (TaskPersister) instantiateClass(persisterClass);
            } catch (Exception e) {
                throw new StateMachineDefintionException("failed to instantiate Reconcile Persister class", e);
            }
        }
        if(taskPersister!= null) builder.setTaskPersister(taskPersister);
    }


    private boolean isNotEmpty(Object str) {
        return str != null && !"".equals(str);
    }

    private Object instantiateClass(String className) throws Exception {
        Class<?> clazz = Class.forName(className);
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true); // 若构造器为 private，需开启访问权限
        return constructor.newInstance();
    }
}
