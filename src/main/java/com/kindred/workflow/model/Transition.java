package com.kindred.workflow.model;

import java.util.List;

public class Transition<S, E, A> {

    private final S source;

    private final S target;

    private final E event;

    private final List<A> actions;

    public Transition(
            S source,
            S target,
            E event,
            List<A> actions) {

        this.source = source;
        this.target = target;
        this.event = event;
        this.actions = actions;
    }

    public S getSource() {
        return source;
    }

    public S getTarget() {
        return target;
    }

    public List<A> getActions() {
        return actions;
    }

    public E getEvent() {
        return event;
    }

}
