package parser.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class VisitField extends VoidVisitorAdapter {

	/*
	 * primitive types byte 0 short 0 int 0 long 0L float 0.0f double 0.0d char
	 * '\u0000' String (or any object) null boolean
	 */
	private ArrayList<Integer> modifier = null;
	private ArrayList<String> fieldType = null;
	private ArrayList<String> fieldName = null;

	private ArrayList<String> fieldPrimitiveUml = null;
	private ArrayList<ArrayList<String>> fieldNonPrimitiveUml = null;

	private static final String[] PRIMITIVETYPE = { "byte", "short", "int", "long", "float", "double", "char",
			"boolean", "String" };
	private static final int TYPESIZE = 9;

	public VisitField() {
		fieldPrimitiveUml = new ArrayList<String>();
		fieldNonPrimitiveUml = new ArrayList<ArrayList<String>>();
	}

	public void visit(FieldDeclaration n, Object arg) {
		if (n.getVariables().isEmpty() == false) {
			modifier = new ArrayList<Integer>();
			modifier.add(n.getModifiers());
			fieldType = new ArrayList<String>();
			fieldType.add(n.getType().toString());
			fieldName = new ArrayList<String>();
			// System.out.println(n.getVariables());
			fieldName.add(n.getVariables().toString());
		}

		// put it in uml
		// need to distinguish between primitive from non ptimitive type

		// iterator
		Iterator modItr = modifier.iterator();
		Iterator fieldNameItr = fieldName.iterator();
		Iterator fieldTypeItr = fieldType.iterator();
		boolean primitive = false;
		String s = "";
		String accessor = "";
		while (modItr.hasNext()) {
			int accessorInt = (Integer) modItr.next();
			switch (accessorInt) {
			// public
			case 1:
				accessor = "+";
				break;
			// private
			case 2:
				accessor = "-";
				break;
			// protected
			case 3:
				accessor = "#";
				break;
			}
			s = s + accessor;
			String name = (String) fieldNameItr.next();
			s = s + name.substring(1, name.length()-1);

			String type = (String) fieldTypeItr.next();

			s = s + ": ";
			s = s + type;
			for (int i = 0; i < TYPESIZE; i++) {
				if (type.contains(PRIMITIVETYPE[i])) {
					primitive = true;
					fieldPrimitiveUml.add(s);
					break;
				}
			}
			
			//non primitive
			if (primitive == false) {
				//add accessor, name, type
				//in order
				ArrayList<String> tmp = new ArrayList<String>();
				tmp.add(accessor);
				tmp.add(name);
				tmp.add(type);
				fieldNonPrimitiveUml.add(tmp);
			}

			//System.out.println(s);
		}
	}
	
	
	public ArrayList<String> getFieldPrimitiveUml() {
		return fieldPrimitiveUml;
	}
	
	public ArrayList<ArrayList<String>> getFieldNonPrimitiveUml() {
		return fieldNonPrimitiveUml;
	}

}
