JaMoPP-mod-example
==================

An work in progress example of how to use JaMoPP to modify existing Java code.

Running the code will modify the CalculatorPow.java file by inserting this code direcly under the first methods signature:

```java
if( false ) {
   int answer = 23;
} else {
}
```


### Note:
In order to build the code you currently need some projects in your workspace.  
This is due some problems with m2eclipse and the JaMoPP maven repository I currently encounter and will likely change in the near future.

The projects you need are:

 * org.emftext.language.java
 * org.emftext.language.java.resource

You will find them under https://github.com/DevBoost/JaMoPP
