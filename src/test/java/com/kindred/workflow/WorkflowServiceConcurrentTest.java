package com.kindred.workflow;

import com.kindred.workflow.builder.WorkflowConfigBuilder;
import com.kindred.workflow.model.Action;
import com.kindred.workflow.model.Context;
import com.kindred.workflow.model.WorkflowManaged;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class WorkflowServiceConcurrentTest {

    private WorkflowService<FakeWorkflowManaged, FakeStates, FakeEvents, Action> stateMachine;

    @Before
    public void setup() {

        WorkflowConfigBuilder<FakeStates, FakeEvents, Action> builder = new WorkflowConfigBuilder();
        builder.initialState(FakeStates.NEW);
        builder.state(FakeStates.PUBLISHED);
        builder.transition().source(FakeStates.NEW).target(FakeStates.PUBLISHED).event(FakeEvents.PUBLISH).action(new FakeAction());

        stateMachine = new WorkflowService<>(builder.build());
    }

    class FakeWorkflowManaged implements WorkflowManaged<FakeStates> {

        @Override
        public FakeStates initialiseWorkflowState() {
            return FakeStates.NEW;
        }

    }

    @Test
    public void shouldManageAConsistentContextPerThread() {
        //GIVEN
        stateMachine.initialise(new FakeWorkflowManaged());

        //WHEN
        Enum state = stateMachine.getState();

        //THEN
        assertThat((FakeStates)state, equalTo(FakeStates.NEW));
    }

}
