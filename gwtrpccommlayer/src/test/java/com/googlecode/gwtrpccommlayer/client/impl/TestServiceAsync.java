package com.googlecode.gwtrpccommlayer.client.impl;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: dan
 * Date: 11/4/10
 * Time: 12:02 AM
 */
public interface TestServiceAsync {
    public void easy(AsyncCallback<Void> callback);
    public void multipleArgs(String test, Integer another,AsyncCallback<Void> callback ) throws Exception;
    public void echoSimple(String test, AsyncCallback<String> callback);
    public <E> void echoGeneric(Set<E> input, AsyncCallback<Set<E>> callback);
}
