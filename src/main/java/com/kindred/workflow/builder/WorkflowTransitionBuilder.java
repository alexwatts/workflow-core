package com.kindred.workflow.builder;

import com.kindred.workflow.model.Transition;

import java.util.LinkedList;
import java.util.List;

public class WorkflowTransitionBuilder<S, E, A> {

    private S source;
    private S target;
    private E event;
    private List<A> actions = new LinkedList<>();

    public WorkflowTransitionBuilder<S, E, A> source(S source) {
        this.source = source;
        return this;
    }

    public WorkflowTransitionBuilder<S, E, A> target(S target) {
        this.target = target;
        return this;
    }

    public WorkflowTransitionBuilder<S, E, A> event(E event) {
        this.event = event;
        return this;
    }

    public WorkflowTransitionBuilder<S, E, A> action(A action) {
        actions.add(action);
        return this;
    }

    public Transition<S, E, A> build() {
        return new Transition<>(source, target, event, actions);
    }

}
