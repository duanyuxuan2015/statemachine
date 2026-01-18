import org.example.statemachine.Action;
import org.example.statemachine.Condition;
import org.example.statemachine.StateMachine;
import org.example.statemachine.builder.StateMachineBuilder;
import org.example.statemachine.builder.StateMachineBuilderFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StateMachineTest {

    static String MACHINE_ID = "TestStateMachine";

    static enum States {
        STATE1,
        STATE2,
        STATE3,
        STATE4
    }

    static enum Events {
        EVENT1,
        EVENT2,
        EVENT3,
        EVENT4,
        INTERNAL_EVENT
    }

    static class Context {
        String operator = "frank";
        String entityId = "123465";
    }
    @Test
    public void testExternalNormal() {
        StateMachineBuilder<States, Events, Context> builder = StateMachineBuilderFactory.create();
        builder.externalTransition()
                .from(States.STATE1)
                .to(States.STATE2)
                .on(Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());


        StateMachine<States, Events, Context> stateMachine = builder.build(MACHINE_ID);
        States target = stateMachine.fireEvent(States.STATE1, Events.EVENT1, new Context());
        Assertions.assertEquals(States.STATE2, target);
    }

    @Test
    public void testExternalsNormal() {
        StateMachineBuilder<States, Events, Context> builder = StateMachineBuilderFactory.create();
        builder.externalTransitions()
                .fromAmong(States.STATE1,States.STATE2)
                .to(States.STATE3)
                .on(Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());


        StateMachine<States, Events, Context> stateMachine = builder.build(MACHINE_ID+1);
        States target = stateMachine.fireEvent(States.STATE1, Events.EVENT1, new Context());
        Assertions.assertEquals(States.STATE3, target);
    }

    private Condition<Context> checkCondition() {
        return new Condition<Context>() {
            @Override
            public boolean isSatisfied(Context context) {
                System.out.println("Check condition : " + context);
                return true;
            }
        };
    }

    private Action<States, Events, Context> doAction() {
        return (from, to, event, ctx) -> {
            System.out.println(
                    ctx.operator + " is operating " + ctx.entityId + " from:" + from + " to:" + to + " on:" + event);
        };
    }
}
