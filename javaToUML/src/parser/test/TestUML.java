package parser.test;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.SourceFileReader;

public class TestUML {
	public static void main(String args[]) throws IOException {
		String path = "/Users/cheyikung/programming/git/202/personal_project/javaToUML/output/umlSampleOutput.java";
		File file = new File(path);
        
		TestUML tuml = new TestUML(file);
		
		System.out.println("success");
	}
	public TestUML(File s) throws IOException {
		File source = s;
		SourceFileReader reader = new SourceFileReader(source);
		List<GeneratedImage> list = reader.getGeneratedImages();
		// Generated files
		File png = list.get(0).getPngFile();
	}

	
	/* yUML
	public void connect() {

		String path = "/Users/cheyikung/programming/git/202/personal_project/javaToUML/output/test.pdf";

		try {
			URL myURL = new URL("http://yuml.me/diagram/scruffy/class/%2F%2F Cool Class Diagram, [Customer|-forname:string;surname:string|doShiz()]<>-orders*>[Order], [Order]++-0..*>[LineItem], [Order]-[note:Aggregate root{bg:wheat}].pdf");
			URLConnection myURLConnection = myURL.openConnection();
			myURLConnection.connect();
			try(InputStream in = myURL.openStream()){
			    Files.copy(in, Paths.get(path));
			}
		} catch (MalformedURLException e) {
			// new URL() failed
			// ...
		} catch (IOException e) {
			// openConnection() failed
			// ...
		}
	}*/
}
