package refactoring;

import java.io.IOException;
import java.math.BigInteger;
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
import org.emftext.language.java.literals.DecimalIntegerLiteral;
import org.emftext.language.java.literals.LiteralsFactory;
import org.emftext.language.java.literals.impl.LiteralsFactoryImpl;
import org.emftext.language.java.members.Method;
import org.emftext.language.java.resource.JaMoPPUtil;
import org.emftext.language.java.statements.Block;
import org.emftext.language.java.statements.Condition;
import org.emftext.language.java.statements.LocalVariableStatement;
import org.emftext.language.java.statements.StatementsFactory;
import org.emftext.language.java.statements.impl.StatementsFactoryImpl;
import org.emftext.language.java.types.PrimitiveType;
import org.emftext.language.java.types.TypesFactory;
import org.emftext.language.java.types.impl.TypesFactoryImpl;
import org.emftext.language.java.variables.LocalVariable;
import org.emftext.language.java.variables.VariablesFactory;
import org.emftext.language.java.variables.impl.VariablesFactoryImpl;

/* 
 * projects needed in the build path:
 * 
 * org.emftext.language.java.resource
 * org.emftext.language.java.resource
 * 
 * as JaMoPP maven repo seems broken right now.
 * 
 * TODO: use maven dependency instead as soon as possible
 */

/**
 * A simple demonstration of using JaMoPP to modify existing .java files.
 * 
 * @author Christian Busch
 */
public class Refactoring {

	public static void main(String[] args) {

		JaMoPPUtil.initialize(); // initialize everything (has to be done once.)

		// load file and get first method
		ResourceSet resSet = new ResourceSetImpl();
		Resource resource = resSet.getResource(
				URI.createURI("src/test/java/input/CalculatorPow.java"), true);
		CompilationUnit cu = (CompilationUnit) resource.getContents().get(0);
		List<Method> methods = cu.getContainedClass().getMethods();
		EObject content = methods.get(0).getFirstChildByType(
				LocalVariableStatement.class);

		StatementsFactory statFac = new StatementsFactoryImpl();
		ExpressionsFactory expFac = new ExpressionsFactoryImpl();
		LiteralsFactory litFac = new LiteralsFactoryImpl();
		VariablesFactory varFac = new VariablesFactoryImpl();
		TypesFactory typeFac = new TypesFactoryImpl();

		Condition condition = statFac.createCondition(); // get an "if"

		// build a "false"
		UnaryExpression exp = expFac.createUnaryExpression();
		BooleanLiteral boo = litFac.createBooleanLiteral();
		boo.setValue(false);
		exp.setChild(boo);

		condition.setCondition(exp); // assemble to "if( false )"

		// create a code block
		Block ifBlock = statFac.createBlock();
		Block elseBlock = statFac.createBlock();

		condition.setStatement(ifBlock);
		condition.setElseStatement(elseBlock);
		LocalVariableStatement locVarStat = statFac
				.createLocalVariableStatement();
		LocalVariable locVar = varFac.createLocalVariable();
		PrimitiveType intType = typeFac.createInt();
		locVar.setTypeReference(intType);
		locVar.setName("answer");
		DecimalIntegerLiteral intLit = litFac.createDecimalIntegerLiteral();
		intLit.setDecimalValue(new BigInteger("23"));
		locVar.setInitialValue(intLit);

		locVarStat.setVariable(locVar);
		ifBlock.getStatements().add(locVarStat);
		((Commentable) content).addBeforeContainingStatement(condition);
		try {
			resource.save(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
