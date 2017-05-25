package com.kindred.workflow;

import com.kindred.workflow.builder.WorkflowConfigBuilder;
import com.kindred.workflow.model.Action;
import com.kindred.workflow.model.WorkflowManaged;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class WorkflowServiceTest {

    private WorkflowService<FakeWorkflowManaged, FakeStates, FakeEvents, Action> stateMachine;

    @Before
    public void setup() {

        WorkflowConfigBuilder<FakeStates, FakeEvents, Action> builder = new WorkflowConfigBuilder();
        builder.initialState(FakeStates.NEW);
        builder.transition().source(FakeStates.NEW).target(FakeStates.PUBLISHED).event(FakeEvents.PUBLISH).action(new FakeAction());
        builder.transition().source(FakeStates.PUBLISHED).target(FakeStates.PRICED).event(FakeEvents.PRICE).action(new FakePriceAction());
        builder.transition().source(FakeStates.PRICED).target(FakeStates.MARKETTED).event(FakeEvents.MARKET).action(new FakeSelectMarketAction()).action( new FakeMarketAction());

        stateMachine = new WorkflowService<>(builder.build());
    }

    class FakeWorkflowManaged implements WorkflowManaged<FakeStates> {

        @Override
        public FakeStates initialiseWorkflowState() {
            return FakeStates.NEW;
        }

    }

    class FakeWorkflowManagedInInvalidState implements WorkflowManaged<FakeStates> {

        @Override
        public FakeStates initialiseWorkflowState() {
            return FakeStates.PUBLISHED;
        }

    }

    @Test
    public void shouldInitialiseStateMachineInObjectState() {
        //GIVEN
        stateMachine.initialise(new FakeWorkflowManaged());

        //WHEN
        Enum state = stateMachine.getState();

        //THEN
        assertThat((FakeStates)state, equalTo(FakeStates.NEW));
    }

    @Test
    public void shouldMakeTransitionToNewState() {
        //GIVEN
        stateMachine.initialise(new FakeWorkflowManaged());

        //WHEN
        stateMachine.signalEvent(FakeEvents.PUBLISH);

        //THEN
        assertThat(stateMachine.getState(), equalTo(FakeStates.PUBLISHED));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotMakeTransitionWhenWorkflowNotInValidState() {
        //GIVEN
        stateMachine.initialise(new FakeWorkflowManaged());
        stateMachine.signalEvent(FakeEvents.PUBLISH);

        //WHEN
        stateMachine.signalEvent(FakeEvents.PUBLISH);

        //THEN
        //IllegalStateExecption
    }

    @Test
    public void shouldBeAbleToUseWorkflowContextViaActions() {
        //GIVEN
        Map<String, Object> context = new HashMap<>();
        context.put("price", 100L);
        stateMachine.initialise(new FakeWorkflowManaged(), context);
        stateMachine.signalEvent(FakeEvents.PUBLISH);

        //WHEN
        Map<String, Object> workflowContext = stateMachine.signalEvent(FakeEvents.PRICE);

        //THEN
        assertThat((Long)workflowContext.get("price"), is(101L));
    }

    @Test
    public void shouldBeAbleToUseWorkflowContextViaSignal() {
        //GIVEN
        stateMachine.initialise(new FakeWorkflowManaged());
        stateMachine.signalEvent(FakeEvents.PUBLISH);


        Map<String, Object> context = new HashMap<>();
        context.put("price", 100L);
        //WHEN
        Map<String, Object> workflowContext = stateMachine.signalEvent(FakeEvents.PRICE, context);

        //THEN
        assertThat((Long)workflowContext.get("price"), equalTo(101L));
    }

    @Test
    public void shouldFireMultipleActionsInCorrectOrder() {
        //GIVEN

        Map<String, Object> context = new HashMap<>();
        context.put("price", 100L);
        stateMachine.initialise(new FakeWorkflowManaged(), context);
        stateMachine.signalEvent(FakeEvents.PUBLISH);
        stateMachine.signalEvent(FakeEvents.PRICE);
        Map<String, Object> workflowContext = stateMachine.signalEvent(FakeEvents.MARKET);

        //THEN
        assertThat((Boolean)workflowContext.get("marketted"), is(true));

    }

}
