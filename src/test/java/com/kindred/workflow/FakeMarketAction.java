package com.kindred.workflow;


import com.kindred.workflow.model.Action;
import com.kindred.workflow.model.Context;

import java.util.Map;

public class FakeMarketAction extends Action {
    @Override
    public Context execute(Context context) {
        String market = (String)context.get("market");
        System.out.println(market);
        context.put("marketted", true);
        return context;
    }
}
