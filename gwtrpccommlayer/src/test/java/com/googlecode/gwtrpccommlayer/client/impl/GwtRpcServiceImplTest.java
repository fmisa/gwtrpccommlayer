package com.googlecode.gwtrpccommlayer.client.impl;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.internal.Preconditions;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.googlecode.gwtrpccommlayer.client.GwtRpcService;
import com.googlecode.gwtrpccommlayer.client.Module;
import junit.framework.Assert;
import org.apache.http.cookie.Cookie;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.mockito.Mockito.mock;

/**
 * Created by IntelliJ IDEA.
 * User: dan
 * Date: 11/3/10
 * Time: 2:32 PM
 */
public class GwtRpcServiceImplTest {
    Injector injector;
    private int PORT = 9099;


    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(new Module());

    }


    @Test
    public void integration() throws Exception {
               Server server = new Server(PORT);
        ServletContextHandler handler = new ServletContextHandler(
                server,
                "/",
                ServletContextHandler.SESSIONS);
        handler.addServlet(DefaultServlet.class, "/");
        handler.addFilter(GuiceFilter.class, "/*", null);
        handler.addEventListener(new GuiceServletContextListener() {
            @Override
            protected Injector getInjector() {
                return Guice.createInjector(new ServletModule() {
                    @Override
                    protected void configureServlets() {
                        //need some kind of servlet to report a success
                        serve("/*").with(SomeServlet.class);
                    }
                });
            }
        });
        server.setHandler(handler);
        server.start();

        GwtRpcService service = injector.getInstance(GwtRpcService.class);
        //Create our proxy-based service using GwtRpcService
        TestService testService = service.create(new URL("http://localhost:" + PORT), TestService.class);

        //Run Synchronous Tests
        testService.easy();
        try { testService.multipleArgs(null, null); Assert.fail(); } catch(Exception e) { }
        String test = "test";
        Assert.assertEquals(test, testService.echoSimple(test));

        Set<String> set = new TreeSet<String>();
        set.add("test");

        Assert.assertEquals(set, testService.echoGeneric(set));

        //Run Asynchronous Tests
        TestServiceAsync serviceAsync = service.create(new URL("http://localhost:" + PORT), TestServiceAsync.class);
        serviceAsync.easy(new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                Assert.assertNotNull(throwable);
            }

            @Override
            public void onSuccess(Void aVoid) {
                Assert.assertNotNull(aVoid);
            }
        });

        server.stop();
    }

    @Test
    public void testCreate() throws Exception {
        GwtRpcService service = new GwtRpcServiceImpl();

        URL mockUrl = new URL("http://google.com");
        TestService testService =service.create(mockUrl, TestService.class);
        Preconditions.checkNotNull(testService);

        List<Cookie> lstCookies = mock(List.class);
        testService =service.create(mockUrl, TestService.class, lstCookies);
        Preconditions.checkNotNull(testService);
    }

    @Test
    public void testCreateUsingGuiceOrFactoryMethod() {
        //GwtRpcService serviceByFactory = GwtRpcService.F

    }


}
