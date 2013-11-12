package refactoring;

import org.eclipse.emf.ecore.resource.Resource;
import org.emftext.language.java.statements.LocalVariableStatement;

/* 
 * projects needed in the build path:
 * 
 * org.emftext.language.java
 * org.emftext.language.java.resource
 * 
 * Reason: JaMoPP maven repo seems broken right now.
 * 
 * TODO: use maven dependency instead as soon as possible
 */

/**
 * A simple demonstration of using JaMoPP to modify existing .java files.
 * 
 * This Class will read a file and insert following code directly in front of
 * the first methods first LocalVariableStatement:
 * 
 * <pre>
 * if (true) {
 *     int answer = 42;
 * } else {
 * }
 * </pre>
 * 
 * @author Christian Busch
 */
public class Demo {

    private static final String location = "src/test/java/input/CalculatorPow.java";


    public static void main(String[] args) {

	Modifier.initialize();

	Resource resource = Modifier.readJavaFile(location);
	LocalVariableStatement content = Modifier.getFirstVariableStatementOfFirstMethod(resource);
	Modifier.modifyCodeBefore(content);
	Modifier.saveModifications(resource);
    }

}
