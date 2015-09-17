package com.parse;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestyUML {
	public static void main(String args[]) {
		TestyUML tuml = new TestyUML();
		tuml.connect();
		System.out.println("success");
	}
	public TestyUML() {

	}

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
	}
}
