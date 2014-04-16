package edu.mit.csail.sdg.alloy4compiler.generator;

import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4compiler.ast.*;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig.*;



public class SigVisitor extends VisitQuery<Object> {
	
	//constructor for our Codegenerator Visitor.
	public SigVisitor(){}
	
	public boolean inSet = false;
	//TODO extend ExprBinary
	//dummy for ExrpBinary visit. does nothing so far
    @Override public String visit(ExprBinary x) throws Err {
    	//only handles super simple cases
    	switch(x.op){
    	//set stuff:
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
    		if(inSet) return "Tuple<" + x.left.accept(this) + ", " + x.right.accept(this) + ">";
    		else {
    			inSet = true;
    			return "ISet<Tuple<" + x.left.accept(this) + ", " + x.right.accept(this) + ">>";
    		}
    	case JOIN:
    		return (String) x.right.accept(this);
    		//now for bool and int cases:
    	case IPLUS:
    		return "(" + x.left.accept(this) + ") + (" + x.right.accept(this) + ")";
       	case IMINUS:
    		return "(" + x.left.accept(this) + ") - (" + x.right.accept(this) + ")";
       	case MUL:
    		return "(" + x.left.accept(this) + ") * (" + x.right.accept(this) + ")";
       	case DIV:
    		return "(" + x.left.accept(this) + ") / (" + x.right.accept(this) + ")";
       	case REM:
    		return "(" + x.left.accept(this) + ") % (" + x.right.accept(this) + ")";
    	case EQUALS:
    		return "(" + x.left.accept(this) + ").Equals(" + x.right.accept(this) + ")";
    	case NOT_EQUALS:
    		return "!(" + x.left.accept(this) + ").Equals(" + x.right.accept(this) + ")"; 
    	case IMPLIES:
    		return "!(" + x.left.accept(this) + ") || (" + x.right.accept(this) + ")";
    	case LT:
    		return "(" + x.left.accept(this) + ") < (" + x.right.accept(this) + ")";
    	case NOT_LT:
    		return "!((" + x.left.accept(this) + ") < (" + x.right.accept(this) + "))";
    	case LTE:
    		return "(" + x.left.accept(this) + ") <= (" + x.right.accept(this) + ")";
    	case NOT_LTE:
    		return "!((" + x.left.accept(this) + ") <= (" + x.right.accept(this) + "))";
    	case GT:
    		return "(" + x.left.accept(this) + ") > (" + x.right.accept(this) + ")";
    	case NOT_GT:
    		return "!((" + x.left.accept(this) + ") > (" + x.right.accept(this) + "))";
    	case GTE:
    		return "(" + x.left.accept(this) + ") >= (" + x.right.accept(this) + ")";
    	case NOT_GTE:
    		return "!((" + x.left.accept(this) + ") >= (" + x.right.accept(this) + "))";
    	case IN:
    		return ("(" + x.right.accept(this) + ").Contains(" + x.left.accept(this) + ")");
    	case NOT_IN:
    		return ("!(" + x.right.accept(this) + ").Contains(" + x.left.accept(this) + ")");    	
    	case AND:
    		return ("(" + x.left.accept(this) + ") && (" + x.right.accept(this) + ")");
    	case OR:
    		return ("(" + x.left.accept(this) + ") || (" + x.right.accept(this) + ")");
    	case IFF:
    		return ("(" + x.left.accept(this) + ") == (" + x.right.accept(this) + ")");
    	case INTERSECT:
			return ("Helper.ToSet((" + x.left.accept(this) + ").Intersect(" + x.right.accept(this) + "))");
    	case PLUS: 
    		return ("Helper.ToSet((" + x.left.accept(this) + ").Union(" + x.right.accept(this) + "))");
    	case MINUS:
    		return ("Helper.ToSet((" + x.left.accept(this) + ").Except(" + x.right.accept(this) + "))");
    	default: return x.op.toString() + "ExprBinary fail";
    	
//    	still need to implement:
//      /** in              */  IN("in",false),
//      /** !in             */  NOT_IN("!in",false),
//      /** &amp;&amp;      */  AND("&&",false),
//      /** ||              */  OR("||",false),
//      /** &lt;=&gt;       */  IFF("<=>",false);
   
    	}
    }
    
    //TODO extend ExprUnary
    /** Visits an ExprUnary node (OP X) by calling accept() on X. */
    @Override public String visit(ExprUnary x) throws Err {
    	/* right now only bundles two simple cases: prints either a set
    	 * or just naked subexpression
       	 */
    	switch(x.op){
    	case LONEOF:
    	case ONEOF:
    	case NOOP: return (String) x.sub.accept(this);
    	case SOMEOF:
    	case SETOF:
    		if(inSet) return (String) x.sub.accept(this);
    		else {
    			inSet = true;
    			return "ISet<" + x.sub.accept(this) + ">";
    		}
		case CLOSURE: return "Helper.Closure(" + x.sub.accept(this) + ")";
        case RCLOSURE: return "Helper.RClosure(" + x.sub.accept(this) + ")";
        case NOT: return "!(" + x.sub.accept(this) + ")";
    	default: return x.op.toString() + "ExprUnary fail";
    	}
    }
    
    
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

    //should not be needed in SigVisitor (added the same as in TVis)
    /** Visits an ExprCall node F[X1,X2,X3..] by calling accept() on X1, X2, X3... */
    @Override public String visit(ExprCall x) throws Err {
    	String res = "FuncClass." + x.fun.label.substring(5) + "(";
    	int i = 0;
    	for(Expr arg : x.args){
    		if(i>0) res += ", ";
    		res += arg.toString();
    		i++;
    	}
    	
    	return res + ")";
    }

    //TODO extend ExprConstant cases (integers, bools)
    /** Visits an ExprConstant node (this default implementation simply returns null) */
    @Override public String visit(ExprConstant x) throws Err {
        return x.toString() + "ExprConstant fail";
    }

    
    /** Visits an ExprITE node (C => X else Y) by calling accept() on C, X, then Y. */
    @Override public String visit(ExprITE x) throws Err {
    	//standard conditional b ? x : y syntax
    	return "(" + x.cond.accept(this) + ") ? (" + x.left.accept(this) + ") : (" + x.right.accept(this) + ")";
    }

    //so far no idea how to handle this
    /** Visits an ExprLet node (let a=x | y) by calling accept() on "a", "x", then "y". */
    @Override public String visit(ExprLet x) throws Err {
    	return x.toString() + "ExprLet fail";
    }

    //should not be needed in SigVisitor
    /** Visits an ExprQt node (all a,b,c:X1, d,e,f:X2... | F) by calling accept() on a,b,c,X1,d,e,f,X2... then on F. */
    @Override public String visit(ExprQt x) throws Err {
    	return x.op.toString()	+ "ExprQt fail";
    }

    
    /** Visits a ExprVar node (this default implementation simply returns null) */
    @Override public String visit(ExprVar x) throws Err {
        return x.label;
    }
    
    /** Visits a Sig node (this default implementation simply returns null) */
    @Override public String visit(Sig x) throws Err {
    	//just return the label
    	if(x.label == "Int") return "int";
    	else if(x.label.startsWith("boolean/")) return "bool";
    	else return x.label.substring(5);
    }


	//field visit method.
    /** Visits a Field node (this default implementation simply returns null) */
    @Override public String visit(Field x) throws Err {
    	//dive into the field expression. fieldname is directly printed in CodeGenerator
    	return (String) x.decl().expr.accept(this); //+ " " + x.label;
    }
}
