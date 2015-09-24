package parser.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringParser {
	public static void main(String[] args) {
		String example = "Collection<B> ";
		String a = example.substring(example.indexOf("<")+1,example.indexOf(">"));
		System.out.println(a);
	}
}
