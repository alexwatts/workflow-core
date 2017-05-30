package com.kindred.workflow;

import com.kindred.workflow.model.Action;
import com.kindred.workflow.model.Context;

import java.util.Map;

public class FakePriceAction extends Action {
    @Override
    public Context execute(Context context) {
        Long price = (Long) context.get("price");
        context.put("price", ++price);
        return context;
    }
}
