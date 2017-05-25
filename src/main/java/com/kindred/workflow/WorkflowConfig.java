package com.kindred.workflow;

import com.kindred.workflow.builder.WorkflowTransitionBuilder;
import com.kindred.workflow.model.Transition;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WorkflowConfig<S, E, A> {

    private final LinkedHashSet<S> states;

    private final Map<E, Transition<S, E, A>> transitions;

    public WorkflowConfig(
            LinkedHashSet<S> states,
            List<WorkflowTransitionBuilder<S, E, A>> transitionBuilders) {
        this.states = states;
        this.transitions = buildTransitions(transitionBuilders);
    }

    public Set<S> getStates() {
        return Collections.unmodifiableSet(states);
    }

    public Map<E, Transition<S, E, A>> getTransitions() {
        return Collections.unmodifiableMap(transitions);
    }

    private Map<E, Transition<S, E, A>> buildTransitions(List<WorkflowTransitionBuilder<S, E, A>> transitionBuilders) {
        Map<E, Transition<S, E, A>> transitionsMap = new HashMap<>();

        for (WorkflowTransitionBuilder<S, E, A> builder: transitionBuilders) {
            transitionsMap.put(builder.build().getEvent(), builder.build());
        }

        return transitionsMap;
    }

}
