package refactoring;

import org.eclipse.emf.ecore.resource.Resource;
import org.emftext.language.java.statements.ExpressionStatement;

public class ProductLineCreator {

    private static final String powLocation = "src/test/java/input/CalculatorPow.java";
    private static final String sqrtLocation = "src/test/java/input/CalculatorSqrt.java";


    public static void main(String[] args) {

	Modifier.initialize();

	Resource powResource = Modifier.readJavaFile(powLocation);
	Resource sqrtResource = Modifier.readJavaFile(sqrtLocation);
	ExpressionStatement powContent = Modifier.getFirstExpressionStatementOfFirstMethod(powResource);
	ExpressionStatement sqrtContent = Modifier.getFirstExpressionStatementOfFirstMethod(sqrtResource);
	Modifier.buildMergingIfStatement(powResource, sqrtResource, powContent, sqrtContent);
	Modifier.saveModifications(powResource);
    }
}
