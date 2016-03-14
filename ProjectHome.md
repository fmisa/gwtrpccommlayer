# Summary #
`GwtRpcCommLayer` is an API that sits on top of any `GwtRpc` service and enables the developer to access the `GwtRpc-servlet(s)` without going through the normal `Browser/JavaScript` engine.

Using this API, a developer can:
  * execute stress-test clients across the entire Ajax call stack
  * perform unit-testing from with ANT or Maven or any framework that can access Java
  * extend your servlet layer to act like a `WebService` layer by exposing all of your method calls to Java applications such as Swing/AWT or CLI (command-line interface) apps.

The API is designed to be non-intrusive and a developer can easily retro-fit it into his/her existing `GwtRpc` based project.

## Requirements ##
On the server-side of this API the only requirement is the obvious Google-Widget-Toolkit (GWT) SDK/code-base

The client-side of this API uses the Apache [HTTP-Client](http://hc.apache.org/httpclient-3.x/) and associated jars. They are as follows:

  * httpclient-4.0.1.jar
  * httpcore-4.0.1.jar
  * httpmime-4.0.1.jar


## Getting Started ##
Download the Eclipse Project [from here](http://gwtrpccommlayer.googlecode.com/files/GwtRpcCommLayer-ver-1.1.zip) and read the README.txt file.

You can grab the JAR (with just source/classes) [from here](http://gwtrpccommlayer.googlecode.com/files/gwt-rpc-comm-layer-ver-1.1.jar)

## Additional Information ##
Please feel free view or join the [Google Group](http://groups.google.com/group/gwtrpccommlayer)
