package assn2.ai;

import assn2.Tamagotchi;

//this class plugs in the data for the FSM.
public class TamagotchiStateMachine
{
    public static FiniteStateMachine<States> create(final Tamagotchi tama)
    {
        FiniteStateMachine<States> fsm = new FiniteStateMachine<>(States.WAITING);
        //get hungry if waiting for a while
        fsm.addTransition(new StateTransition<States>(States.WAITING, States.HUNGRY) {
            @Override
            public boolean isConditionSatisfied() {return tama.getMillisSinceLastStateChange() > seconds(10);}
        });
        //I added this state to make sure you couldn't just cycle between WAIT -> PLAY -> SLEEP -> WAIT forever
        fsm.addTransition(new StateTransition<States>(States.WAITING, States.HUNGRY) {
            @Override
            public boolean isConditionSatisfied() {return tama.getMillisSinceLastEat() > minutes(1);}
        });
        //play if possible
        fsm.addTransition(new StateTransition<States>(States.WAITING, States.PLAYING) {
            @Override
            public boolean isConditionSatisfied() {return tama.isPlayAvailable();}
        });
        //eat if possible
        fsm.addTransition(new StateTransition<States>(States.HUNGRY, States.EATING) {
            @Override
            public boolean isConditionSatisfied() {return tama.isFoodAvailable();}
        });
        //die of excessive hunger
        fsm.addTransition(new StateTransition<States>(States.HUNGRY, States.DEAD) {
            @Override
            public boolean isConditionSatisfied() {return tama.getMillisSinceLastStateChange() > seconds(10);}
        });
        //maybe it goes into a food coma (nondeterminism is part of the FSM implementation)
        fsm.addTransition(new StateTransition<States>(States.EATING, States.SLEEPING) {
            @Override
            public boolean isConditionSatisfied() {return tama.getMillisSinceLastStateChange() > seconds(2);}
        });
        //maybe it's still hungry (nondeterminism is part of the FSM implementation)
        fsm.addTransition(new StateTransition<States>(States.EATING, States.HUNGRY) {
            @Override
            public boolean isConditionSatisfied() {return tama.getMillisSinceLastStateChange() > seconds(2);}
        });
        //get tired after playing
        fsm.addTransition(new StateTransition<States>(States.PLAYING, States.SLEEPING) {
            @Override
            public boolean isConditionSatisfied() {return tama.getMillisSinceLastStateChange() > seconds(5);}
        });
        //wake up after a bit
        fsm.addTransition(new StateTransition<States>(States.SLEEPING, States.WAITING) {
            @Override
            public boolean isConditionSatisfied() {return tama.getMillisSinceLastStateChange() > seconds(10);}
        });
        //die of old age :(
        fsm.addTransition(new StateTransition<States>(States.SLEEPING, States.DEAD) {
            @Override
            public boolean isConditionSatisfied() {return tama.getTotalAgeInMillis() > minutes(10);}
        });
        //start all over again!
        fsm.addTransition(new StateTransition<States>(States.DEAD, States.WAITING) {
            @Override
            public boolean isConditionSatisfied() {return tama.isStartAvailable();}
        });
        return fsm;
    }

    private static double seconds(double num)
    {
        return num * 1000;
    }

    private static double minutes(double num)
    {
        return num * 1000 * 60;
    }

    private TamagotchiStateMachine() {}
}