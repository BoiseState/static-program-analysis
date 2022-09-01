package ast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

public class VarRename {
	
	private File file;
	private ASTRewrite rewriter;
	private AST ast;
	private String s1;
	private String s2;
	private String mName;
	private int bId;
	
	public VarRename(File file, String s1, String s2, String mName, int bId) throws IOException {
		this.file = file;
		this.s1 = s1;
		this.s2 = s2;
		this.mName = mName;
		this.bId = bId;
	}
	
	
	public void transform() throws IOException, MalformedTreeException, BadLocationException {
		String source = new String(Files.readAllBytes(file.toPath()));
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(source.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		ast = cu.getAST();
		rewriter = ASTRewrite.create(ast);
		
		//main work here
		RewriterVisitor visitor = new RewriterVisitor();
		cu.accept(visitor);
		
		//writing back to the file
		Document document = new Document(source);
		TextEdit edits = rewriter.rewriteAST(document, null);
		edits.apply(document);
		File copy = new File(file.getPath()+"_copy");

		BufferedWriter out = new BufferedWriter(new FileWriter(copy));

		out.write(document.get());
		out.flush();
		out.close();
	}
	
	private class RewriterVisitor extends ASTVisitor{
		
		//remove methods with more than 3 parameter
		
		@Override
		public boolean visit(MethodDeclaration node) {
			boolean ret = false;
			String currMethodName = node.getName().getIdentifier();
			if(currMethodName.equals(mName)) {
				ret = true;
			}
			return ret;
		}
		
		//change simple name
		@Override
		public boolean visit(SimpleName node) {
			ASTNode parent = node.getParent();
			if(parent instanceof MethodDeclaration) {
				return true;
			}
			if(node.getIdentifier().equals(s1)) {
				System.out.println("replacing");
				SimpleName newName = ast.newSimpleName(s2);
				rewriter.replace(node, newName, null);
			}
			return true;
		}
		
	}

}
