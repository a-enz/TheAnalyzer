package edu.mit.csail.sdg.alloy4compiler.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4.SafeList;
import edu.mit.csail.sdg.alloy4.ErrorFatal;
import edu.mit.csail.sdg.alloy4compiler.ast.*;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig.*;

public final class CodeGenerator {

  private CodeGenerator(Iterable<Sig> sigs, SafeList<Func> funcs, String originalFilename, PrintWriter out, boolean checkContracts) throws Err {
	  
	  //create a visitor with the PrintWriter as an argument
	  SigVisitor sigvis = new SigVisitor();
	 
	  out.println("// This C# file is generated from " + originalFilename);
	  if(!checkContracts) out.println("\n#undef CONTRACTS_FULL");
	  
	  //import c libraries
	  out.println("\nusing System;\n"
	  		+ "using System.Linq;\n"
			+ "using System.Collections.Generic;\n"
			+ "using System.Diagnostics.Contracts;\n");
	  
	  //visit all non-builtin sigs
	  for(Sig s : sigs){

		  if(!s.builtin && s.label.startsWith("this/")){

			  String sname = sigvis.visit(s);
			  
			  //visit toplevel signatures and print their class declarations
			  if(s.isTopLevel()){
				  if(s.isAbstract != null) out.print("abstract public class "); //print abstract classes
				  else out.print("public class ");

				  out.print(sname + " {\n");
	
			  }
			  // now for the extends signatures
			  else{
				  if(s instanceof PrimSig){ //extending classes are apparently all PrimSig
					  //print out the extending class
					  out.print("public class " + sname + " : "); 
					  //now print out parent class
					  out.print(sigvis.visit(((PrimSig) s).parent) + " {\n");
				  }
			  }

			  // if the signature has multiplicity ONE create an instance of it
			  if(s.isOne != null){
				  //singleton design pattern (hopefully?)
				  out.print("  private static " + sname + " instance;\n");
				  out.print("  private " + sname + "() { }\n");
				  out.print("  public static " + sname + " Instance {\n"
						  + "    get {\n"
						  + "      if (instance == null) {\n"
						  + "        instance = new " + sname + "();\n"
						  + "      }\n"
						  + "      return instance;\n"
						  + "    }\n"
						  + "  }\n");
			  }

			  //if there are fields for this signature print their names and contracts
			  if (!s.getFields().isEmpty()){
				  //field declarations TODO: extend cases in sigvisitor
				  //right now it only prints the label
				  for(Field f : s.getFields()){
					  sigvis.inSet = false; //helper bool so we can assure that there will be no ISets of ISets
					  out.println("  public " + sigvis.visit(f) + " " + f.label + ";");
				  }
				  out.println();
				  
				  //TODO field invariant contracts
				  out.print("  [ContractInvariantMethod]\n"
						  + "  private void ObjectInvariant() {\n");

				  for(Field f : s.getFields()){
					  // check multiplicity of field and do non-null check one ONE fields
					  //could be made by visiting the Expr (ExprUnary in a signature with quantifiers case)
					  String fmult = f.decl().expr.mult().toString();
					  if(fmult == "one of" || fmult == "set of" || fmult == "some of"){
						  out.print("    Contract.Invariant(" + f.label + " != null);\n");
					  }
					  
					  if((f.decl().expr instanceof ExprBinary)) out.print(f.decl().expr.accept(new ContractVisitor(f.label, "    Contract.Invariant")));
				  }
				  
				  out.print("  }\n"); //close invariant
			  }

			  out.print("}\n\n"); //close class parenthesis 
		  }
	  }
	  
	  //TODO extend function contracts
	  
	  FuncVisitor funcvis = new FuncVisitor();
	  
	  out.println("public static class FuncClass {");
	  for(Func f : funcs){
		  String test = funcvis.visit(f);
		  out.println(test);
	  }
	  out.println("}");

	  
	  //TODO: finish Helper function
	  //for now just a dummy function:
	  out.print("public static class Helper {\n");
	  out.print("\npublic static ISet<Tuple<L, R>> Closure<L, R>(ISet<Tuple<L, R>> set) {"
				  	+ "\n\tTuple<L,R> typeTest = set.ElementAt(0);"
				  	+ "\n\tif(typeTest.Item2 is L)"
				  	+ "\n\t{"
		            + "\n\t\tbool changed = true;"
		            + "\n\t\twhile (changed)"
		            + "\n\t\t{"
		            + "\n\t\t\tforeach (Tuple<L, R> tuple0 in set)"
		            + "\n\t\t\t{"
		            + "\n\t\t\t\tif (tuple0.Item1 == null || tuple0.Item2 == null) return set;"
		            + "\n\t\t\t\tchanged = false;"
		            + "\n\t\t\t\tR source = tuple0.Item2;"
		            + "\n\t\t\t\tforeach (Tuple<L, R> tuple1 in set)"
		            + "\n\t\t\t\t{"
		            + "\n\t\t\t\t\tif (tuple0.Item1 == null || tuple0.Item2 == null) return set;"
		            + "\n\t\t\t\t\tif (source.Equals(tuple1.Item1))"
		            + "\n\t\t\t\t\t{"
		            + "\n\t\t\t\t\t\tTuple<L,R> newTuple = new Tuple<L, R>(tuple0.Item1, tuple1.Item2);"
		            + "\n\t\t\t\t\t\tif (!set.Contains(newTuple))"
		            + "\n\t\t\t\t\t\t{"
		            + "\n\t\t\t\t\t\t\tset.Add(newTuple);"
		            + "\n\t\t\t\t\t\t\tchanged = true;"
		            + "\n\t\t\t\t\t\t\t}"
		            + "\n\t\t\t\t\t}"
		            + "\n\t\t\t\t}"
		            + "\n\t\t\t}"
		            + "\n\t\t}"
		            + "\n\t}"
		            + "\n\treturn set;"
		            + "\n}");
	        out.print("\n\npublic static ISet<Tuple<L, R>> RClosure<L, R>(ISet<Tuple<L, R>> set) {"
	                + "\n\tISet<Tuple<L, R>> transitiveSet = Closure(set);"
	                + "\n\tforeach (Tuple<L, R> t in transitiveSet){"
	                + "\n\t\tTuple<L,L> leftTuple = new Tuple<L,L>(t.Item1, t.Item1);"
	                + "\n\t\tTuple<R, R> rightTuple = new Tuple<R, R>(t.Item2, t.Item2);"
	                + "\n\t\tif(!transitiveSet.Contains(leftTuple as Tuple<L,R>)){"
	                + "\n\t\t\ttransitiveSet.Add(leftTuple as Tuple<L, R>);"
	                + "\n\t\t}"
	                + "\n\t\tif (!transitiveSet.Contains(rightTuple as Tuple<L, R>)){"
	                + "\n\t\t\ttransitiveSet.Add(rightTuple as Tuple<L, R>);"
	                + "\n\t\t}"
	                + "\n\t}"
	                + "\n\treturn transitiveSet;"
	                + "\n}\n");
	        
	        out.print("public static ISet<T> ToSet<T>(IEnumerable<T> ienum) {\n"
	        		+ "  ISet<T> res = new HashSet<T>();\n"
	        		+ "  foreach(T item in ienum){\n"
	        		+ "    res.Add(item);\n"
	        		+ "  }\n"
	        		+ "  return res;\n"
	        		+ "}\n");
	  		out.print("\n}\n");
	  //close Printwriter
	  out.close();
  }

  public static void writeCode(Module world, String originalFilename, boolean checkContracts, boolean saveInDist) throws Err {
    try {
      String f;
      String ext = ".cs";
      if (saveInDist) {
        f = ".\\" + new File(originalFilename).getName() + ext;
      }
      else {
        f = originalFilename + ext;
      }
      File file = new File(f);
      if (file.exists()) {
        file.delete();
      }
      PrintWriter out = new PrintWriter(new FileWriter(f, true));
      new CodeGenerator(world.getAllReachableSigs(), world.getAllFunc(), originalFilename, out, checkContracts);
    }
    catch (Throwable ex) {
      if (ex instanceof Err) {
        throw (Err)ex;
      }
      else {
        throw new ErrorFatal("Error writing the generated C# code file.", ex);
      }
    }
  }
}
