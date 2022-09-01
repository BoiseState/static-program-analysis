package ast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import org.eclipse.jdt.core.dom.AST;
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

public class Rewriting {
	
	private File file;
	private ASTRewrite rewriter;
	private AST ast;
	
	public Rewriting(File file) throws IOException {
		this.file = file;
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
		
		//remove methods with more than 1 parameter
		
		@Override
		public boolean visit(MethodDeclaration node) {
			boolean ret = true;
			if(node.parameters().size() > 3) {
				rewriter.remove(node, null);
				ret = false;
			}
			return ret;
		}
		
		//change simple name
		@Override
		public boolean visit(SimpleName node) {
			//if(node.getFullyQualifiedName())
			System.out.println(node.getIdentifier());
			if(node.getIdentifier().equals("code1b")) {
				System.out.println("replacing");
				SimpleName newName = ast.newSimpleName("myVar");
				rewriter.replace(node, newName, null);
			}
			return true;
		}
		
		//adding use insertFirst
		@Override
		public void endVisit(CompilationUnit node) {
			ImportDeclaration id = ast.newImportDeclaration();
			id.setName(ast.newName("java.util.Set".split("\\.")));
			ListRewrite listRewrite = rewriter.getListRewrite(node, CompilationUnit.IMPORTS_PROPERTY);
			listRewrite.insertFirst(id, null);
		}
		
	}

}
