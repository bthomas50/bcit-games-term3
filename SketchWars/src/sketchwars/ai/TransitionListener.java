package sketchwars.ai;

//this listener is invoked whenever a fsm undergoes a state transition.
public interface TransitionListener<T>
{
    void transitioned(T from, T to);
}