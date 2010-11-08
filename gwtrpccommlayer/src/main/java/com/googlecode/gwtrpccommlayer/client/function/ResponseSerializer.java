package com.googlecode.gwtrpccommlayer.client.function;

import com.google.common.base.Function;
import com.googlecode.gwtrpccommlayer.shared.GwtRpcCommLayerPojoResponse;
import org.apache.http.HttpResponse;

/**
 * Created by IntelliJ IDEA.
 * User: dan
 * Date: 11/6/10
 * Time: 12:06 AM
 */
public interface ResponseSerializer extends Function<HttpResponse, GwtRpcCommLayerPojoResponse> {
}
