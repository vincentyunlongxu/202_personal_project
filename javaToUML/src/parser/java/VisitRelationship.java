package parser.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class VisitRelationship extends VoidVisitorAdapter {

	private ArrayList<String> implementList = null;
	private ArrayList<String> extendsList = null;

	private ArrayList<String> allClassName = null;
	private ArrayList<String> allInterfaceName = null;
	private ArrayList<ArrayList<String>> fieldNonPrimitiveUmlList = null;
	private String currentClass = "";

	private ArrayList<String> fieldNonPrimitiveUml = null;
	private ArrayList<String> relationshipUml = null;
	private ArrayList<ArrayList<String>> associationUmlList = null;
	private ArrayList<String> associationUml = null;
	private CompilationUnit cu = null;

	public VisitRelationship() {
		fieldNonPrimitiveUml = new ArrayList<String>();
		relationshipUml = new ArrayList<String>();
		associationUmlList = new ArrayList<ArrayList<String>>();
		associationUml = new ArrayList<String>();
		implementList = new ArrayList<String>();
		extendsList = new ArrayList<String>();
	}

	public void setAllClassName(ArrayList<String> c) {
		this.allClassName = c;
	}

	public void setAllInterfaceName(ArrayList<String> i) {
		this.allInterfaceName = i;
	}

	public void setFieldNonPrimitiveUmlList(ArrayList<ArrayList<String>> f) {
		this.fieldNonPrimitiveUmlList = f;
	}

	public void setCurrentClass(String c) {
		this.currentClass = c;
	}

	public ArrayList<String> getFieldNonPrimitiveUml() {
		return fieldNonPrimitiveUml;
	}

	public ArrayList<String> getRelationshipUml() {
		return relationshipUml;
	}

	public void setAssociationUmlList(ArrayList<ArrayList<String>> a) {
		this.associationUmlList = a;
	}

	public ArrayList<ArrayList<String>> getAssociationUmlList() {
		return associationUmlList;
	}

	public ArrayList<String> getAssociationUml(ArrayList<String> associationUml,
			ArrayList<ArrayList<String>> associationUmlList) {
		for (int i = 0; i < associationUmlList.size(); i++) {
			ArrayList<String> tmp = associationUmlList.get(i);
			String s = "";
			for (int j = 0; j < tmp.size(); j++) {
				s = s + tmp.get(j);
			}
			associationUml.add(s);
		}
		return associationUml;
	}

	public void setAssociation() {
		// draw association
		// container
		// and multiplicity

		// parse fieldNonPrimitiveUml name
		// check with associationUmlList
		// [type] [1..*] [--] [0..1] [type]
		String accessor = "";
		String name = "";
		String type = "";
		String typeName = "";
		boolean classFileExist = false;
		boolean container = false;
		boolean isInassociationUmlList = false;
		String leftClass = "null";
		String leftMutiplicity = "null";
		String association = "--";
		String rightMutiplicity = "null";
		String rightClass = "null";
		for (int i = 0; i < fieldNonPrimitiveUmlList.size(); i++) {
			classFileExist = false;
			container = false;
			ArrayList<String> field = fieldNonPrimitiveUmlList.get(i);
			if (field.size() == 3) {
				accessor = field.get(0);
				name = field.get(1);
				type = field.get(2);
				if (type.contains("<")) {
					container = true;
					typeName = type.substring(type.indexOf("<") + 1, type.indexOf(">"));
				} else {
					typeName = type;
				}
			}
			// check with class name
			for (int j = 0; j < allClassName.size(); j++) {
				String className = allClassName.get(j);
				if (className.equals(typeName)) {
					classFileExist = true;
					break;
				}
			}

			if (classFileExist == true) {
				// check associationUmlList
				for (int k = 0; k < associationUmlList.size(); k++) {
					ArrayList<String> associationUML = associationUmlList.get(k);

					if (associationUML.size() == 5) {
						leftClass = associationUML.get(0);
						leftMutiplicity = associationUML.get(1);
						association = associationUML.get(2);
						rightMutiplicity = associationUML.get(3);
						rightClass = associationUML.get(4);
					}
					if (rightClass.equals(typeName) && leftClass.equals(currentClass)) {
						isInassociationUmlList = true;
						if (container == true) {
							rightMutiplicity = "\"*\"";
						} else {
							rightMutiplicity = "\"0..1\"";
						}
						associationUML.set(3, rightMutiplicity);
						associationUmlList.set(k, associationUML);
						break;
					}
				}
				// not in associationUml
				if (isInassociationUmlList == false) {
					ArrayList<String> associationUML = new ArrayList<String>();
					leftClass = typeName;
					// leftMutiplicity
					// association
					if (container == true) {
						leftMutiplicity = "\"*\"";
					} else {
						leftMutiplicity = "\"0..1\"";
					}
					rightClass = currentClass;
					associationUML.add(leftClass);
					associationUML.add(leftMutiplicity);
					associationUML.add(association);
					associationUML.add(rightMutiplicity);
					associationUML.add(rightClass);

					associationUmlList.add(associationUML);
				}
				break;
			}
			// class file doesn't exist field inside class
			String s = accessor + name.substring(1, name.length() - 1) + ": " + type;
			fieldNonPrimitiveUml.add(s);
		}
	}

	// get relationship
	public void visit(ClassOrInterfaceDeclaration n, Object arg) {
		if (allClassName == null || allInterfaceName == null) {
			System.err.println("system error: no java list. system exit");
			System.exit(1);
		}
		boolean drawImpl = false;
		boolean drawExt = false;
		String implement = "";
		String extend = "";
		if (n.getImplements().isEmpty() == false) {
			implement = n.getImplements().toString();
			drawImpl = true;
			// implementList.add(n.getImplements().toString());
		}
		if (n.getExtends().isEmpty() == false) {
			extend = n.getExtends().toString();
			drawExt = true;
			// extendsList.add(n.getExtends().toString());
		}

		if (drawImpl == false && drawExt == false) {
			return;
		}
		ArrayList<String> name_tmp = new ArrayList<String>();
		if (drawImpl == true) {
			// add implement list
			Collections.addAll(implementList, implement.split("\\s*,\\s*"));
			Collections.addAll(name_tmp, allInterfaceName.toString().split("\\s*,\\s*"));

			// get rid of []
			if (name_tmp.size() > 1) {
				name_tmp.set(0, name_tmp.get(0).substring(1, name_tmp.get(0).length()));
				name_tmp.set(name_tmp.size() - 1,
						name_tmp.get(name_tmp.size() - 1).substring(0, name_tmp.get(name_tmp.size() - 1).length() - 1));

			} else {
				name_tmp.set(0, name_tmp.get(0).substring(1, name_tmp.get(0).length() - 1));
			}
			System.out.println("all interface: " + name_tmp);
			if (implementList.size() > 1) {

				implementList.set(0, implementList.get(0).substring(1, implementList.get(0).length()));
				implementList.set(implementList.size() - 1, implementList.get(implementList.size() - 1).substring(0,
						implementList.get(implementList.size() - 1).length() - 1));
			} else {
				implementList.set(0, implementList.get(0).substring(1, implementList.get(0).length() - 1));
			}
			System.out.println("implementList : " + implementList);
			boolean checkAllImplements = name_tmp.containsAll(implementList);

			// test
			// System.out.println("checkALLImple: " + checkAllImplements);
			if (checkAllImplements == false) {
				System.err.println("found a class implements an interface but the interface does not exists");
				System.err.println("system exit");
				System.exit(1);
			}
			// test
			// System.out.println("this class: " + currentClass);
			// System.out.println("size: " + implementList.size());
			// System.out.println(implementList.get(0).substring(1,
			// implementList.get(0).length()));

			// System.out.println(implementList.get(implementList.size()-1).substring(0,
			// implementList.get(0).length()-1));

			// draw uml
			for (int i = 0; i < implementList.size(); i++) {
				relationshipUml.add(currentClass + "..|>" + implementList.get(i));
			}
			// test
			// System.out.println(relationshipUml);
		}

		if (drawExt == true) {
			// add extend list
			Collections.addAll(extendsList, extend.split("\\s*,\\s*"));

			if (name_tmp.isEmpty() == false) {
				name_tmp.clear();
			}

			Collections.addAll(name_tmp, allClassName.toString().split("\\s*,\\s*"));

			// get rid of []
			if (name_tmp.size() > 1) {
				name_tmp.set(0, name_tmp.get(0).substring(1, name_tmp.get(0).length()));
				name_tmp.set(name_tmp.size() - 1,
						name_tmp.get(name_tmp.size() - 1).substring(0, name_tmp.get(name_tmp.size() - 1).length() - 1));

			} else {
				name_tmp.set(0, name_tmp.get(0).substring(1, name_tmp.get(0).length() - 1));
			}
			System.out.println("all class: " + name_tmp);
			if (extendsList.size() > 1) {

				extendsList.set(0, extendsList.get(0).substring(1, extendsList.get(0).length()));
				extendsList.set(extendsList.size() - 1, extendsList.get(extendsList.size() - 1).substring(0,
						extendsList.get(extendsList.size() - 1).length() - 1));
			} else {
				extendsList.set(0, extendsList.get(0).substring(1, extendsList.get(0).length() - 1));
			}
			System.out.println("extendsList : " + extendsList);

			boolean checkAllExtends = name_tmp.containsAll(extendsList);
			// test

			if (checkAllExtends == false) {
				System.err.println("found a class extends a parent class but parent class does not exists");
				System.err.println("trigger by class: " + currentClass);
				System.err.println("system exit");
				System.exit(1);
			}

			// draw uml
			for (int i = 0; i < extendsList.size(); i++) {
				relationshipUml.add(currentClass + "--|>" + extendsList.get(i));
			}
		}
	}

}
