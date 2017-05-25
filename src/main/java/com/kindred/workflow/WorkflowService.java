package com.kindred.workflow;

import com.kindred.workflow.model.Action;
import com.kindred.workflow.model.Transition;
import com.kindred.workflow.model.WorkflowManaged;

import java.util.HashMap;
import java.util.Map;

public class WorkflowService<W extends WorkflowManaged<S>, S, E, A extends Action> {

    private S workflowState;

    private Map<String, Object> context;

    private WorkflowConfig<S, E, A> workflowConfig;
    private W managedObject;

    public WorkflowService(WorkflowConfig<S, E, A> workflowConfig) {
        this.workflowConfig = workflowConfig;
    }

    public void initialise(W managedObject) {
        this.managedObject = managedObject;
        this.setWorkflowState(managedObject.initialiseWorkflowState());
    }

    public void initialise(W managedObject, Map<String, Object> context) {
        this.managedObject = managedObject;
        this.context = context;
        this.setWorkflowState(managedObject.initialiseWorkflowState());
    }

    public Map<String, Object> signalEvent(E event) {
        Transition<S, E, A> transition = workflowConfig.getTransitions().get(event);
        validateTransition(transition);
        for (A action: transition.getActions()) {
            Map<String, Object> executionContext = action.execute(context);
            if (executionContext != null && executionContext.keySet().size() > 0) {
                context.putAll(executionContext);
            }
        }
        setWorkflowState(transition.getTarget());
        return context;
    }

    public Map<String, Object> signalEvent(E event, Map<String, Object> signalContext) {
        if (context == null) {
            context = new HashMap<>();
        }
        context.putAll(signalContext);
        this.signalEvent(event);
        return context;
    }

    public void setWorkflowState(S workflowState) {
        this.workflowState = workflowState;
    }

    public S getState() {
        return workflowState;
    }

    private void validateTransition(Transition transition) {
        if (!getState().equals(transition.getSource())) {
            throw new IllegalStateException("Workflow was not in the expected state for the transition");
        }
    }
}
