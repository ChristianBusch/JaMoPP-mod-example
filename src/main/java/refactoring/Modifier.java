package refactoring;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.emftext.language.java.containers.CompilationUnit;
import org.emftext.language.java.expressions.ExpressionsFactory;
import org.emftext.language.java.expressions.UnaryExpression;
import org.emftext.language.java.expressions.impl.ExpressionsFactoryImpl;
import org.emftext.language.java.literals.BooleanLiteral;
import org.emftext.language.java.literals.DecimalIntegerLiteral;
import org.emftext.language.java.literals.LiteralsFactory;
import org.emftext.language.java.literals.impl.LiteralsFactoryImpl;
import org.emftext.language.java.members.Field;
import org.emftext.language.java.members.MembersFactory;
import org.emftext.language.java.members.Method;
import org.emftext.language.java.members.impl.MembersFactoryImpl;
import org.emftext.language.java.modifiers.ModifiersFactory;
import org.emftext.language.java.modifiers.impl.ModifiersFactoryImpl;
import org.emftext.language.java.references.IdentifierReference;
import org.emftext.language.java.references.ReferencesFactory;
import org.emftext.language.java.references.impl.ReferencesFactoryImpl;
import org.emftext.language.java.resource.JaMoPPUtil;
import org.emftext.language.java.statements.Block;
import org.emftext.language.java.statements.Condition;
import org.emftext.language.java.statements.ExpressionStatement;
import org.emftext.language.java.statements.LocalVariableStatement;
import org.emftext.language.java.statements.Statement;
import org.emftext.language.java.statements.StatementsFactory;
import org.emftext.language.java.statements.impl.StatementsFactoryImpl;
import org.emftext.language.java.types.PrimitiveType;
import org.emftext.language.java.types.TypesFactory;
import org.emftext.language.java.types.impl.TypesFactoryImpl;
import org.emftext.language.java.variables.LocalVariable;
import org.emftext.language.java.variables.VariablesFactory;
import org.emftext.language.java.variables.impl.VariablesFactoryImpl;

/**
 * A simple demonstration of using JaMoPP to modify existing .java files.
 * 
 * @author Christian Busch
 */
public class Modifier {

    private static final StatementsFactory statFac = new StatementsFactoryImpl();
    private static final ExpressionsFactory expFac = new ExpressionsFactoryImpl();
    private static final LiteralsFactory litFac = new LiteralsFactoryImpl();
    private static final VariablesFactory varFac = new VariablesFactoryImpl();
    private static final TypesFactory typeFac = new TypesFactoryImpl();
    private static final ReferencesFactory refFac = new ReferencesFactoryImpl();
    private static final MembersFactory memFac = new MembersFactoryImpl();
    private static final ModifiersFactory modFac = new ModifiersFactoryImpl();


    public static void initialize() {

	JaMoPPUtil.initialize(); // initialize everything (has to be done once.)
    }


    public static Resource readJavaFile(String fileLocation) {

	ResourceSet resSet = new ResourceSetImpl();
	Resource resource = resSet.getResource(URI.createURI(fileLocation), true);
	return resource;
    }


    public static LocalVariableStatement getFirstVariableStatementOfFirstMethod(Resource resource) {

	CompilationUnit cu = (CompilationUnit) resource.getContents().get(0);
	List<Method> methods = cu.getContainedClass().getMethods();
	LocalVariableStatement content = methods.get(0).getFirstChildByType(LocalVariableStatement.class);
	return content;
    }


    public static ExpressionStatement getFirstExpressionStatementOfFirstMethod(Resource resource) {

	CompilationUnit cu = (CompilationUnit) resource.getContents().get(0);
	List<Method> methods = cu.getContainedClass().getMethods();
	ExpressionStatement content = methods.get(0).getFirstChildByType(ExpressionStatement.class);
	return content;
    }


    public static void modifyCodeBefore(LocalVariableStatement content) {

	Block ifBlock = statFac.createBlock();
	Block elseBlock = statFac.createBlock();

	LocalVariableStatement initializedVariable = createLocalVariableStatement(statFac, litFac, varFac,
		typeFac);
	ifBlock.getStatements().add(initializedVariable);

	Condition ifElseBlock = statFac.createCondition();
	UnaryExpression falseBool = buildFalseBoolean();
	ifElseBlock.setCondition(falseBool);
	ifElseBlock.setStatement(ifBlock);
	ifElseBlock.setElseStatement(elseBlock);

	content.addBeforeContainingStatement(ifElseBlock);
    }


    private static UnaryExpression buildFalseBoolean() {

	UnaryExpression exp = expFac.createUnaryExpression();
	BooleanLiteral boo = litFac.createBooleanLiteral();
	boo.setValue(false);
	exp.setChild(boo);
	return exp;
    }


    private static LocalVariableStatement createLocalVariableStatement(StatementsFactory statFac,
	    LiteralsFactory litFac, VariablesFactory varFac, TypesFactory typeFac) {

	LocalVariable answerVariable = varFac.createLocalVariable();
	PrimitiveType intType = typeFac.createInt();
	answerVariable.setTypeReference(intType);
	answerVariable.setName("answer");
	DecimalIntegerLiteral intLit = litFac.createDecimalIntegerLiteral();
	intLit.setDecimalValue(new BigInteger("42"));
	answerVariable.setInitialValue(intLit);

	LocalVariableStatement locVarStat = statFac.createLocalVariableStatement();
	locVarStat.setVariable(answerVariable);
	return locVarStat;
    }


    public static void saveModifications(Resource resource) {

	try {
	    resource.save(null);
	} catch (IOException e) {
	    System.out.println("ERROR saving modifications!");
	    e.printStackTrace();
	}
    }


    public static void buildMergingIfStatement(Resource resource1, Resource resource2,
	    ExpressionStatement content1, ExpressionStatement content2) {

	CompilationUnit cu = (CompilationUnit) resource1.getContents().get(0);

	// create boolean
	PrimitiveType boolType = typeFac.createBoolean();
	UnaryExpression trueBoolExp = expFac.createUnaryExpression();
	BooleanLiteral trueBool = litFac.createBooleanLiteral();
	trueBool.setValue(true);
	trueBoolExp.setChild(trueBool);

	// create field holding the boolean
	Field testField = memFac.createField();
	testField.setInitialValue(trueBoolExp);
	testField.setName("test");
	testField.setTypeReference(boolType);
	testField.addModifier(modFac.createPrivate());
	testField.addModifier(modFac.createStatic());
	testField.addModifier(modFac.createFinal());

	// adding the field to the class as first statement
	cu.getContainedClass().getMembers().add(0, testField);

	// adding an import (not needed, just for demonstration purpose)
	cu.addImport("java.util.List");

	Block ifBlock = statFac.createBlock();
	Block elseBlock = statFac.createBlock();

	IdentifierReference testRef = refFac.createIdentifierReference();
	testRef.setTarget(testField);
	Condition ifElseBlock = statFac.createCondition();
	ifElseBlock.setCondition(testRef);
	ifElseBlock.setStatement(ifBlock);
	ifElseBlock.setElseStatement(elseBlock);
	
	Statement content1Copy = (Statement) EcoreUtil.copy(content1);
	Statement content2Copy = (Statement) EcoreUtil.copy(content2);

	ifBlock.getStatements().add(content1Copy);
	elseBlock.getStatements().add(content2Copy);

	content1.addBeforeContainingStatement(ifElseBlock);

	// delete now redundant statement out of block
	EcoreUtil.delete(content1);

    }

}
