package  org.example.statemachine.builder;

import org.example.statemachine.State;
import org.example.statemachine.impl.StateHelper;
import org.example.statemachine.impl.TransitionType;

import java.util.Map;

 abstract class AbstractTransitionBuilder<S extends Enum<S>,E extends Enum<E>,C> implements  From<S,E,C>,On<S,E,C>,To<S,E,C>{

    final Map<S, State<S, E, C>> stateMap;

    protected State<S, E, C> target;

    final TransitionType transitionType;

    public AbstractTransitionBuilder(Map<S, State<S, E, C>> stateMap, TransitionType transitionType) {
        this.stateMap = stateMap;
        this.transitionType = transitionType;
    }
    @Override
    public To<S, E, C> to(S stateId) {
        target = StateHelper.getState(stateMap, stateId);
        return this;
    }
}
