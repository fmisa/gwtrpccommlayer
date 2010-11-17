package com.googlecode.gwtrpccommlayer.gwtp.server;

import com.google.inject.Inject;
import com.googlecode.gwtrpccommlayer.server.GwtRpcCommLayerServlet;
import com.gwtplatform.dispatch.client.DispatchService;
import com.gwtplatform.dispatch.server.Dispatch;
import com.gwtplatform.dispatch.shared.Action;
import com.gwtplatform.dispatch.shared.ActionException;
import com.gwtplatform.dispatch.shared.Result;
import com.gwtplatform.dispatch.shared.ServiceException;

import javax.inject.Singleton;

/**
 * Created by IntelliJ IDEA.
 * User: dan
 * Date: 10/30/10
 * Time: 3:06 PM
 */
@Singleton
public class DispatchingPOJOServlet
        extends GwtRpcCommLayerServlet
        implements DispatchService{
    private static final long serialVersionUID = 8270773441785078583L;

    private Dispatch dispatch;

    @Inject
    public DispatchingPOJOServlet(Dispatch dispatch) {
        super();
        this.dispatch = dispatch;
    }

    @Override
    public Result execute(String cookieSentByRPC, Action<?> action)
            throws ActionException, ServiceException {
        return dispatch.execute(action);
    }

    @Override
    public void undo(String cookieSentByRPC, Action<Result> action,
            Result result) throws ActionException, ServiceException {
        dispatch.undo(action, result);
    }

}
