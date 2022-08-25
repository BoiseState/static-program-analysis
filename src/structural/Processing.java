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
			while(reader.hasNextLine()) {
				String line = reader.nextLine();
				System.out.println(line);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Cannot find file");
			e.printStackTrace();
		}
	}

}
