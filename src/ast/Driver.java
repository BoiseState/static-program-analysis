package ast;

import java.io.File;
import java.io.IOException;

public class Driver {

	public static void main(String[] args) {
		String fileName = "./test/structural/Test.java";
		File file = new File(fileName);
		try {
			Processing proc = new Processing(file);
			proc.analyze();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
