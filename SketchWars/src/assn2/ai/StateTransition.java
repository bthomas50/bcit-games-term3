package assn2.ai;

//represents a possible state transition from one state to another
public abstract class StateTransition<T>
{
    private T fromState;
    private T toState;

    public StateTransition(T from, T to)
    {
        fromState = from;
        toState = to;
    }

    public abstract boolean isConditionSatisfied();

    public T getFromState()
    {
        return fromState;
    }

    public T getToState()
    {
        return toState;
    }
}