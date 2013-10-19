JaMoPP-mod-example
==================

An work in progress example of how to use JaMoPP to modify existing Java code.

Running the code will modify the CalculatorPow.java file by inserting this code direcly ahead of the first methods first instruction\*:

```java
if( false ) {
   int answer = 23;
} else {
}
```

So out of this:

```jave
package input;

public class CalculatorPow {

	public double compute(double i) {
		double result = Math.pow(i,2);
		return result;
	}
}

```

...becomes this:

```jave
package input;

public class CalculatorPow {

	public double compute(double i) {
		if (false) {
			int answer = 23;
		}else {
		}
		double result = Math.pow(i,2);
		return result;
	}
}

```

\* actually the first LocalVariableStatement

### Note:
In order to build the code you currently need some projects in your workspace.  
This is due some problems with m2eclipse and the JaMoPP maven repository I currently encounter and will likely change in the near future.

The projects you need are:

 * org.emftext.language.java
 * org.emftext.language.java.resource

You will find them under https://github.com/DevBoost/JaMoPP
