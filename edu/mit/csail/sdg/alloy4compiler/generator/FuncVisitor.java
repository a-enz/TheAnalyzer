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
    		String castTypeIntersect = x.left.type().toString();
    		return ("(ISet<" + castTypeIntersect.substring(6, castTypeIntersect.length()-1) + ">)" + "((" + x.left.accept(this) + ").Union(" + x.right.accept(this) + "))");
    	case PLUS:
    		String castTypePlus = x.left.type().toString();
    		return ("(ISet<" + castTypePlus.substring(6, castTypePlus.length()-1) + ">)" + "((" + x.left.accept(this) + ").Union(" + x.right.accept(this) + "))");
    	case IPLUS:
    		return ("(" + x.left.accept(this) + " + " + x.right.accept(this) + ")");
    	case MINUS:
    		String castTypeMinus = x.left.type().toString();
    		return ("(ISet<" + castTypeMinus.substring(6, castTypeMinus.length()-1) + ">)" + "((" + x.left.accept(this) + ").Except(" + x.right.accept(this) + "))");
    	case IMINUS:
    		return ("(" + x.left.accept(this) + " - " + x.right.accept(this) + ")");
    	case MUL:
    		return ("(" + x.left.accept(this) + " * " + x.right.accept(this) + ")");
    	case DIV:
    		return ("(" + x.left.accept(this) + " / " + x.right.accept(this) + ")");
    	case REM:
    		return ("(" + x.left.accept(this) + " % " + x.right.accept(this) + ")");
    	case EQUALS:
    		return ("((" + x.left.accept(this) + ").Equals((" + x.right.accept(this) + ")))");
    	case NOT_EQUALS:
       		return ("(!(" + x.left.accept(this) + ").Equals((" + x.right.accept(this) + ")))");	
    	case IMPLIES:
       		return ("(!(" + x.left.accept(this) + ") || (" + x.right.accept(this) + "))");	
    	case LT:
    		return ("(" + x.left.accept(this) + " < " + x.right.accept(this) + ")");
    	case LTE:
    		return ("(" + x.left.accept(this) + " <= " + x.right.accept(this) + ")");
    	case GT:
    		return ("(" + x.left.accept(this) + " > " + x.right.accept(this) + ")");
    	case GTE:
    		return ("(" + x.left.accept(this) + " >= " + x.right.accept(this) + ")");
    	case NOT_LT:
    		return ("(!(" + x.left.accept(this) + " < " + x.right.accept(this) + "))");
    	case NOT_LTE:
    		return ("(!(" + x.left.accept(this) + " <= " + x.right.accept(this) + "))");
    	case NOT_GT:
    		return ("(!(" + x.left.accept(this) + " > " + x.right.accept(this) + "))");
    	case NOT_GTE:
    		return ("(!(" + x.left.accept(this) + " >= " + x.right.accept(this) + "))");
    	case IN:
    		return ("((" + x.right.accept(this) + ").Contains(" + x.right.accept(this) + "))");
    	case NOT_IN:
    		return ("(!(" + x.right.accept(this) + ").Contains(" + x.right.accept(this) + "))");
    	case AND:
    		return ("(" + x.left.accept(this) + " && " + x.right.accept(this) + ")");
    	case OR:
    		return ("(" + x.left.accept(this) + " || " + x.right.accept(this) + ")");
    	case IFF:
    		return ("(" + x.left.accept(this) + " == " + x.right.accept(this) + ")");
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
		PostconditionVisitor postVisitor = new PostconditionVisitor();

		boolean isPred = f.isPred;
		
		//return type string
		String returnType = "";
		if(isPred){
			returnType = "bool";
		}else{
			returnType = (String) f.returnDecl.accept(this);	
		}
		
		//Parameter Construction
		List<Decl> declList = f.decls;
		
		for(Decl decl:declList){
			int numOfParams = decl.names.size();
			boolean nonNullCheck;
			
			String declMult = (String) decl.expr.accept(postVisitor);
			
			//checks whether non null check is needed or not
			if(declMult == "one of" || declMult == "some of" || declMult == "set of"){
				nonNullCheck = true;
			}else{
				nonNullCheck = false;
			}
			
			String tempParams = "";
			for(int i = 0; i<numOfParams ;i++){
				//if.. else if.. addd by andi
				if(!isPred && declMult == "one of" || declMult == "lone of") tempParams += " " + decl.names.get(i).type().toExpr().accept(this) + " " + decl.names.get(i).label + ",";
				else if(!isPred && declMult == "some of" || declMult == "set of") tempParams += " ISet<" + decl.names.get(i).type().toExpr().accept(this) + "> " + decl.names.get(i).label + ",";
				else tempParams += " " + decl.names.get(i).type().toExpr().accept(this) + " " + decl.names.get(i).label + ",";
				if(nonNullCheck){
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
            case SETOF: return "ISet<" + x.sub.accept(this) + ">"; //changed by andi, Nino: merci
            //TODO this operators must be handled
            case NOT: return "!";
            case TRANSPOSE: break;
            case CARDINALITY: break;
            case CAST2INT:  break;
            case CAST2SIGINT: break;
    		default: break;
    	}
    	return x.op.toString();
    }
}
