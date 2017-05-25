package com.kindred.workflow;


import com.kindred.workflow.model.Action;

import java.util.Map;

public class FakeMarketAction extends Action {
    @Override
    public Map<String, Object> execute(Map<String, Object> context) {
        String market = (String)context.get("market");
        System.out.println(market);
        context.put("marketted", true);
        return context;
    }
}
