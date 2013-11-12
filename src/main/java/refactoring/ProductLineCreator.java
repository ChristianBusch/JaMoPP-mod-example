package refactoring;

import org.eclipse.emf.ecore.resource.Resource;
import org.emftext.language.java.statements.ExpressionStatement;

public class ProductLineCreator {

    private static final String location1 = "src/test/java/input/CalculatorPow.java";
    private static final String location2 = "src/test/java/input/CalculatorSqrt.java";


    public static void main(String[] args) {

	Modifier.initialize();

	Resource resource1 = Modifier.readJavaFile(location1);
	Resource resource2 = Modifier.readJavaFile(location2);
	ExpressionStatement content1 = Modifier.getFirstExpressionStatementOfFirstMethod(resource1);
	ExpressionStatement content2 = Modifier.getFirstExpressionStatementOfFirstMethod(resource2);
	Modifier.buildMergingIfStatement(resource1, resource2, content1, content2);
	Modifier.saveModifications(resource1);
    }
}
