package parser.java;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;

import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.SourceFileReader;

public class Umlparser {

	// lists contains all .java files with provided <classpath>
	private static ArrayList<File> listFiles = new ArrayList<File>();

	// lists of all classes
	private static ArrayList<String> allClassName = new ArrayList<String>();

	// lists of all interfaces
	private static ArrayList<String> allInterfaceName = new ArrayList<String>();

	// lists of outputUML
	private static ArrayList<String> uml = new ArrayList<String>();

	private static ArrayList<ArrayList<String>> associationUmlList = new ArrayList<ArrayList<String>>();

	private static String outputPath = null;

	public static void main(String args[]) {
		if (args.length != 2) {
			System.err.println("Umlparser <classpath> <output file name>");
			System.exit(1);
		}
		// get source Path from args[0]
		File sourcePath = new File(args[0]);
		outputPath = args[1];

		// get all .java path and add them to listFiles ArrayList
		new Umlparser().listJavaFilesForFolder(sourcePath);
		new Umlparser().insertAllClassAndInterface();
		// test above
		// System.out.println("all classes: " + allClassName);
		// System.out.println("all interfaces: " + allInterfaceName);

		// output @startuml
		uml.add("@startuml");

		// insert each class methods and fields

		for (int i = 0; i < listFiles.size(); i++) {
			File file = new File(listFiles.get(i).getAbsolutePath());
			FileInputStream in = null;
			CompilationUnit cu = null;
			try {
				in = new FileInputStream(file);
				cu = JavaParser.parse(in);

			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("parser error");
				System.exit(1);
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			// add class to uml
			VisitClass vc = new VisitClass();
			vc.visit(cu, null);
			if (vc.isInterface()) {
				uml.add("interface " + vc.getInterfaceName() + " {");
			} else {
				uml.add("class " + vc.getClassName() + " {");
			}

			// add method to uml
			VisitMethod vm = new VisitMethod();
			vm.visit(cu, null);

			// System.out.println(vm.getMethodUml());
			// add all methods
			uml.addAll(vm.getMethodUml());

			// add field to uml
			VisitField vf = new VisitField();
			vf.visit(cu, null);
			// add all primitive field
			uml.addAll(vf.getFieldPrimitiveUml());

			// add all non primitive field

			VisitRelationship vr = new VisitRelationship();
			if (vc.isInterface() == false) {
				vr.setCurrentClass(vc.getClassName());
			}
			vr.setAllClassName(allClassName);
			vr.setAllInterfaceName(allInterfaceName);
			vr.setFieldNonPrimitiveUmlList(vf.getFieldNonPrimitiveUml());
			vr.setAssociationUmlList(associationUmlList);

			// process relationship
			vr.visit(cu, null);

			// get all association List
			// ok
			vr.setAssociation();
			associationUmlList = vr.getAssociationUmlList();

			// add all non primitive field
			// ok
			uml.addAll(vr.getFieldNonPrimitiveUml());

			uml.add("}");

			// add relationship to uml
			uml.addAll(vr.getRelationshipUml());

		}

		// process association uml

		// add all association to uml
		// ok
		uml = new VisitRelationship().getAssociationUml(uml, associationUmlList);

		// test associationuml
		// System.out.println("association uml: " + associationUml);

		// add all association to uml

		// add @enduml
		// ok
		uml.add("@enduml");

		// test uml output
		//
		System.out.println(uml);

		// write all to output file
		new Umlparser().writeToFile(uml);

		// process uml to graph
		new Umlparser().createUML();

	}
	
	public void createUML() {
		
		File source = new File(outputPath + "/umlOutput.java");
		SourceFileReader reader;
		try {
			reader = new SourceFileReader(source);
			List<GeneratedImage> list = reader.getGeneratedImages();
			// Generated files
			File png = list.get(0).getPngFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("success!");
	}
	
	public void writeToFile(ArrayList<String> uml) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(outputPath + "/umlOutput.java", "UTF-8");
			for(int i = 0; i < uml.size(); i++) {
				writer.println(uml.get(i));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			writer.close();
		}		
	}

	public void insertAllClassAndInterface() {
		for (int i = 0; i < listFiles.size(); i++) {
			File file = new File(listFiles.get(i).getAbsolutePath());
			FileInputStream in = null;
			CompilationUnit cu = null;
			try {
				in = new FileInputStream(file);
				cu = JavaParser.parse(in);

			} catch (Exception e) {
				// e.printStackTrace();
				System.err.println("syntax error... system exit");
				System.exit(1);
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						// e.printStackTrace();
						System.err.println("unable to close FileInputStream");
						System.exit(1);
					}
				}
			}

			// get class or interface name
			VisitClass vc = new VisitClass();
			vc.visit(cu, null);
			if (vc.isInterface()) {
				allInterfaceName.add(vc.getInterfaceName());
			} else {
				allClassName.add(vc.getClassName());
			}
		}
	}

	// insert all .java path to listFiles ArrayList method
	public void listJavaFilesForFolder(final File sourcePath) {

		FilenameFilter javaFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				String lowercaseName = name.toLowerCase();
				if (lowercaseName.endsWith(".java")) {
					return true;
				}
				return false;
			}
		};
		for (File file : sourcePath.listFiles(javaFilter)) {
			listFiles.add(file);
		}
	}
}
