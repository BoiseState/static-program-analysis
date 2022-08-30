package ast;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Processing {
	
	private File file;
	
	public Processing(File file) throws IOException {
		this.file = file;
	}
	
	public void analyze() throws IOException {
		String source = new String(Files.readAllBytes(file.toPath()));
	}

}
