package edu.mit.csail.sdg.alloy4compiler.generator;

import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4compiler.ast.*;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig.*;



public class ContractVisitor extends VisitQuery<Object> {
	
	//constructor for our Codegenerator Visitor.
	public ContractVisitor(String label, String contractType){
		this.label = label;
		this.contractType = contractType;
	}
	
	private String label;
	private String contractType;
	public boolean inSet = false;
	//TODO extend ExprBinary
	//dummy for ExrpBinary visit. does nothing so far
    @Override public String visit(ExprBinary x) throws Err {
    	//only handles super simple cases
    	switch(x.op){
    	//set stuff:
    	case ARROW: //poorly done
    		return contractType + "(Contract.ForAll(" + label + ", e1 => " + x.left.accept(this) + ".Equals(e1.Item1)));\n"
    				+ contractType + "(Contract.ForAll(" + label + ", e1 => " + x.right.accept(this) + ".Equals(e1.Item2)));\n";
    	case ANY_ARROW_SOME: //poorly done
    		return contractType + "(Contract.Exists(" + label + ", e1 => e1.Item2 != null));\n";
    	case ANY_ARROW_ONE: //poorly done
    		return contractType +"(Contract.ForAll(" + label + ", e1 => Contract.ForAll(" + label + ", e2 => (e1.Item2 == e2.Item2))));\n";
    	case ANY_ARROW_LONE:
    		return contractType + "(ANY_ARROW_LONE);\n";
    	case SOME_ARROW_ANY: //poorly done
    		return contractType + "(Contract.Exists(" + label + ", e1 => e1.Item1 != null));\n";
    	case  SOME_ARROW_SOME:
    		return contractType + "(SOME_ARROW_SOME);\n";
    	case  SOME_ARROW_ONE:
    		return contractType + "(SOME_ARROW_ONE);\n";
    	case  SOME_ARROW_LONE:
    		return contractType + "(SOME_ARROW_LONE);\n";
    	case  ONE_ARROW_ANY: //poorly done
    		return contractType +"(Contract.ForAll(" + label + ", e1 => Contract.ForAll(" + label + ", e2 => (e1.Item1 == e2.Item1))));\n";
    	case  ONE_ARROW_SOME:
    		return contractType + "(ONE_ARROW_SOME);\n";
    	case  LONE_ARROW_ANY:
    		return contractType + "(LONW_ARROW_ANY);\n";
    	case  LONE_ARROW_LONE:
    		return contractType + "(LONE_ARROW_LONE);\n";
    	case  LONE_ARROW_SOME:
    		return contractType + "(LONE_ARROW_SOME);\n";
    	case  LONE_ARROW_ONE:
    		return contractType + "(LONE_ARROW_ONE);\n";
    	case  ONE_ARROW_ONE: //poorly done
    		return contractType + "(Contract.ForAll(" + label + ", e1 => Contract.ForAll(" + label + ".Where(e2 => e2.Item1 == e1.Item1), e3 => (e1.Item2 == e3.Item2))));\n"
    				+ contractType + "(Contract.ForAll(" + label + ", e1 => Contract.ForAll(" + label + ".Where(e2 => e2.Item2 == e1.Item2), e3 => (e1.Item1 == e3.Item1))));\n";
    	case  ONE_ARROW_LONE:
    		return contractType + "(ONE_ARROW_LONE);\n";
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

    
    /** Visits a ExprVar node (this default implementation simply returns null) */
    @Override public String visit(ExprVar x) throws Err {
        return x.label;
    }
    
    /** Visits a Sig node (this default implementation simply returns null) */
    @Override public String visit(Sig x) throws Err {
    	//just return the label
    	if(x.label == "Int") return "int";
    	else if(x.label.startsWith("boolean/")) return "bool";
    	else return "new " + x.label.substring(5) + "()";
    }


	//field visit method.
    /** Visits a Field node (this default implementation simply returns null) */
    @Override public String visit(Field x) throws Err {
    	//dive into the field expression. fieldname is directly printed in CodeGenerator
    	return (String) x.label; //+ " " + x.label;
    }
}
