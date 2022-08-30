package structural;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Processing {
	public static void main(String[] args) {
		String fileName = "./test/structural/Ex03.java";
		File file = new File(fileName);
		try {
			Scanner reader = new Scanner(file);
			int count = 0;
			boolean commentOn = false;
			while(reader.hasNextLine()) {
				String line = reader.nextLine();
				//System.out.println(line);
				line = line.trim();
				//handling in-line and block comments
				if((line.startsWith("/*") && line.endsWith("*/") 
						&& !line.substring(2, line.length()-2).contains("*"))
						|| line.startsWith("//")) {
					continue;
				} else if (line.startsWith("/*") && !line.contains("*/")) {
					commentOn = true;
				}
				if(commentOn && line.contains("*/")) {
					commentOn = false;
				}
				
				if(!commentOn && !line.isEmpty() && !line.equals("}")
						&& !line.startsWith("package ") &&
						!line.startsWith("class ")) {
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
