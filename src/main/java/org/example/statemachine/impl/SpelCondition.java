package org.example.statemachine.impl;

import org.example.statemachine.Condition;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * SpEL条件实现类
 * <p>使用Spring Expression Language（SpEL）表达式定义守卫条件</p>
 *
 * @param <C> 上下文类型
 */
public class SpelCondition<C> implements Condition <C>{

    /**
     * SpEL表达式解析器
     */
    private static final ExpressionParser parser;

    static {
        parser = new SpelExpressionParser();
    }

    /**
     * SpEL表达式
     */
    private final Expression expression;

    /**
     * 构造函数
     *
     * @param script SpEL表达式脚本
     */
    public SpelCondition(String script) {
        expression = parser.parseExpression(script);
    }

    @Override
    public boolean isSatisfied(C context) {
        EvaluationContext evaluationContext = new StandardEvaluationContext();
        evaluationContext.setVariable("ctx",context);
        Object returnValue = expression.getValue(evaluationContext);
        if(returnValue instanceof  Boolean) return (Boolean)returnValue ;
        return false;
    }

}