package com.kindred.workflow.builder;

import com.kindred.workflow.WorkflowConfig;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class WorkflowConfigBuilder<S, E, A> {

    private LinkedHashSet<S> states = new LinkedHashSet<>();

    private List<WorkflowTransitionBuilder<S, E, A>> transitions = new ArrayList<>();

    public WorkflowConfigBuilder initialState(S state) {
        addInitialState(state);
        return this;
    }

    public void state(S state) {
        addState(state);
    }

    public WorkflowTransitionBuilder<S, E, A> transition() {
        WorkflowTransitionBuilder<S, E, A> transitionBuilder = new WorkflowTransitionBuilder<>();
        transitions.add(transitionBuilder);
        return transitionBuilder;
    }

    public WorkflowConfig<S, E, A> build() {
        return new WorkflowConfig<>(this.states, this.transitions);
    }

    private void addInitialState(S state) {
        if (!states.isEmpty()) {
            throw new RuntimeException("There can only be one initial state");
        } else {
            states.add(state);
        }
    }

    private void addState(S state) {
        states.add(state);
    }

}
