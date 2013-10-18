package refactoring;

import java.io.IOException;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.emftext.language.java.commons.Commentable;
import org.emftext.language.java.containers.CompilationUnit;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.expressions.UnaryExpression;
import org.emftext.language.java.expressions.impl.ExpressionsFactoryImpl;
import org.emftext.language.java.literals.BooleanLiteral;
import org.emftext.language.java.literals.LiteralsFactory;
import org.emftext.language.java.literals.impl.LiteralsFactoryImpl;
import org.emftext.language.java.members.Method;
import org.emftext.language.java.resource.JaMoPPUtil;
import org.emftext.language.java.statements.Assert;
import org.emftext.language.java.statements.LocalVariableStatement;
import org.emftext.language.java.statements.StatementsFactory;
import org.emftext.language.java.statements.impl.StatementsFactoryImpl;

public class Refactoring {

	public static void main(String[] args) {

		// project org.emftext.language.java.resource has to be in workplace and
		// buildpath
		// as JaMoPP maven repo seems broken right now.
		// TODO: use maven dependency instead as soon as possible
		JaMoPPUtil.initialize(); // initialize everything (has to be done once.)
		ResourceSet resSet = new ResourceSetImpl();
		Resource resource = resSet.getResource(
				URI.createURI("src/test/java/input/CalculatorPow.java"), true); // load
																				// the
																				// file
																				// as
																				// resource
		CompilationUnit cu = (CompilationUnit) resource.getContents().get(0);
		List<Method> methods = cu.getContainedClass().getMethods();
		EObject content = methods.get(0).getFirstChildByType(
				LocalVariableStatement.class);

		StatementsFactory statFac = new StatementsFactoryImpl();
		ExpressionsFactory expFac = new ExpressionsFactoryImpl();
		LiteralsFactory litFac = new LiteralsFactoryImpl();

		Assert newAss = statFac.createAssert();
		UnaryExpression exp = expFac.createUnaryExpression();
		BooleanLiteral boo = litFac.createBooleanLiteral();
		boo.setValue(false);
		exp.setChild(boo);

		newAss.setCondition(exp);
		((Commentable) content).addBeforeContainingStatement(newAss);
		try {
			resource.save(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
