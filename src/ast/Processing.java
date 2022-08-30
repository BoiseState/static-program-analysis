package ast;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class Processing {
	
	private File file;
	
	public Processing(File file) throws IOException {
		this.file = file;
	}
	
	public void analyze() throws IOException {
		String source = new String(Files.readAllBytes(file.toPath()));
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(source.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		ASTNode node = parser.createAST(null);
		ProcessorVisitor visitor =  new ProcessorVisitor();
		node.accept(visitor);
	}
	
	
	private class ProcessorVisitor extends ASTVisitor{
		
		@Override
		public boolean visit(TypeDeclaration node) {
			System.out.println("Here! " + node.getName());
			return true;
		}
		
		@Override
		public boolean visit(MethodDeclaration node) {
			System.out.println("In method " + node.getName());
			return false;
		}
	}

}
