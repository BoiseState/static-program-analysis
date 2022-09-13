/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package dataflow;

import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.classLoader.JavaLanguage;
import com.ibm.wala.core.util.config.AnalysisScopeReader;
import com.ibm.wala.core.util.strings.StringStuff;
import com.ibm.wala.ipa.callgraph.AnalysisCache;
import com.ibm.wala.ipa.callgraph.AnalysisCacheImpl;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.ipa.callgraph.CallGraphBuilder;
import com.ibm.wala.ipa.callgraph.CallGraphBuilderCancelException;
import com.ibm.wala.ipa.callgraph.Entrypoint;
import com.ibm.wala.ipa.callgraph.impl.DefaultEntrypoint;
import com.ibm.wala.ipa.callgraph.impl.Util;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKey;
import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.ipa.cha.ClassHierarchyFactory;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.SSABinaryOpInstruction;
import com.ibm.wala.ssa.SSAConditionalBranchInstruction;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSAReturnInstruction;
import com.ibm.wala.ssa.SymbolTable;
import com.ibm.wala.ssa.analysis.ExplodedControlFlowGraph;
import com.ibm.wala.ssa.analysis.IExplodedBasicBlock;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.io.CommandLine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Driver that constructs a call graph for an application specified via a scope file.  
 * Useful for getting some code to copy-paste.    
 */
public class ScopeFileCFG {

  /**
   * Usage: ScopeFileCallGraph -scopeFile file_path [-entryClass class_name |
   * -mainClass class_name]
   * 
   * If given -mainClass, uses main() method of class_name as entrypoint. If
   * given -entryClass, uses all public methods of class_name.
   * 
   * @throws IOException
   * @throws ClassHierarchyException
   * @throws CallGraphBuilderCancelException
   * @throws IllegalArgumentException
   */
  public static void main(String[] args) throws IOException, ClassHierarchyException, IllegalArgumentException,
      CallGraphBuilderCancelException {
    Properties p = CommandLine.parse(args);
    String scopeFile = p.getProperty("scopeFile");
    String entryClass = p.getProperty("entryClass");
    String mainClass = p.getProperty("mainClass");
    if (mainClass != null && entryClass != null) {
      throw new IllegalArgumentException("only specify one of mainClass or entryClass");
    }

    AnalysisScope scope = AnalysisScopeReader.instance.readJavaScope(scopeFile, null, ScopeFileCFG.class.getClassLoader());
    // set exclusions.  we use these exclusions as standard for handling JDK 8
    ExampleUtil.addDefaultExclusions(scope);
    IClassHierarchy cha = ClassHierarchyFactory.make(scope);
    AnalysisOptions options = new AnalysisOptions();
    Iterable<Entrypoint> entrypoints = entryClass != null ? makePublicEntrypoints(cha, entryClass) : Util.makeMainEntrypoints(cha, mainClass);
    options.setEntrypoints(entrypoints);
    //options.getSSAOptions().setDefaultValues(SymbolTable::getDefaultValue);
    for(Entrypoint e : entrypoints) {
    	System.out.println("entry " + e);
    }

    // you can dial down reflection handling if you like
//    options.setReflectionOptions(ReflectionOptions.NONE);
    AnalysisCache cache = new AnalysisCacheImpl(options.getSSAOptions());
   
    
    // other builders can be constructed with different Util methods
    CallGraphBuilder<InstanceKey> builder = Util.makeZeroCFABuilder(new JavaLanguage(), options, cache, cha);
    System.out.println("building call graph...");
    CallGraph cg = builder.makeCallGraph(options, null);
 
    //find the main method -- could be parametrized by the method name
    CGNode mainEntry = null;
    for(CGNode n : cg.getEntrypointNodes()) {
    	if(n.toString().contains("main(II)I"));
    	mainEntry = n;
    }
    System.out.println("Main Entry point is " + mainEntry);
    
    
    IMethod myM = mainEntry.getMethod();
    
    IR mIR = cache.getIR(myM);
    
    SymbolTable symbT = mIR.getSymbolTable();
    
    ExplodedControlFlowGraph eCFG = ExplodedControlFlowGraph.make(mIR);
    
    IExplodedBasicBlock entry = eCFG.entry();
    System.out.println(entry);
    
    
    List<IExplodedBasicBlock> q = new ArrayList<IExplodedBasicBlock>();
    q.add(entry);
    Set<IExplodedBasicBlock> seen = new HashSet<IExplodedBasicBlock>();
    
    while(!q.isEmpty()) {
    	IExplodedBasicBlock curr = q.remove(0);
    	System.out.println("Curr " + curr);
    	SSAInstruction instr = curr.getInstruction();
    	if(instr != null) {
    		System.out.println("I " + instr);
    	}
    	if( instr instanceof SSAConditionalBranchInstruction) {
    		System.out.println("\t\t\t Branch!");
    		SSAConditionalBranchInstruction condInstr = (SSAConditionalBranchInstruction) instr;
    		System.out.println(condInstr.getUse(0) + " " + symbT.getValueString(condInstr.getUse(0))); 
    		System.out.println(condInstr.getUse(1) + " " + symbT.getValueString(condInstr.getUse(1))); 
    	}
    	if( instr instanceof SSABinaryOpInstruction) {
    		System.out.println("\t\t\t Assignment!");
    		SSABinaryOpInstruction binopInstr = (SSABinaryOpInstruction)instr;
    		System.out.println(binopInstr.getDef() + " " + symbT.getValueString(binopInstr.getDef()));
    		System.out.println(binopInstr.getUse(0) + " " + symbT.getValueString(binopInstr.getUse(0)));
    		System.out.println(binopInstr.getUse(1) + " " + symbT.getValueString(binopInstr.getUse(1)));
    		//System.out.println(binopInstr.getOperator());
    	}
    	
    	if(instr instanceof SSAReturnInstruction) {
    		System.out.println("\t\t\t Return!");
    		SSAReturnInstruction retInstr = (SSAReturnInstruction) instr;
    		System.out.println(retInstr.getResult() + " " + symbT.getValueString(retInstr.getResult()));
    	}
    	Iterator<IExplodedBasicBlock> iterSucc = eCFG.getSuccNodes(curr);
    	while(iterSucc.hasNext()) {
    		IExplodedBasicBlock succ = iterSucc.next();
//    		System.out.println("Succ " + succ);
//    		System.out.println("S " + succ.getInstruction()+"\n");
    		if(!seen.contains(succ)) {
    			q.add(succ);
    			seen.add(succ);
    		}
    		
    	}
    }
    
  }

  private static Iterable<Entrypoint> makePublicEntrypoints(IClassHierarchy cha, String entryClass) {
    Collection<Entrypoint> result = new ArrayList<>();
    IClass klass = cha.lookupClass(TypeReference.findOrCreate(ClassLoaderReference.Application,
        StringStuff.deployment2CanonicalTypeString(entryClass)));
    System.out.println("klass " + klass);
    for (IMethod m : klass.getDeclaredMethods()) {
      if (m.isPublic()) {
        result.add(new DefaultEntrypoint(m, cha));
      }
    }
    return result;
  }
}
