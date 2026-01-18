import com.example.Context;
import com.example.Events;
import com.example.States;
import org.example.statemachine.StateMachine;
import org.example.statemachine.builder.StateMachineXmlBuilder;
import org.example.statemachine.builder.StateMachineXmlBuilderImpl;
import org.example.statemachine.exception.StateMachineDefintionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.net.URI;
import java.util.Objects;

public class Main2 {

    public static void main(String[] args) throws StateMachineDefintionException {

        String script = "#ctx.name == 'jack'";

        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(script);

        Context context = new Context();
        context.setName("jack");
        EvaluationContext evaluationContext = new StandardEvaluationContext();
        evaluationContext.setVariable("ctx",context);
        System.out.println(expression.getValue(evaluationContext)) ;
    }
}