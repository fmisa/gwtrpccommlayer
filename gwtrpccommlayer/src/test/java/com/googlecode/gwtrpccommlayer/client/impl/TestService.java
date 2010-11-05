package com.googlecode.gwtrpccommlayer.client.impl;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: dan
 * Date: 11/4/10
 * Time: 12:02 AM
 */
public interface TestService {
    public void easy();
    public void multipleArgs(String test, Integer another) throws Exception;
    public String echoSimple(String test);
    public <E> Set<E> echoGeneric(Set<E> input);
}
