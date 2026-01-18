import com.example.Context;
import com.example.Events;
import com.example.StateMachineContainer;
import com.example.States;
import org.example.statemachine.StateMachine;
import org.example.statemachine.builder.StateMachineXmlBuilder;
import org.example.statemachine.builder.StateMachineXmlBuilderImpl;
import org.example.statemachine.exception.StateMachineDefintionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.net.URI;
import java.util.Objects;

public class Main {

    public static void main(String[] args) throws StateMachineDefintionException, InterruptedException {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config.xml");
        StateMachineXmlBuilder<States, Events, Context> builder = new StateMachineXmlBuilderImpl<>(applicationContext);
        String filePath = "stateMachine-definition.xml";
        StateMachine<States,Events,Context> stateMachine = builder.build(filePath,States.class,Events.class);
        StateMachineContainer.addStateMachine(stateMachine.getMachineId(),stateMachine);
        Context context = new Context();
        context.setName("jack");
        States target = stateMachine.fireEvent(States.STATE1, Events.EVENT1, context);
        System.out.println(target!=null?target.name():"failed");
        target = stateMachine.fireEvent(States.STATE1, Events.EVENT2, context);
        System.out.println(target!=null?target.name():"failed");
        Thread.sleep(8000);
        stateMachine.fireEvent(States.STATE2,Events.EVENT2,context);

    }
}