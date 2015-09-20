package com.parse;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class Test {
	public static void main(String[] args) throws Exception {
        // creates an input stream for the file to be parsed
		String path = "/Users/cheyikung/programming/git/202/personal_project/javaToUML/javapath/TestJava.java";
        File file = new File(path);
		FileInputStream in = new FileInputStream(file);
		
        CompilationUnit cu=null;
        try {
            // parse the file
  
            cu = JavaParser.parse(in);
            new MethodVisitor().visit(cu, null);
            
        } catch (Exception e) {
        	//System.out.println("parser exception");
        	e.printStackTrace();
        }finally {
            in.close();
        }

        //System.out.println(cu);
        
        // visit and print the methods names
        //new ClassDeclarationVisitor().visit(cu, null);
        
        
        //new FieldVisitor().visit(cu, null);
    }

    /**
     * Simple visitor implementation for visiting MethodDeclaration nodes. 
     */
    private static class MethodVisitor extends VoidVisitorAdapter {

    	
        @Override
        public void visit(MethodDeclaration n, Object arg) {
            // here you can access the attributes of the method.
            // this method will be called for all methods in this 
            // CompilationUnit, including inner class methods
        	//System.out.println(n.getModifiers() + n.getType() + " " + n.getName());
            System.out.println(n.getType());
            
        }
    }
    /*
    private static class FieldVisitor extends VoidVisitorAdapter {
        @Override
        public void visit(FieldDeclaration n, Object arg) {
        	System.out.println(n.getType());
        }
    }*/
    
    /*
    private static class ClassDeclarationVisitor extends VoidVisitorAdapter {
    	public void visit(ClassOrInterfaceDeclaration n, Object arg) {
    		System.out.println(n.isInterface());
    	}
    	
    }*/
  
}
