package com.kindred.workflow;

import com.kindred.workflow.model.Action;

import java.util.Map;

public class FakeAction extends Action {
    @Override
    public Map<String, Object> execute(Map<String, Object> context) {
        return context;
    }
}
