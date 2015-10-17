package sketchwars.ai;

import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Random;

//This class implements a finite state machine for a generic state type, T
public class FiniteStateMachine<T>
{
    private T currentState;
    //would be more efficient as a multimap<T, StateTransition<T>> where the keys are the "from" state
    private Set<StateTransition<T>> transitions;
    private TransitionListener<T> listener;
    private Random rng;

    public FiniteStateMachine(T startingState)
    {
        currentState = startingState;
        transitions = new HashSet<>();
        rng = new Random();
    }

    public void addTransition(StateTransition<T> transition)
    {
        transitions.add(transition);
    }

    public void setTransitionListener(TransitionListener<T> list)
    {
        listener = list;
    }

    public void update()
    {
        ArrayList<StateTransition<T>> possibleTransitions = new ArrayList<>();
        for(StateTransition<T> transition : transitions)
        {
            if(transition.getFromState() == currentState &&
               transition.isConditionSatisfied())
            {
                possibleTransitions.add(transition);
            }
        }
        int numPossible = possibleTransitions.size();
        if(numPossible > 0)
        {
            //if more than 1 transition is possible, pick from them at random.
            //we could prioritize transitions and randomly break ties among the highest-priority transitions, but it's not needed for this assignment
            //we could also weight transitions to let the fsm pick some transitions more than others
            invokeTransition(possibleTransitions.get(rng.nextInt(numPossible)));
        }
    }

    public T getCurrentState()
    {
        return currentState;
    }

    private void invokeTransition(StateTransition<T> transition)
    {
        currentState = transition.getToState();
        if(listener != null)
        {
            //notify the transition listener
            listener.transitioned(transition.getFromState(), transition.getToState());
        }
    }
}