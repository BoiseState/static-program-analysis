package ast;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.text.edits.MalformedTreeException;

/*
 * https://git.eclipse.org/c/jdt/eclipse.jdt.core.git/tree/org.eclipse.jdt.core/dom/org/eclipse/jdt/core/dom/ASTVisitor.java

 */
public class Driver {

	public static void main(String[] args) {
		String fileName = "./test/structural/Ex02.java";
		File file = new File(fileName);
		try {
//			Processing proc = new Processing(file);
//			proc.analyze();
			Rewriting rev = new Rewriting(file);
			rev.transform();
			
		} catch (IOException | MalformedTreeException | BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
