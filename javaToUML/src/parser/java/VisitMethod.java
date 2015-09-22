package parser.java;

import java.util.ArrayList;
import java.util.Iterator;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class VisitMethod extends VoidVisitorAdapter {

	private ArrayList<Integer> modifier = null;
	private ArrayList<String> methodReturnType = null;
	private ArrayList<String> methodName = null;
	private ArrayList<Boolean> emptyParameter = null;
	private ArrayList<Integer> parameterSize = null;

	private ArrayList<String> methodUml = null;

	public VisitMethod() {
		methodUml = new ArrayList<String>();
	}

	public void visit(MethodDeclaration n, Object arg) {
		if (n.getName().isEmpty() == false) {
			modifier = new ArrayList<Integer>();
			modifier.add(n.getModifiers());
			methodName = new ArrayList<String>();
			methodName.add(n.getName());
			methodReturnType = new ArrayList<String>();
			methodReturnType.add(n.getType().toString());
		}

		emptyParameter = new ArrayList<Boolean>();
		emptyParameter.add(n.getParameters().isEmpty());
		parameterSize = new ArrayList<Integer>();
		parameterSize.add(n.getParameters().size());
	
		// put into uml format	
		Iterator modItr = modifier.iterator();
		Iterator methodNameItr = methodName.iterator();
		Iterator paraBoolItr = emptyParameter.iterator();
		Iterator paraSize = parameterSize.iterator();
		Iterator returnTypeItr = methodReturnType.iterator();
		int parItr = 0;
		while (methodNameItr.hasNext()) {
			String s = "";
			String para = "";
			int m = (Integer) modItr.next();
			String methodName = methodNameItr.next().toString();
			boolean emptyPara = (boolean) paraBoolItr.next();
			int parSize = (int) paraSize.next();
			String rt = returnTypeItr.next().toString();
			//System.out.println("m: " + m + " methodName: " + methodName + "emptyPara: " + emptyPara + " parSize: " + parSize + " returnType: " + rt + " parItr: " + parItr);
			if (m == 1) {
				s = s + "+" + methodName;
				if (emptyPara) {
					s = s + "():";
				} else {
					
					for (int i = parItr; i < parSize; i++) {
						para = para + n.getParameters().get(i).getId().getName();
						para = para + ":";
						para = para + n.getParameters().get(i).getType().toString();
						para = para + ",";
						
					}
					parItr = parItr + parSize;
					para = para.substring(0, para.length()-1);
					//test
					//System.out.println("in parameter: " + para);
					s = s + "(" + para + "):";

				}
				s = s + rt;
			}
			methodUml.add(s);
		}
	}

	public ArrayList<String> getMethodUml() {
		return methodUml;
	}
}
