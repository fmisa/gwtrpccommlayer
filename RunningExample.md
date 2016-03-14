Using the command line, navigate to the  "example" maven module (in directory "example")

run the following:

`mvn war:exploded gwt:run`

to get the server up and running.  Verify GWT-RPC works as normal.

To test that normal java client code works:
run Main.java in example.test.  This is probably best accomplished with your IDE because you don't want to be writing out classpath's and such.