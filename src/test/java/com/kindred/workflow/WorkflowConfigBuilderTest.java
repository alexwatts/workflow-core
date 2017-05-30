package com.kindred.workflow;


import com.kindred.workflow.builder.WorkflowConfigBuilder;
import com.kindred.workflow.model.Action;
import com.kindred.workflow.model.Transition;
import org.junit.Before;
import org.junit.Test;


import java.util.Map;
import java.util.Set;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


public class WorkflowConfigBuilderTest {

    WorkflowConfigBuilder<FakeStates, FakeEvents, Action> builder;

    @Before
    public void setup() {
        builder = new WorkflowConfigBuilder<>();
    }

    @Test
    public void shouldBeAbleToAddInitialState() {
        //GIVEN
        builder.initialState(FakeStates.NEW);

        //WHEN
        Set<FakeStates> states = builder.build().getStates();

        //THEN
        assertThat(states.size(), equalTo(1));
        assertThat(states, contains(FakeStates.NEW));
    }

    @Test
    public void shouldBeAbleToAddState() {
        //GIVEN
        builder.initialState(FakeStates.NEW);
        builder.state(FakeStates.PUBLISHED);

        //WHEN
        Set<FakeStates> states = builder.build().getStates();

        //THEN
        assertThat(states.size(), equalTo(2));
        assertThat(states, contains(FakeStates.NEW, FakeStates.PUBLISHED));
    }

    @Test
    public void shouldBeAbleToAddTransition() {
        //GIVEN
        Action fakeAction = new FakeAction();

        builder.initialState(FakeStates.NEW);
        builder.state(FakeStates.PUBLISHED);

        builder.transition().
                source(FakeStates.NEW).
                target(FakeStates.PUBLISHED).
                    event(FakeEvents.PUBLISH)
                .action(fakeAction);

        //WHEN
        Map<FakeEvents, Transition<FakeStates, FakeEvents, Action>> transitions = builder.build().getTransitions();

        //THEN
        assertThat(transitions.get(FakeEvents.PUBLISH).getSource(), is(FakeStates.NEW));
        assertThat(transitions.get(FakeEvents.PUBLISH).getTarget(), is(FakeStates.PUBLISHED));
        assertThat(transitions.get(FakeEvents.PUBLISH).getActions().size(), is(1));
        assertThat(transitions.get(FakeEvents.PUBLISH).getActions(), contains(fakeAction));

    }


    @Test(expected = RuntimeException.class)
    public void shouldNotBeAbleToAddInitialStateTwice() {
        //GIVEN
        builder.initialState(FakeStates.NEW);

        //WHEN
        builder.initialState(FakeStates.PUBLISHED);

        //THEN
        //throws exception
    }

}
