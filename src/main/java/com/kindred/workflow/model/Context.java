package com.kindred.workflow.model;

import java.util.Map;

public class Context {

    private ThreadLocal<Map<String, Object>> context;

    public Context(Map<String, Object> context) {
        initialiseContext(context);
    }

    public Context(Context context) {
        initialiseContext(context);
    }

    public boolean isEmpty() {
        return context.get().keySet().size() > 0;
    }

    public void put(String key, Object object) {
        context.get().put(key, object);
    }

    public void merge(Context context) {
        this.context.get().putAll(context.getAll());
    }

    public Object get(String key) {
        return context.get().get(key);
    }

    private Map<String, Object> getAll() {
        return context.get();
    }

    private void initialiseContext(Context context) {
        ThreadLocal threadLocal = new ThreadLocal();
        threadLocal.set(context.getAll());
        this.context = threadLocal;
    }

    private void initialiseContext(Map<String,Object> context) {
        ThreadLocal threadLocal = new ThreadLocal();
        threadLocal.set(context);
        this.context = threadLocal;
    }

}
