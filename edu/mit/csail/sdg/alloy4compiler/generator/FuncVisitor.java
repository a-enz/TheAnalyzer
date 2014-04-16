package edu.mit.csail.sdg.alloy4compiler.generator;

import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4compiler.ast.*;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig.*;

import java.util.List;




public class FuncVisitor extends VisitQuery<Object> {
	
	//constructor for our Codegenerator Visitor. Takes a Printwriter as argument
	public FuncVisitor(){};
	

	//dummy for ExrpBinary visit. does not distinguish the operator so far 
    @Override public String visit(ExprBinary x) throws Err {
    	switch(x.op) {
    	case ARROW:
    	case ANY_ARROW_SOME:
    	case ANY_ARROW_ONE:
    	case ANY_ARROW_LONE:
    	case SOME_ARROW_ANY:
    	case  SOME_ARROW_SOME:
    	case  SOME_ARROW_ONE:
    	case  SOME_ARROW_LONE:
    	case  ONE_ARROW_ANY:
    	case  ONE_ARROW_SOME:
    	case  LONE_ARROW_ANY:
    	case  LONE_ARROW_LONE:
    	case  LONE_ARROW_SOME:
    	case  LONE_ARROW_ONE:
    	case  ONE_ARROW_ONE:
    	case  ONE_ARROW_LONE:
    		return ("ISet<Tuple<" + x.left.toString().substring(5) + ", " + x.right.toString().substring(5) + ">>");
    	case JOIN:
    		return (x.left.accept(this) + "." + x.right.accept(this));
    	case INTERSECT:
//    		String returnString;
//    		if(/*A,A,A is simple object*/){
//    			returnString = "(ISet</*type of A*/>)" + "(" + x.left.accept(this) + ").Intersect(" + x.right.accept(this) + "))"; //TODO
//    		}else if(/*A,B are simple sets but different type*/){ //for example A:T1, B:T2
//    			returnString = "(ISet<Object>)" + "((ISet<Object>)" + x.left.accept(this) + ").Intersect((ISet<Object>)" + x.right.accept(this) + "))";
//    			//TODO invariant that elements in result are from type T1 or T2
//    		}else if(/*A,B are simple sets with same type*/){ //for example A:T, B:T
//    			returnString = "(ISet</*type A*/>)" + "(" + x.left.accept(this) + ").Intersect(" + x.right.accept(this) + "))";
//    			//TODO invariant that elements in result are from type T1 or T2
//    		}else if(/*A is set,B is simple object*/){//for example A:T->T, B:T
//    			returnString = "(ISet<Object>)" + "((ISet<Object>)" + x.left.accept(this) + ").Intersect((ISet<Object>)" + x.right.accept(this) + "))";
//    			//TODO invariant that elements in result are form type T->T or T
//    		}else if(/*B is set, A is simple object*/){//for example A:T3,B:T1->T2
//    			//TODO invariant that element in result are from type T1->T2 or T3
//    		}else if(/*A,B are relations of same type*/){//for example A:T->T, B:T->T
//    			returnString = "(ISet<Tuple</*type of A*/,/*type of A*/>)" + "(" + x.left.accept(this) + ").Intersect(" + x.right.accept(this) + "))"; //TODO
//    			//TODO invariant that result is from type T->T
//    		}else if(/*A,B are relations with different types*/){
//    			returnString = "(ISet<Object>)" + "((ISet<Object>)" + x.left.accept(this) + ").Intersect((ISet<Object>)" + x.right.accept(this) + "))";	
//    		}
//    		return returnString;
//    		String castTypeIntersect = x.left.type().toString();
//    		String type = castTypeIntersect.substring(6, castTypeIntersect.length()-1);
//    		return ("ISet<" + type + "> res = new HashSet<" + type + ">();\n"
//    				+ "IEnumerable<" + type + "> calc = ((" + x.left.accept(this) + ").Intersect(" + x.right.accept(this) + "))");
    	
       		String castTypeIntersect = x.left.type().toString();
    		return ("Helper.ToSet((" + x.left.accept(this) + ").Intersect(" + x.right.accept(this) + "))");
    	case PLUS:
    		String castTypePlus = x.left.type().toString();
    		return ("Helper.ToSet((" + x.left.accept(this) + ").Union(" + x.right.accept(this) + "))");
    	case IPLUS:
    		return ("(" + x.left.accept(this) + " + " + x.right.accept(this) + ")");
    	case MINUS:
    		String castTypeMinus = x.left.type().toString();
    		return ("Helper.ToSet((" + x.left.accept(this) + ").Except(" + x.right.accept(this) + "))");
    	case IMINUS:
    		return ("(" + x.left.accept(this) + " - " + x.right.accept(this) + ")");
    	case MUL:
    		return ("(" + x.left.accept(this) + " * " + x.right.accept(this) + ")");
    	case DIV:
    		return ("(" + x.left.accept(this) + " / " + x.right.accept(this) + ")");
    	case REM:
    		return ("(" + x.left.accept(this) + " % " + x.right.accept(this) + ")");
    	case EQUALS:
    		return ("(" + x.left.accept(this) + ").Equals(" + x.right.accept(this) + ")");
    	case NOT_EQUALS:
       		return ("!(" + x.left.accept(this) + ").Equals(" + x.right.accept(this) + ")");	
    	case IMPLIES:
       		return ("!(" + x.left.accept(this) + ") || (" + x.right.accept(this) + ")");	
    	case LT:
    		return ("(" + x.left.accept(this) + " < " + x.right.accept(this) + ")");
    	case LTE:
    		return ("(" + x.left.accept(this) + " <= " + x.right.accept(this) + ")");
    	case GT:
    		return ("(" + x.left.accept(this) + " > " + x.right.accept(this) + ")");
    	case GTE:
    		return ("(" + x.left.accept(this) + " >= " + x.right.accept(this) + ")");
    	case NOT_LT:
    		return ("!((" + x.left.accept(this) + " < " + x.right.accept(this) + "))");
    	case NOT_LTE:
    		return ("!((" + x.left.accept(this) + " <= " + x.right.accept(this) + "))");
    	case NOT_GT:
    		return ("!((" + x.left.accept(this) + " > " + x.right.accept(this) + "))");
    	case NOT_GTE:
    		return ("!((" + x.left.accept(this) + " >= " + x.right.accept(this) + "))");
    	case IN:
    		return ("(" + x.right.accept(this) + ").Contains(" + x.left.accept(this) + ")"); //andi: switched places of x.right & x.left
    	case NOT_IN:
    		return ("!(" + x.right.accept(this) + ").Contains(" + x.left.accept(this) + ")"); //andi: switched places of x.right & x.left
    	case AND:
    		return ("(" + x.left.accept(this) + ") && (" + x.right.accept(this) + ")");
    	case OR:
    		return ("(" + x.left.accept(this) + ") || (" + x.right.accept(this) + ")");
    	case IFF:
    		return ("(" + x.left.accept(this) + ") == (" + x.right.accept(this) + ")");
    	default:
    		return "noScpecifiedBinaryOperation";
    	}
    }
    
    
    

    /** Visits a Sig node (this default implementation simply returns null) */
    @Override public String visit(Sig x) throws Err {
    	//just return the label
    	if(x.label == "Int") return "int";
    	else if(x.label.startsWith("boolean/")) return "bool";
    	else return x.label.substring(5);

    }

    
    
    public String visit(Func f) throws Err {
		String params = ""; //string of all parameters
		String preconditions = ""; //string that has all "!=" statments for the parameters
		String postconditions = ""; //andi: postconditions
		boolean postNonNullCheck; //andi: postconditions
		PostconditionVisitor postVisitor = new PostconditionVisitor();

		boolean isPred = f.isPred;
		
		//return type string
		String returnType = "";
		if(isPred){
			returnType = "bool";
		}else{
			returnType = (String) f.returnDecl.accept(this);	
		}
		
		//andi: postconditions
		String postDeclMult = (String) f.returnDecl.accept(postVisitor); //andi: postconditions
		if(postDeclMult == "one of" || postDeclMult == "some of" || postDeclMult == "set of"){
			postNonNullCheck = true;
		}else{
			postNonNullCheck = false;
		}
		
		if(postNonNullCheck) postconditions = "\n\t\tContract.Ensures(Contract.Result<" + returnType + ">() != null);";
		
		//Parameter Construction
		List<Decl> declList = f.decls;
		
		for(Decl decl:declList){
			int numOfParams = decl.names.size();
			boolean preNonNullCheck;
			
			String preDeclMult = (String) decl.expr.accept(postVisitor);
			
			//checks whether non null check is needed or not
			if(preDeclMult == "one of" || preDeclMult == "some of" || preDeclMult == "set of"){
				preNonNullCheck = true;
			}else{
				preNonNullCheck = false;
			}
			
	
			
			String tempParams = "";
			for(int i = 0; i<numOfParams ;i++){
				//if.. else if.. addd by andi
//				if(!isPred && declMult == "one of" || declMult == "lone of") tempParams += " " + decl.names.get(i).type().toExpr().accept(this) + " " + decl.names.get(i).label + ",";
//				else if(!isPred && declMult == "some of" || declMult == "set of") tempParams += " ISet<" + decl.names.get(i).type().toExpr().accept(this) + "> " + decl.names.get(i).label + ",";
//				else tempParams += " " + decl.names.get(i).type().toExpr().accept(this) + " " + decl.names.get(i).label + ",";
				
				tempParams += " " + decl.expr.accept(this) + " " + decl.names.get(i).label + ","; //andi: simplified this so that we visit an expression to get the types and multiplicities
				
				
				if(preNonNullCheck){
					preconditions += "\n\t\tContract.Requires(" + decl.names.get(i).label + " != null);";
				}
			}
			//wrong statement?! cuts the "," at the end
			//tempParams = tempParams.substring(0, tempParams.length()-1);
			params += tempParams;
		}
		
		params = params.substring(1,params.length()-1);
		
		
		//body computation
		String body = (String) f.getBody().accept(this);
		
		//construct string to return
		
		return "\tpublic static "
				+ returnType
				+ " "
				+ f.label.substring(5)
				+ "("
				+ params + "){"
				+ preconditions
				+ postconditions
				+ "\n\t\treturn "
				+ body
				+ ";"
				+ "\n\t}";
		
    }
    
    public String visit(ExprVar f) throws Err{
    	return f.label;
    }
    
    public String visit(Field x) throws Err {
        return x.label;
    }
    
    
    
    public String visit(ExprUnary x) throws Err {
    	switch (x.op){
    		case CLOSURE: return "Helper.Closure(" + x.sub.accept(this) + ")";
            case RCLOSURE: return "Helper.RClosure(" + x.sub.accept(this) + ")";
    		case NOOP: return (String) x.sub.accept(this);
            case LONEOF: return (String) x.sub.accept(this);
            case ONEOF: return (String) x.sub.accept(this);
    		case SOMEOF:
            case SETOF: return "ISet<" + x.sub.accept(this) + ">";
            case NOT: return "!(" + x.sub.accept(this) + ")";
            case TRANSPOSE: break;
            case CARDINALITY: break;
            case CAST2INT:  break;
            case CAST2SIGINT: break;
    		default: break;
    	}
    	return x.op.toString();
    }
    
    
    // andi: added ExprList visitor (we need this in test23)
    /** Visits an ExprList node F[X1,X2,X3..] by calling accept() on X1, X2, X3... */
    @Override public String visit(ExprList x) throws Err {
    	/*we simply return all visited expressions in the list
    	 * concatinated with the proper operator
    	 */
    	String res = "";
    	int i = 0;
    	switch(x.op){
    	case AND:
	        for(Expr y : x.args){
	        	if(i>0) res += " && ";
	        	res += "(" + y.accept(this) + ")";
	        	i++;
	        }
	        break;
    	case OR:
    		for(Expr y : x.args){
	        	if(i>0) res += " || ";
	        	res += "(" + y.accept(this) + ")";
	        	i++;
	        }
    		break;
		default: res += "ExprList fail";
    	}
    	return res;
    }
    
    //andi: added same implementation of ExprITE as in all other visitors
    /** Visits an ExprITE node (C => X else Y) by calling accept() on C, X, then Y. */
    @Override public Object visit(ExprITE x) throws Err {
    	//standard conditional b ? x : y syntax
    	return "(" + x.cond.accept(this) + ") ? (" + x.left.accept(this) + ") : (" + x.right.accept(this) + ")";
    }
}


