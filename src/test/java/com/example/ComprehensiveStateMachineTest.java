package com.example;

import org.example.statemachine.*;
import org.example.statemachine.builder.StateMachineBuilder;
import org.example.statemachine.builder.StateMachineBuilderFactory;
import org.example.statemachine.impl.StateMachineException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * 状态机综合测试类
 * <p>测试状态机的各种功能，包括外部转换、内部转换、条件判断、动作执行等</p>
 */
@DisplayName("状态机综合测试")
public class ComprehensiveStateMachineTest {

    /**
     * 测试上下文类
     */
    static class TestContext {
        String operator = "test_user";
        String entityId = "test_123";
        boolean conditionFlag = true;
        int actionCount = 0;

        public TestContext() {
        }

        public TestContext(boolean conditionFlag) {
            this.conditionFlag = conditionFlag;
        }
    }

    /**
     * 测试状态枚举
     */
    enum TestStates {
        IDLE,           // 空闲状态
        RUNNING,        // 运行状态
        PAUSED,         // 暂停状态
        COMPLETED,      // 完成状态
        FAILED          // 失败状态
    }

    /**
     * 测试事件枚举
     */
    enum TestEvents {
        START,          // 开始
        PAUSE,          // 暂停
        RESUME,         // 恢复
        COMPLETE,       // 完成
        FAIL,           // 失败
        RESET           // 重置
    }

    @Test
    @DisplayName("测试基础外部转换")
    public void testBasicExternalTransition() {
        StateMachineBuilder<TestStates, TestEvents, TestContext> builder = StateMachineBuilderFactory.create();

        builder.externalTransition()
                .from(TestStates.IDLE)
                .to(TestStates.RUNNING)
                .on(TestEvents.START)
                .perform(createAction("启动"));

        StateMachine<TestStates, TestEvents, TestContext> stateMachine = builder.build("BasicTestMachine");

        TestStates target = stateMachine.fireEvent(TestStates.IDLE, TestEvents.START, new TestContext());

        Assertions.assertEquals(TestStates.RUNNING, target, "状态应从IDLE转换为RUNNING");
    }

    @Test
    @DisplayName("测试带条件的转换")
    public void testTransitionWithCondition() {
        StateMachineBuilder<TestStates, TestEvents, TestContext> builder = StateMachineBuilderFactory.create();

        builder.externalTransition()
                .from(TestStates.RUNNING)
                .to(TestStates.COMPLETED)
                .on(TestEvents.COMPLETE)
                .when(context -> context.conditionFlag)
                .perform(createAction("完成"));

        StateMachine<TestStates, TestEvents, TestContext> stateMachine = builder.build("ConditionTestMachine");

        // 条件满足时应该转换
        TestContext context1 = new TestContext(true);
        TestStates target1 = stateMachine.fireEvent(TestStates.RUNNING, TestEvents.COMPLETE, context1);
        Assertions.assertEquals(TestStates.COMPLETED, target1, "条件满足时应转换为COMPLETED");

        // 重新构建状态机，条件不满足时不应转换
        StateMachineBuilder<TestStates, TestEvents, TestContext> builder2 = StateMachineBuilderFactory.create();
        builder2.externalTransition()
                .from(TestStates.RUNNING)
                .to(TestStates.COMPLETED)
                .on(TestEvents.COMPLETE)
                .when(context -> context.conditionFlag)
                .perform(createAction("完成"));
        StateMachine<TestStates, TestEvents, TestContext> stateMachine2 = builder2.build("ConditionTestMachine2");

        TestContext context2 = new TestContext(false);
        TestStates target2 = stateMachine2.fireEvent(TestStates.RUNNING, TestEvents.COMPLETE, context2);
        Assertions.assertEquals(TestStates.RUNNING, target2, "条件不满足时应保持RUNNING状态");
    }

    @Test
    @DisplayName("测试内部转换")
    public void testInternalTransition() {
        StateMachineBuilder<TestStates, TestEvents, TestContext> builder = StateMachineBuilderFactory.create();

        builder.internalTransition()
                .within(TestStates.RUNNING)
                .on(TestEvents.PAUSE)
                .perform(createAction("内部暂停"));

        StateMachine<TestStates, TestEvents, TestContext> stateMachine = builder.build("InternalTransitionTestMachine");

        TestContext context = new TestContext();
        TestStates target = stateMachine.fireEvent(TestStates.RUNNING, TestEvents.PAUSE, context);

        Assertions.assertEquals(TestStates.RUNNING, target, "内部转换不应改变状态");
        Assertions.assertEquals(1, context.actionCount, "应该执行一次动作");
    }

    @Test
    @DisplayName("测试多路径转换")
    public void testMultipleTransitions() {
        StateMachineBuilder<TestStates, TestEvents, TestContext> builder = StateMachineBuilderFactory.create();

        // 构建状态转换链
        builder.externalTransition()
                .from(TestStates.IDLE)
                .to(TestStates.RUNNING)
                .on(TestEvents.START)
                .perform(createAction("启动"));

        builder.externalTransition()
                .from(TestStates.RUNNING)
                .to(TestStates.PAUSED)
                .on(TestEvents.PAUSE)
                .perform(createAction("暂停"));

        builder.externalTransition()
                .from(TestStates.PAUSED)
                .to(TestStates.RUNNING)
                .on(TestEvents.RESUME)
                .perform(createAction("恢复"));

        builder.externalTransition()
                .from(TestStates.RUNNING)
                .to(TestStates.COMPLETED)
                .on(TestEvents.COMPLETE)
                .perform(createAction("完成"));

        StateMachine<TestStates, TestEvents, TestContext> stateMachine = builder.build("MultiPathTestMachine");
        TestContext context = new TestContext();

        // 测试状态转换链
        TestStates state1 = stateMachine.fireEvent(TestStates.IDLE, TestEvents.START, context);
        Assertions.assertEquals(TestStates.RUNNING, state1);

        TestStates state2 = stateMachine.fireEvent(TestStates.RUNNING, TestEvents.PAUSE, context);
        Assertions.assertEquals(TestStates.PAUSED, state2);

        TestStates state3 = stateMachine.fireEvent(TestStates.PAUSED, TestEvents.RESUME, context);
        Assertions.assertEquals(TestStates.RUNNING, state3);

        TestStates state4 = stateMachine.fireEvent(TestStates.RUNNING, TestEvents.COMPLETE, context);
        Assertions.assertEquals(TestStates.COMPLETED, state4);

        Assertions.assertEquals(4, context.actionCount, "应该执行4次动作");
    }

    @Test
    @DisplayName("测试状态机监听器")
    public void testStateMachineListener() {
        StateMachineBuilder<TestStates, TestEvents, TestContext> builder = StateMachineBuilderFactory.create();

        final boolean[] transitionCalled = {false};
        final boolean[] eventNotAcceptedCalled = {false};
        final boolean[] exceptionCalled = {false};

        StateMachineListener<TestStates, TestEvents, TestContext> listener = new StateMachineListener<TestStates, TestEvents, TestContext>() {
            @Override
            public void eventNotAccepted(TestStates state, TestEvents event, TestContext context) {
                eventNotAcceptedCalled[0] = true;
                System.out.println("事件未被接受: " + event + " 在状态 " + state);
            }

            @Override
            public void onTransition(TestStates source, TestStates target, TestEvents event, TestContext context) {
                transitionCalled[0] = true;
                System.out.println("状态转换: " + source + " -> " + target + " on " + event);
            }

            @Override
            public void onException(TestStates state, TestEvents event, TestContext context, Throwable throwable) {
                exceptionCalled[0] = true;
                System.out.println("发生异常: " + throwable.getMessage());
            }
        };

        builder.addListener(listener);

        builder.externalTransition()
                .from(TestStates.IDLE)
                .to(TestStates.RUNNING)
                .on(TestEvents.START)
                .perform(createAction("启动"));

        StateMachine<TestStates, TestEvents, TestContext> stateMachine = builder.build("ListenerTestMachine");

        // 测试正常转换
        TestContext context = new TestContext();
        stateMachine.fireEvent(TestStates.IDLE, TestEvents.START, context);
        Assertions.assertTrue(transitionCalled[0], "应该调用onTransition回调");

        // 测试不接受的事件
        stateMachine.fireEvent(TestStates.RUNNING, TestEvents.FAIL, context);
        Assertions.assertTrue(eventNotAcceptedCalled[0], "应该调用eventNotAccepted回调");
    }

    @Test
    @DisplayName("测试验证方法")
    public void testVerifyMethod() {
        StateMachineBuilder<TestStates, TestEvents, TestContext> builder = StateMachineBuilderFactory.create();

        builder.externalTransition()
                .from(TestStates.IDLE)
                .to(TestStates.RUNNING)
                .on(TestEvents.START);

        StateMachine<TestStates, TestEvents, TestContext> stateMachine = builder.build("VerifyTestMachine");

        // 验证存在的转换
        boolean canTransition = stateMachine.verify(TestStates.IDLE, TestEvents.START);
        Assertions.assertTrue(canTransition, "应该能够转换");

        // 验证不存在的转换
        boolean cannotTransition = stateMachine.verify(TestStates.RUNNING, TestEvents.PAUSE);
        Assertions.assertFalse(cannotTransition, "不应该能够转换");
    }

    @Test
    @DisplayName("测试多个源状态到同一目标状态")
    public void testMultipleSourcesToSameTarget() {
        StateMachineBuilder<TestStates, TestEvents, TestContext> builder = StateMachineBuilderFactory.create();

        builder.externalTransitions()
                .fromAmong(TestStates.RUNNING, TestStates.PAUSED)
                .to(TestStates.FAILED)
                .on(TestEvents.FAIL)
                .perform(createAction("失败"));

        StateMachine<TestStates, TestEvents, TestContext> stateMachine = builder.build("MultiSourceTestMachine");

        TestStates target1 = stateMachine.fireEvent(TestStates.RUNNING, TestEvents.FAIL, new TestContext());
        Assertions.assertEquals(TestStates.FAILED, target1, "从RUNNING应该能转换为FAILED");

        TestStates target2 = stateMachine.fireEvent(TestStates.PAUSED, TestEvents.FAIL, new TestContext());
        Assertions.assertEquals(TestStates.FAILED, target2, "从PAUSED应该能转换为FAILED");
    }

    @Test
    @DisplayName("测试无动作的转换")
    public void testTransitionWithoutAction() {
        StateMachineBuilder<TestStates, TestEvents, TestContext> builder = StateMachineBuilderFactory.create();

        builder.externalTransition()
                .from(TestStates.IDLE)
                .to(TestStates.RUNNING)
                .on(TestEvents.START);

        StateMachine<TestStates, TestEvents, TestContext> stateMachine = builder.build("NoActionTestMachine");

        TestStates target = stateMachine.fireEvent(TestStates.IDLE, TestEvents.START, new TestContext());
        Assertions.assertEquals(TestStates.RUNNING, target, "应该成功转换");
    }

    @Test
    @DisplayName("测试显示状态机结构")
    public void testShowStateMachine() {
        StateMachineBuilder<TestStates, TestEvents, TestContext> builder = StateMachineBuilderFactory.create();

        builder.externalTransition()
                .from(TestStates.IDLE)
                .to(TestStates.RUNNING)
                .on(TestEvents.START);

        builder.externalTransition()
                .from(TestStates.RUNNING)
                .to(TestStates.COMPLETED)
                .on(TestEvents.COMPLETE);

        StateMachine<TestStates, TestEvents, TestContext> stateMachine = builder.build("DisplayTestMachine");

        // 测试显示状态机（不应该抛出异常）
        Assertions.assertDoesNotThrow(() -> stateMachine.showStateMachine());

        // 测试生成PlantUML
        String plantUML = stateMachine.generatePlantUML();
        Assertions.assertNotNull(plantUML, "PlantUML不应为null");
        Assertions.assertTrue(plantUML.contains("@startuml"), "应该包含PlantUML起始标记");
        Assertions.assertTrue(plantUML.contains("@enduml"), "应该包含PlantUML结束标记");
    }

    @Test
    @DisplayName("测试状态机工厂")
    public void testStateMachineFactory() {
        StateMachineBuilder<TestStates, TestEvents, TestContext> builder = StateMachineBuilderFactory.create();

        builder.externalTransition()
                .from(TestStates.IDLE)
                .to(TestStates.RUNNING)
                .on(TestEvents.START);

        StateMachine<TestStates, TestEvents, TestContext> stateMachine = builder.build("FactoryTestMachine");

        // 从工厂获取状态机
        StateMachine<TestStates, TestEvents, TestContext> retrievedMachine = StateMachineFactory.get("FactoryTestMachine");

        Assertions.assertNotNull(retrievedMachine, "应该能从工厂获取状态机");
        Assertions.assertEquals(stateMachine.getMachineId(), retrievedMachine.getMachineId(), "状态机ID应该相同");
    }

    /**
     * 创建测试动作
     *
     * @param actionName 动作名称
     * @return 动作对象
     */
    private Action<TestStates, TestEvents, TestContext> createAction(String actionName) {
        return (from, to, event, context) -> {
            context.actionCount++;
            System.out.println(String.format("执行动作[%s]: %s -> %s on %s, 操作者: %s",
                    actionName, from, to, event, context.operator));
        };
    }
}
