package edu.mit.csail.sdg.alloy4compiler.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;

import kodkod.ast.Relation;
import kodkod.instance.Tuple;
import kodkod.instance.TupleSet;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4.Pair;
import edu.mit.csail.sdg.alloy4.ErrorFatal;
import edu.mit.csail.sdg.alloy4compiler.ast.*;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig.*;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Tuple;
import edu.mit.csail.sdg.alloy4compiler.translator.A4TupleSet;

public final class TestGenerator {

  private TestGenerator(A4Solution solution, Iterable<Sig> sigs, Iterable<Pair<String, Expr>> assertions, Iterable<Command> cmds, String originalFilename, PrintWriter out) throws Err {
	  //create a visitor that generates string we can print
	  TVisitor tviso = new TVisitor();
	  
	  out.println("// This C# file is generated from " + originalFilename);
	  
	  //import c libraries
	  out.println("\nusing System;\n"
	  		+ "using System.Linq;\n"
			+ "using System.Collections.Generic;\n"
			+ "using System.Diagnostics.Contracts;\n");
	  
	  //declare Test class and Main method
	  out.println("public static class Test {\n"
	  		    + "  public static void Main(string[] args) {\n");
	  
	  
	  for(Sig s : sigs){
		  if(!s.builtin && s.label.startsWith("this/")){
			  
			  String clname = (String) tviso.visitThis(s);
			  out.println("    var " + clname + "Set = new HashSet<" + clname + ">();");
			  
			  
			  //get all the created instances of that particular Sig
			  for(A4Tuple instance: solution.eval(s)){
				  if(instance != null && instance.sig(0) != null && (instance.toString().startsWith(s.toString().substring(5)))/* && ((instance.sig(0).isOne == null) || (s.isOne != null))*/){
					  String atname = instance.toString().replace("$", "");
					  
					  out.println("    " + clname + " " + atname +  ";");
					  //now add the instance to the hashset of the class and also to ancestor classes if needed.
					  PrimSig ps = (PrimSig)s; //we need to assign s to another variable so we can iterate over the parent structure
					  String pname;
					  int plevel = 0; //parent level, we need this because we don't want to recreate new instances if we need to add them to a parent set again
					  while(ps != null && ps.label != "univ"){
						  pname = tviso.visit(ps); //get the name of this class for easier printing
						  if(s.isOne == null){ //if multiplicity of signature is ONE add the Instance object to the sets
							  if(plevel == 0) out.println("    " + pname + "Set.Add(" + atname + " = new " + pname + "());"); //first time, create new object
							  else out.println("    " + pname + "Set.Add(" + atname + ");"); //not first time, just add object
						  }
						  else{ //... else create a new object
							  if(plevel == 0) out.println("    " + pname + "Set.Add(" + atname + " = " + clname + ".Instance);"); //first time, instantiate object
							  else out.println("    " + pname + "Set.Add(" + atname + ");"); //not first time, just add object
						  }
						  //check the parents of this nodes (ancestor) classes
						  ps = ps.parent;
						  plevel++;
					  }
				  }
			  }		  	  
		  }
	  }
	  

	  /*now we go for the fields of the signature:
	   * the reason we do this here and not in the above
	   * loop through the signatures is that we want to
	   * avoid field assignments to objects that are
	   * created later in the c# code
	   */
	  for(Sig s : sigs){
		  if(!s.builtin && s.label.startsWith("this/")){
			  for(Field f : s.getFields()){
				  
				  //get relations tuples
				  for(A4Tuple relation: solution.eval(f)){

				  
				  //now some meta variables to keep track of change:
				  boolean haschanged = true;
				  String prevlabel = "";
				  
				  String leftatom = relation.atom(0).toString().replace("$", "");
				  String rightatom = relation.atom(1).toString();
				  String sndrightatom = "";
				  String rightclass = "";
				  String sndrightclass = "";
				  
				  //changed to false if we're still assigning to the same object field
				  if(prevlabel != "" && prevlabel.equals(leftatom)) haschanged = false;
				  
				  //look if we have an int in the rightatom
				  boolean isint = true;
				  try{
					 Integer.parseInt(rightatom);
				  }
				  catch(Exception e){
					  isint = false;
				  }

				  //check rightatom for bool value
				  if(rightatom.startsWith("boolean/True")) {
					  rightclass = "bool";
					  rightatom = "true";
				  }
				  else if(rightatom.startsWith("boolean/False")) {
					  rightclass = "bool";
					  rightatom = "false";
				  }
				  else {
					  if(isint) rightclass = "int";
					  else rightclass = rightatom.substring(0, rightatom.indexOf("$"));
					  rightatom = rightatom.replace("$", "");
				  }

				  //look if we have an int in sndrightatom
				  isint = true;
				  try{
					 Integer.parseInt(rightatom);
				  }
				  catch(Exception e){
					  isint = false;
				  }
				  
				  if(relation.arity() > 2) {
					  sndrightatom = relation.atom(2).toString();
					  //check sndrightatom for bool value
					  if(sndrightatom.startsWith("boolean/True")) {
						  sndrightclass = "bool";
						  sndrightatom = "true";
					  }
					  else if(sndrightatom.startsWith("boolean/False")) {
						  sndrightclass = "bool";
						  sndrightatom = "false";
					  }
					  else {
						  if(isint) sndrightclass = "int";
						  else sndrightclass = sndrightatom.substring(0, sndrightatom.indexOf("$"));
						  sndrightatom = sndrightatom.replace("$", "");
					  }
				  }

				  String fmult = f.decl().expr.mult().toString();
				  
				  //now either assign or add a value depending on whether the field is a set or single value
				  if(fmult == "set of" || fmult == "some of"){
					  if(sndrightatom == "") {
						  //create a hashset only if first iteration
						  if(haschanged) out.println("    " + leftatom + "." + f.label + " = new HashSet<" + rightclass + ">();");
						  //add atoms to that set:
						  out.println("    " + leftatom + "." + f.label + ".Add(" + rightatom + ");");
					  }
					  else{
						  //create a hashset of tuples only if first iteration
						  if(haschanged) out.println("    " + leftatom + "." + f.label + " = new HashSet<Tuple<" 
								  + rightclass + "," + sndrightclass + ">>();");
						  //add atoms to that set
						  out.println("    " + leftatom + "." + f.label + ".Add("
						  		+ "Tuple.Create<" + rightclass + "," + sndrightclass + ">"
						  				+ "(" + rightatom + "," + sndrightatom + "));");
					  }
				  }
				  else out.println("    " + leftatom + "." + f.label + " = " + rightatom + ";");
				  
				  prevlabel = leftatom;  
				  
				  }
			  }
		  }
	  }
	  
	  //TODO a comparison with "cmds" might be needed (see project guideline TestGenerator.writeTest)
	  //get assertions
	  //(a command represents either a 'run' or 'check')
	  out.println();
	  for(Pair<String,Expr> asspair : assertions){
		  //open contract
		  out.print("    Contract.Assert(");
		  
		  
		  out.print((String) tviso.visitThis(asspair.b)); //visiter les éxpressions
		  
		  //close contract
		  out.print(", \"" + asspair.a + "\");\n");
	  }
	  
	  
	  //some test output TODO remove. for debugging only
	  for(Entry<Relation, TupleSet> entry : solution.debugExtractKInstance().relationTuples().entrySet()){
		  Relation key = entry.getKey();
		  TupleSet tuples = entry.getValue();
		  
		  
		  out.print("// " + key.toString());
		  for(Tuple t : tuples){
			  out.print(" " + t.toString() + t.arity() + " ");
		  }
		  out.println();
	  }
	  

	  //close Test class and Main method
	  out.print("  }\n}\n");
	  
	  out.close();
  }


public static void writeTest(A4Solution solution, Module world, String originalFilename, boolean saveInDist) throws Err {
    try {
      String f;
      String ext = ".tests.cs";
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
      new TestGenerator(solution, world.getAllReachableSigs(), world.getAllAssertions(), world.getAllCommands(), originalFilename, out);
    }
    catch (Throwable ex) {
      if (ex instanceof Err) {
        throw (Err)ex;
      }
      else {
        throw new ErrorFatal("Error writing the generated C# test file.", ex);
      }
    }
  }
}
