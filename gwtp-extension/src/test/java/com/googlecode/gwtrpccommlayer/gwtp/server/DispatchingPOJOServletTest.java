package com.googlecode.gwtrpccommlayer.gwtp.server;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.googlecode.gwtrpccommlayer.client.GwtRpcService;
import com.googlecode.gwtrpccommlayer.gwtp.test.TAction;
import com.googlecode.gwtrpccommlayer.gwtp.test.THandlerModule;
import com.gwtplatform.dispatch.client.DispatchServiceAsync;
import com.gwtplatform.dispatch.shared.ActionImpl;
import com.gwtplatform.dispatch.shared.Result;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.Assert;
import org.junit.Test;

import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: dan
 * Date: 11/16/10
 * Time: 10:04 PM
 */
public class DispatchingPOJOServletTest {
    private Integer PORT = 49222;

    @Test
    public void test() throws Exception {
                Injector injector = Guice.createInjector(new com.googlecode.gwtrpccommlayer.client.Module());
        Server server = new Server(PORT);
        ServletContextHandler handler = new ServletContextHandler(
                server,
                "/",
                ServletContextHandler.SESSIONS);
        handler.addServlet(DefaultServlet.class, "/");
        //FilterHolder filterHolder = new FilterHolder(GuiceFilter.class);
        handler.addFilter(GuiceFilter.class, "/*", 0);
        handler.addEventListener(new GuiceServletContextListener() {
            @Override
            protected Injector getInjector() {
                return Guice.createInjector( new THandlerModule(),
                        new com.googlecode.gwtrpccommlayer.gwtp.server.Module());

            }
        });

        server.setHandler(handler);
        server.start();

        GwtRpcService service = injector.getInstance(GwtRpcService.class);
        //Create our proxy-based service using GwtRpcService
        DispatchServiceAsync testService = service.create(
                new URL(
                        "http://localhost:" + PORT + "/" +
                                ActionImpl.DEFAULT_SERVICE_NAME),
                DispatchServiceAsync.class);

        testService.execute("", new TAction(), new AsyncCallback<Result>() {
            @Override
            public void onFailure(Throwable caught) {
                Assert.fail(caught.getMessage());
            }

            @Override
            public void onSuccess(Result result) {
                Assert.assertNotNull(result);
            }
        });



        server.stop();
    }
}
