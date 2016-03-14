# Introduction #

Gwt-RPC Comm Layer is a service factory that allows you to fire Gwt-RPC requests from a plain-old java client.  It uses simple object serialization instead of Gwt-RPC's serialization policies.

The main benefit is that you can call your Gwt-RPC services from any java client, not just GWT-generated javascript.

# Security #

Using a POJO has security implications, as you don't want malicious code to execute on your server.

This threat is almost non-existent with normal GWT, because in that limited Java environment you may only use a subset of Java classes.

In light of this threat, I advise you to transmit only value objects and externally authenticate and authorize the user.

# Ok, let's actually get started #
## Service implementation ##

1) Server-side service implementation must either:
  1. extend GwtRpcComLayerServlet
> 2 map servlet to custom implementation with either Guice or web.xml

**OR**

2) Pass your implementation class as a string to GwtRpcCommLayerServlet using Java EE 

&lt;init-param&gt;


downside: only useful for one service


## Instantiate a Gwt-RPC service: ##

Using [Guice Dependency Injection](http://code.google.com/p/google-guice) (recommended, if you know what you're doing you'd just inject this and only do this ugly stuff sparingly!):

GwtRpcService service = Guice.createInjector(new DefaultModule()).getInstance(GwtRpcService.class);

**OR**

Use the factory method (Newbies look here):
GwtRpcService service = GwtRpcService.FACTORY.create();


## Get your own service from Gwt-RPC service factory ##
Your Gwt-RPC service is bound to a URL.  Here's an example using [Gwt-Platform's](http://code.google.com/p/gwt-platform) excellent DispatchAsyncService:

URL url = new Url("httfake://myapp.appspot.com/myContext/dispatch");

DispatchAsyncService asyncService = service.create(DispatchAsync.class, url);