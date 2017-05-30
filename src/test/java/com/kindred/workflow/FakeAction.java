package com.kindred.workflow;

import com.kindred.workflow.model.Action;
import com.kindred.workflow.model.Context;

import java.util.Map;

public class FakeAction extends Action {
    @Override
    public Context execute(Context context) {
        return context;
    }
}
