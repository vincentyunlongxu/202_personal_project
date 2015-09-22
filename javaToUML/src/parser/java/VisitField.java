package parser.java;

import java.util.ArrayList;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class VisitField extends VoidVisitorAdapter {
	
	/*primitive types
	**byte	0
	**short	0
	**int	0
	**long	0L
	**float	0.0f
	**double	0.0d
	**char	'\u0000'
	**String (or any object)  	null
	**boolean
	*/
	private ArrayList<Integer> modifier = null;
	private ArrayList<String> fieldType = null;
	private ArrayList<String> fieldName = null;
	
	private ArrayList<String> fieldPrimitiveUml = null;
	private ArrayList<String> fieldNonPrimitiveUml = null;
	
	public VisitField() {
		fieldPrimitiveUml = new ArrayList<String>();
		fieldNonPrimitiveUml = new ArrayList<String>();
	}
	
	public void visit(FieldDeclaration n, Object arg) {
		if(n.getVariables().isEmpty() == false) {
			modifier = new ArrayList<Integer>();
			modifier.add(n.getModifiers());
			fieldType = new ArrayList<String>();
			fieldType.add(n.getType().toString());
			fieldName = new ArrayList<String>();
			fieldName.add(n.getVariables().toString());
		}
	}
}
