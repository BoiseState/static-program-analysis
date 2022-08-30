package structural;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Processing2 {
	public static void main(String[] args) {
		String fileName = "./test/structural/Ex03.java";
		File file = new File(fileName);
		try {
			Scanner reader = new Scanner(file);
			int count = 0;
			while(reader.hasNext()) {
				String line = reader.next();
				if(line.startsWith("if(") || line.equals("if")) {
					System.out.println(line);
					count++;
				}
				
				
			}
			System.out.println("lines: " + count);
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find file");
			e.printStackTrace();
		}
	}

}
