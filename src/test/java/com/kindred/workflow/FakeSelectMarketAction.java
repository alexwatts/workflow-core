package com.kindred.workflow;


import com.kindred.workflow.model.Action;

import java.util.Map;

public class FakeSelectMarketAction extends Action {
    @Override
    public Map<String, Object> execute(Map<String, Object> context) {
        context.put("market", "UK16");
        return context;
    }
}
