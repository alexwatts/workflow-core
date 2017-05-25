package com.kindred.workflow.model;

import java.util.Map;

public abstract class Action {

    public abstract Map<String,Object> execute(Map<String,Object> context);

}
