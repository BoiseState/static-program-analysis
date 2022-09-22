package dataflow;

import java.io.IOException;
import java.util.Properties;

import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.core.util.config.AnalysisScopeReader;
import com.ibm.wala.core.util.strings.StringStuff;
import com.ibm.wala.ipa.callgraph.AnalysisCache;
import com.ibm.wala.ipa.callgraph.AnalysisCacheImpl;
import com.ibm.wala.ipa.callgraph.AnalysisOptions;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.ipa.cha.ClassHierarchyFactory;
import com.ibm.wala.ipa.cha.IClassHierarchy;
import com.ibm.wala.ssa.IR;
import com.ibm.wala.ssa.analysis.ExplodedControlFlowGraph;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.types.TypeReference;
import com.ibm.wala.util.io.CommandLine;

public class IntroprocRechingDefsDriver {

	public static void main(String[] args) throws IOException, ClassHierarchyException {
		
		   Properties p = CommandLine.parse(args);
		    String scopeFile = p.getProperty("scopeFile");
		    String entryClass = p.getProperty("entryClass");
		    String method = p.getProperty("method");
		    if (method == null && entryClass == null) {
		      throw new IllegalArgumentException("specfied class and method to analyze");
		    }

		    AnalysisScope scope = AnalysisScopeReader.instance.readJavaScope(scopeFile, null, ScopeFileCFG.class.getClassLoader());
		    // set exclusions.  we use these exclusions as standard for handling JDK 8
		    ExampleUtil.addDefaultExclusions(scope);
		    IClassHierarchy cha = ClassHierarchyFactory.make(scope);
		    AnalysisOptions options = new AnalysisOptions();
		    AnalysisCache cache = new AnalysisCacheImpl(options.getSSAOptions());
		    //get the class object
		    IClass entryClassObj = cha.lookupClass(TypeReference.findOrCreate(ClassLoaderReference.Application, StringStuff.deployment2CanonicalTypeString(entryClass)));
		  //find a method we would like to analyze in that class
		    IMethod methodObj = null;
		    for(IMethod m : entryClassObj.getDeclaredMethods()) {
		    	if(m.getName().toString().equals(method)) {
		    		methodObj = m;
		    	}
		    }
		    
		    if(methodObj == null) {
		    	System.out.println("No method is found with name " + method);
		    	return;
		    }
		    
		IR mIR = cache.getIR(methodObj);
		ExplodedControlFlowGraph ecfg = ExplodedControlFlowGraph.make(mIR);;
		
		IntraprocReachingDefs RD = new IntraprocReachingDefs(ecfg, cha);
		
		RD.analyze();
		

	}

}
