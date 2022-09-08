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

import com.ibm.wala.cfg.ControlFlowGraph;
import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.classLoader.JavaLanguage;
import com.ibm.wala.core.util.config.AnalysisScopeReader;
import com.ibm.wala.core.util.strings.StringStuff;
import com.ibm.wala.core.util.warnings.Warnings;
import com.ibm.wala.examples.util.ExampleUtil;
import com.ibm.wala.ipa.callgraph.AnalysisCache;
import com.ibm.wala.ipa.callgraph.AnalysisCacheImpl;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.ipa.callgraph.CallGraphBuilder;
import com.ibm.wala.ipa.callgraph.CallGraphBuilderCancelException;
import com.ibm.wala.ipa.callgraph.CallGraphStats;
import com.ibm.wala.ipa.callgraph.Entrypoint;
import com.ibm.wala.ipa.callgraph.impl.DefaultEntrypoint;
import com.ibm.wala.ipa.callgraph.impl.Util;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKey;
import com.ibm.wala.ipa.cfg.InterproceduralCFG;
import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.ipa.cha.ClassHierarchyFactory;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.ssa.ISSABasicBlock;
import com.ibm.wala.ssa.SSABinaryOpInstruction;
import com.ibm.wala.ssa.SSAConditionalBranchInstruction;
import com.ibm.wala.ssa.SSAInstruction;
import com.ibm.wala.ssa.SSAPutInstruction;
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
    long start = System.currentTimeMillis();
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
    System.out.println(cha.getNumberOfClasses() + " classes");
    System.out.println(Warnings.asString());
    Warnings.clear();
    AnalysisOptions options = new AnalysisOptions();
    Iterable<Entrypoint> entrypoints = entryClass != null ? makePublicEntrypoints(cha, entryClass) : Util.makeMainEntrypoints(cha, mainClass);
    options.setEntrypoints(entrypoints);
    options.getSSAOptions().setDefaultValues(SymbolTable::getDefaultValue);
    for(Entrypoint e : entrypoints) {
    	System.out.println("entry " + e);
    }
    // you can dial down reflection handling if you like
//    options.setReflectionOptions(ReflectionOptions.NONE);
    AnalysisCache cache = new AnalysisCacheImpl();
    // other builders can be constructed with different Util methods
    CallGraphBuilder<InstanceKey> builder = Util.makeZeroCFABuilder(new JavaLanguage(), options, cache, cha);//Util.makeZeroOneContainerCFABuilder(options, cache, cha);
//    CallGraphBuilder builder = Util.makeNCFABuilder(2, options, cache, cha, scope);
//    CallGraphBuilder builder = Util.makeVanillaNCFABuilder(2, options, cache, cha, scope);
    System.out.println("building call graph...");
    CallGraph cg = builder.makeCallGraph(options, null);
    long end = System.currentTimeMillis();
    System.out.println("done");
    System.out.println("took " + (end-start) + "ms");
    System.out.println("entry noted in CG " + cg.getEntrypointNodes());
    CGNode mainEntry = null;
    for(CGNode n : cg.getEntrypointNodes()) {
    	if(n.toString().contains(" main([Ljava/lang/String;)V"));
    	mainEntry = n;
    }
    System.out.println("Main Entry point is " + mainEntry);
    
    
    ExplodedControlFlowGraph eCFG = ExplodedControlFlowGraph.make(mainEntry.getIR());
    
    IExplodedBasicBlock entry = eCFG.entry();
    System.out.println(entry);
    
    
    List<IExplodedBasicBlock> q = new ArrayList<IExplodedBasicBlock>();
    q.add(entry);
    Set<IExplodedBasicBlock> seen = new HashSet<IExplodedBasicBlock>();
    while(!q.isEmpty()) {
    	IExplodedBasicBlock curr = q.remove(0);
    	System.out.println("Curr " + curr);
    	SSAInstruction instr = curr.getInstruction();
    	System.out.println("I " + instr + (instr!=null?instr.getClass():""));
    	if( instr instanceof SSAConditionalBranchInstruction) {
    		System.out.println("\t\t\t Branch!");
    		SSAConditionalBranchInstruction condInstr = (SSAConditionalBranchInstruction) instr;
    		System.out.println(condInstr.getUse(0)); 
    		System.out.println(condInstr.getUse(1)); 
    	}
    	if( instr instanceof SSABinaryOpInstruction) {
    		System.out.println("\t\t\t Assignment!");
    		SSABinaryOpInstruction binopInstr = (SSABinaryOpInstruction)instr;
    		System.out.println(binopInstr.getDef());
    		System.out.println(binopInstr.getUse(0));
    		System.out.println(binopInstr.getUse(1));
    		//System.out.println(binopInstr.getOperator());
    	}
    	Iterator<IExplodedBasicBlock> iterSucc = eCFG.getSuccNodes(curr);
    	while(iterSucc.hasNext()) {
    		IExplodedBasicBlock succ = iterSucc.next();
    		System.out.println("Succ " + succ);
    		System.out.println("S " + succ.getInstruction()+"\n");
    		if(!seen.contains(succ)) {
    			q.add(succ);
    		}
    		
    	}
    }
    
    
    /*
    
    InterproceduralCFG cfg = new InterproceduralCFG(cg);
    ControlFlowGraph<SSAInstruction, ISSABasicBlock> slice = cfg.getCFG(mainEntry);
    
    System.out.println(slice);
  //BFS
    List<ISSABasicBlock> q = new ArrayList<ISSABasicBlock>();
    System.out.println(slice.entry());
    //TODO: add to the queue
    q.add(slice.entry());
    
    Set<ISSABasicBlock> seen = new HashSet<ISSABasicBlock>();
    while(!q.isEmpty()) {
    	ISSABasicBlock curr = q.remove(0);
    	System.out.println(curr);
    	Iterator<SSAInstruction> instIter = curr.iterator();
		while(instIter.hasNext()) {
			System.out.println("I " + instIter.next());
		}
    	seen.add(curr);
    
    	Iterator<ISSABasicBlock> iter = slice.getSuccNodes(curr);
    	while(iter.hasNext()) {
    		ISSABasicBlock succ = iter.next();
    		
    		System.out.println("succ " + succ);
    		if(!seen.contains(succ)) {
    			q.add(succ);
    		}
    	}
    } 
    
    
    
    System.out.println(CallGraphStats.getStats(cg));
    */
  }

  private static Iterable<Entrypoint> makePublicEntrypoints(IClassHierarchy cha, String entryClass) {
    Collection<Entrypoint> result = new ArrayList<>();
    IClass klass = cha.lookupClass(TypeReference.findOrCreate(ClassLoaderReference.Application,
        StringStuff.deployment2CanonicalTypeString(entryClass)));
    for (IMethod m : klass.getDeclaredMethods()) {
      if (m.isPublic()) {
        result.add(new DefaultEntrypoint(m, cha));
      }
    }
    return result;
  }
}
