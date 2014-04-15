package edu.mit.csail.sdg.alloy4compiler.generator;

import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4compiler.ast.*;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig.*;





public class TVisitor extends VisitQuery<Object> {
	
	//constructor for our Codegenerator Visitor.
	public TVisitor(){}
	
	//TODO extend ExprBinary
	//dummy for ExrpBinary visit. does nothing so far
    @Override public Object visit(ExprBinary x) throws Err {
    	switch(x.op){
    	//set cases
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
    		return "ISet<Tuple<" + x.left.accept(this) + ", " + x.right.accept(this) + ">>";
    	case JOIN:
    		return (String) x.right.accept(this);
    	//int and bool cases:
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
    		return "(" + x.left.accept(this) + ") == (" + x.right.accept(this) + ")";
    	case NOT_EQUALS:
    		return "(" + x.left.accept(this) + ") != (" + x.right.accept(this) + ")"; 
    	case IMPLIES:
    		return "(" + x.left.accept(this) + ") -: (" + x.right.accept(this) + ")";
    	case LT:
    		return "(" + x.left.accept(this) + ") < (" + x.right.accept(this) + ")";
    	case LTE:
    		return "(" + x.left.accept(this) + ") <= (" + x.right.accept(this) + ")";
    	case GT:
    		return "(" + x.left.accept(this) + ") < (" + x.right.accept(this) + ")";
    	case GTE:
    		return "(" + x.left.accept(this) + ") <= (" + x.right.accept(this) + ")";
		default: return "ExprBinary fail:" + x.op.toString();
    	}
    	
//    	still need to implement:
//        /** &amp;           */  INTERSECT("&",false),
//        /** set union       */  PLUS("+",false),
//        /** set diff        */  MINUS("-",false),
//        /** multiply        */  MUL("*",false),
//        /** !&lt;           */  NOT_LT("!<",false),
//        /** !=&lt;          */  NOT_LTE("!<=",false),
//        /** !&gt;           */  NOT_GT("!>",false),
//        /** !&gt;=          */  NOT_GTE("!>=",false),
//        /** in              */  IN("in",false),
//        /** !in             */  NOT_IN("!in",false),
//        /** &amp;&amp;      */  AND("&&",false),
//        /** ||              */  OR("||",false),
//        /** &lt;=&gt;       */  IFF("<=>",false);
    }
    

    /** Visits an ExprList node F[X1,X2,X3..] by calling accept() on X1, X2, X3... */
    @Override public Object visit(ExprList x) throws Err {
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

    /** Visits an ExprCall node F[X1,X2,X3..] by calling accept() on X1, X2, X3... */
    @Override public Object visit(ExprCall x) throws Err {
    	String res = "FuncClass." + x.fun.label.substring(5) + "(";
    	int i = 0;
    	for(Expr arg : x.args){
    		if(i>0) res += ", ";
    		res += arg.toString();
    		i++;
    	}
    	
    	return res + ")";
    }

    //TODO extend ExprConstant
    /** Visits an ExprConstant node (this default implementation simply returns null) */
    @Override public Object visit(ExprConstant x) throws Err {
        return x.toString();
    }

    /** Visits an ExprITE node (C => X else Y) by calling accept() on C, X, then Y. */
    @Override public Object visit(ExprITE x) throws Err {
    	//standard conditional b ? x : y syntax
    	return "(" + x.cond.accept(this) + ") ? (" + x.left.accept(this) + ") : (" + x.right.accept(this) + ")";
    }

//    /** Visits an ExprLet node (let a=x | y) by calling accept() on "a", "x", then "y". */
//    @Override public  Object visit(ExprLet x) throws Err {
//        Object ans = x.var.accept(this);
//        if (ans==null) ans = x.expr.accept(this);
//        if (ans==null) ans = x.sub.accept(this);
//        return ans;
//    }

    /** Visits an ExprQt node (all a,b,c:X1, d,e,f:X2... | F) by calling accept() on a,b,c,X1,d,e,f,X2... then on F. */
    @Override public Object visit(ExprQt x) throws Err {
    	String res = "";

    	switch(x.op){
    	/* general schema of all those cases:
    	 * -quantifier comes first
    	 * -print type of variable, then variable name
    	 * -lambda arrow
    	 * -visit sub expression
    	 * -close the quantifier expression
     	 */
    	case ALL:
    	//ALL: use FORALL contract
    		res += "Contract.ForAll(";
    		for(Decl d : x.decls){
        		for(ExprHasName v : d.names){
        			res += v.type().toExpr().accept(this) + "Set, ";
        			res += v.toString();
        			res += " => ";
        		}
    		}
    		
    		res += x.sub.accept(this);
    		res += ")";
    		break;
    	case NO:
		//NO: there should exist no instance where the predicate holds. use negated EXISTS contract
    		res += "!Contract.Exists(";
    		for(Decl d : x.decls){
        		for(ExprHasName v : d.names){
        			res += v.type().toExpr().accept(this) + "Set, ";
        			res += v.toString();
        			res += " => ";
        		}
    		}
    		res += x.sub.accept(this);
    		res += ")";
    		break;
    	case ONE: //EXACTL one
    		for(Decl d : x.decls){
        		for(ExprHasName v : d.names){
        			res += v.type().toExpr().accept(this) + "Set.Where(";
        			res += v.toString();
        			res += " => ";
        		}
    		}
    		res += x.sub.accept(this);
    		res += ").Count() == 1";
    		break;
    	case LONE: //set contains less or equal to 1
    		for(Decl d : x.decls){
        		for(ExprHasName v : d.names){
        			res += v.type().toExpr().accept(this) + "Set.Where(";
        			res += v.toString();
        			res += " => ";
        		}
    		}
    		res += x.sub.accept(this);
    		res += ").Count() <= 1";
    		break;
    	case SOME: //there should be one or more instances. use EXISTS contract
    		res += "Contract.Exists(";
    		for(Decl d : x.decls){
        		for(ExprHasName v : d.names){
        			res += v.type().toExpr().accept(this) + "Set, ";
        			res += v.toString();
        			res += " => ";
        		}
    		}
    		res += x.sub.accept(this);
    		res += ")";
    		break;
		default: res += "FAIL (" + x.op.toString() + ")";
    	}
    	return res;
    }

    //TODO extend ExprUnary (or is it even needed in assertions?)
    //right now it seems it is not used at all
    /** Visits an ExprUnary node (OP X) by calling accept() on X. */
    @Override public Object visit(ExprUnary x) throws Err {
    	switch(x.op){
    	case LONEOF:
    	case ONEOF:
    	case NOOP:
    		return (String) x.sub.accept(this);
    	case SOMEOF:
    	case SETOF:
    		return "ISet<" + x.sub.accept(this) + ">";
    	default: return x.op.toString() + "ExprUnary fail";
    	}
    }

    /** Visits a ExprVar node (this default implementation simply returns null) */
    @Override public Object visit(ExprVar x) throws Err {
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
		return x.label;
    }
}
