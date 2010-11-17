package com.googlecode.gwtrpccommlayer.gwtp.test;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

/**
 * Created by IntelliJ IDEA.
 * User: dan
 * Date: 11/16/10
 * Time: 8:13 PM
 */
public class THandler implements ActionHandler<TAction,TResult> {
    @Override
    public TResult execute(TAction testAction, ExecutionContext executionContext) throws ActionException {
        return new TResult();
    }

    @Override
    public Class<TAction> getActionType() {
        return TAction.class;
    }

    @Override
    public void undo(TAction testAction, TResult tResult, ExecutionContext executionContext) throws ActionException {
    }
}
