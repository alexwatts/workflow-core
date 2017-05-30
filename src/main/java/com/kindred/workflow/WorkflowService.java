package com.kindred.workflow;

import com.kindred.workflow.model.Action;
import com.kindred.workflow.model.Context;
import com.kindred.workflow.model.Transition;
import com.kindred.workflow.model.WorkflowManaged;

public class WorkflowService<W extends WorkflowManaged<S>, S, E, A extends Action> {

    private S workflowState;

    private Context context;

    private WorkflowConfig<S, E, A> workflowConfig;
    private W managedObject;

    public WorkflowService(WorkflowConfig<S, E, A> workflowConfig) {
        this.workflowConfig = workflowConfig;
    }

    public void initialise(W managedObject) {
        this.managedObject = managedObject;
        this.setWorkflowState(this.managedObject.initialiseWorkflowState());
    }

    public void initialise(W managedObject, Context context) {
        this.managedObject = managedObject;
        this.context = context;
        this.setWorkflowState(managedObject.initialiseWorkflowState());
    }

    public Context signalEvent(E event) {
        Transition<S, E, A> transition = workflowConfig.getTransitions().get(event);
        validateTransition(transition);
        for (A action: transition.getActions()) {
            Context executionContext = action.execute(context);
            if (executionContext != null && !executionContext.isEmpty()) {
                context.merge(executionContext);
            }
        }
        setWorkflowState(transition.getTarget());
        return context;
    }

    public Context signalEvent(E event, Context signalContext) {
        if (context == null) {
            context = new Context(signalContext);
        }
        context.merge(signalContext);
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
