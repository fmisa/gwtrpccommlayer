package com.googlecode.gwtrpccommlayer.client;

import com.googlecode.gwtrpccommlayer.shared.GwtRpcCommLayerPojoRequest;
import com.googlecode.gwtrpccommlayer.shared.GwtRpcCommLayerPojoResponse;

/**
 * Created by IntelliJ IDEA.
 * User: dan
 * Date: 10/31/10
 * Time: 8:09 PM
 */
public interface RequestExecutor {
    GwtRpcCommLayerPojoResponse executeRequest(GwtRpcCommLayerPojoRequest request);
}
