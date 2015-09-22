package parser.java;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class VisitClass extends VoidVisitorAdapter {
	
	private String className = null;
	private String interfaceName = null;
	private boolean isInterface;
	
	@SuppressWarnings("unchecked")
	public VisitClass() {
	}
	
	public void visit(ClassOrInterfaceDeclaration n, Object arg) {
		isInterface = n.isInterface();
		if(isInterface) {
			interfaceName = n.getName();
		} else {
			className = n.getName();
		}
	}
	
	public String getClassName() {
		
		return className;
	}
	
	public boolean isInterface() {
		return isInterface;
	}
	
	public String getInterfaceName() {
		return interfaceName;
	}
	
}
