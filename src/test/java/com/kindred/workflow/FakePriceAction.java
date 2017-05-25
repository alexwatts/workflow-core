package com.kindred.workflow;

import com.kindred.workflow.model.Action;

import java.util.Map;

public class FakePriceAction extends Action {
    @Override
    public Map<String, Object> execute(Map<String, Object> context) {
        Long price = (Long) context.get("price");
        context.put("price", ++price);
        return context;
    }
}
